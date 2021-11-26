package no.nav.dvh.kafka.consumer;

import lombok.SneakyThrows;
import no.nav.dvh.kafka.consumer.controller.Metrikk;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.kafka.support.Acknowledgment;

import java.time.*;
import java.util.Map;

import static no.nav.dvh.kafka.consumer.controller.Metrikk.*;

public abstract class AbstractListener<K, V> implements AcknowledgingMessageListener<K, V>, ConsumerSeekAware {
    Logger LOGGER =
            LoggerFactory.getLogger(AbstractListener.class);

    @Autowired
    private Metrikk metrikk;

    @Autowired
    private BatchInterval batchInterval;

    @Autowired
    private ConfigurableApplicationContext appContext;

    @SneakyThrows
    @Override
    public void onMessage(ConsumerRecord<K, V> record, Acknowledgment acknowledgment) {
        var stopDate = batchInterval.getStopDate();
        if (stopDate != null) {
            var stopDateInEpochMilli = LocalDate.parse(stopDate).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();

            if (record.timestamp() >= stopDateInEpochMilli) {
                appContext.close();
                return;
            }
        }
        LocalDateTime kafkaMottatDato = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(record.timestamp()), ZoneId.of("Europe/Oslo"));

        LocalDateTime lastetDato = LocalDateTime.now(ZoneId.of("Europe/Oslo"));

        metrikk.tellepunkt(LEST);
        try {
            prosseserMelding(record, kafkaMottatDato, lastetDato);
            acknowledgment.acknowledge();
        } catch (Exception e) {
            metrikk.tellepunkt(IKKE_PROSESSERT);
            LOGGER.error(
                    "Could not parse the following message from Kafka: " +
                            "Exception type: " + e.getClass().getName() +
                            ", Topic: " + record.topic() +
                            ", Partition: " + record.partition() +
                            ", Offset: " + record.offset()
            );
            prosseserFeilendeMeilding(record, kafkaMottatDato, lastetDato);
            if (stopDate != null) {
                appContext.close();
            }
            throw e;
        }
        metrikk.tellepunkt(PROSESSERT);
    }

    protected abstract void prosseserMelding(
            ConsumerRecord<K, V> record,
            LocalDateTime kafkaMottattDato,
            LocalDateTime lastetDato) throws Exception;

    protected abstract void prosseserFeilendeMeilding(
            ConsumerRecord<K, V> record,
            LocalDateTime kafkaMottattDato,
            LocalDateTime lastetDato) throws Exception;

    @Override
    public void onPartitionsAssigned(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
        var startDate = batchInterval.getStartDate();
        if (startDate != null) {
            var startDateInEpochMilli = LocalDate.parse(startDate).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
            callback.seekToTimestamp(assignments.keySet(), startDateInEpochMilli);
        }
    }
}
