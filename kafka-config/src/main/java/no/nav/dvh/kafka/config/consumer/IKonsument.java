package no.nav.dvh.kafka.config.consumer;

import lombok.SneakyThrows;
import no.nav.dvh.kafka.config.controller.Metrikk;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NestedRuntimeException;
import org.springframework.kafka.listener.MessageListener;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public interface IKonsument extends MessageListener<String, String> {

    Logger LOGGER =
            LoggerFactory.getLogger(IKonsument.class);

    Metrikk metrikk();

    @SneakyThrows
    @Override
    default void onMessage(ConsumerRecord<String,String> record) {
        LocalDateTime kafkaMottatDato = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(record.timestamp()), ZoneId.of("Europe/Oslo"));

        LocalDateTime lastetDato = LocalDateTime.now(ZoneId.of("Europe/Oslo"));

        try {
            metrikk().tellepunkt(Metrikk.LEST);
        } catch (Exception e) {
            LOGGER.warn("Unable to increment the read messages metric counter");
        }
        try {
            prosseserMelding(record, kafkaMottatDato, lastetDato);
        } catch (NestedRuntimeException e) {
            throw e;
        } catch (Exception e) {
            metrikk().tellepunkt(Metrikk.IKKE_PROSSESERT);
            prosseserFeilendeMeilding(record, kafkaMottatDato, lastetDato);
            throw new ParseReceivedMessageException(record, e);
        }
        try {
            metrikk().tellepunkt(Metrikk.PROSESSERT);
        } catch (Exception e) {
            LOGGER.warn("Could not increment the processed messages metric counter");
        }

    }

    void prosseserMelding(
            ConsumerRecord<String, String> record,
            LocalDateTime kafkaMottattDato,
            LocalDateTime lastetDato) throws Exception;

    void prosseserFeilendeMeilding(
            ConsumerRecord<String, String> record,
            LocalDateTime kafkaMottattDato,
            LocalDateTime lastetDato) throws Exception;

    class ParseReceivedMessageException extends Exception {
        ParseReceivedMessageException(ConsumerRecord<String, String> record, Exception e) {
            LOGGER.error(
                    "Could not parse the following message from Kafka producer: " +
                            "Exception type: " + e.getClass().getName() +
                            ", Received message key: " + record.key() +
                            ", Topic: " + record.topic() +
                            ", Partition: " + record.partition() +
                            ", Offset: " + record.offset()
            );
        }
    }

}
