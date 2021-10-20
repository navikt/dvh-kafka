package no.nav.dvh.kafka.consumer;

import lombok.SneakyThrows;
import no.nav.dvh.kafka.consumer.controller.Metrikk;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.kafka.listener.MessageListener;

import java.time.*;
import java.util.Map;

import static no.nav.dvh.kafka.consumer.controller.Metrikk.*;

public interface Listener<K, V> extends MessageListener<K, V>, ConsumerSeekAware {

    Logger LOGGER =
            LoggerFactory.getLogger(Listener.class);

    Metrikk metrikk();

    BatchInterval batchInterval();

    @SneakyThrows
    @Override
    default void onMessage(ConsumerRecord<K , V> record) {
        LocalDateTime kafkaMottatDato = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(record.timestamp()), ZoneId.of("Europe/Oslo"));

        LocalDateTime lastetDato = LocalDateTime.now(ZoneId.of("Europe/Oslo"));

        metrikk().tellepunkt(LEST);
        try {
            prosseserMelding(record, kafkaMottatDato, lastetDato);
        } catch (Exception e) {
            metrikk().tellepunkt(IKKE_PROSESSERT);
            LOGGER.error(
                    "Could not parse the following message from Kafka: " +
                            "Exception type: " + e.getClass().getName() +
                            ", Topic: " + record.topic() +
                            ", Partition: " + record.partition() +
                            ", Offset: " + record.offset()
            );
            prosseserFeilendeMeilding(record, kafkaMottatDato, lastetDato);
            throw e;
        }
        metrikk().tellepunkt(PROSESSERT);
    }

    void prosseserMelding(
            ConsumerRecord<K, V> record,
            LocalDateTime kafkaMottattDato,
            LocalDateTime lastetDato) throws Exception;

    void prosseserFeilendeMeilding(
            ConsumerRecord<K, V> record,
            LocalDateTime kafkaMottattDato,
            LocalDateTime lastetDato) throws Exception;

    default void onPartitionsAssigned(Map<TopicPartition, Long> assignments, ConsumerSeekAware.ConsumerSeekCallback callback) {
        var startDate = batchInterval().getStartDate();
        if (startDate != null) {
            var startDateInEpochMilli = LocalDate.of(startDate.getYear(), startDate.getMonth(), startDate.getDay()).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
            callback.seekToTimestamp(assignments.keySet(), startDateInEpochMilli);
        }
    }
}
