package no.nav.dvh.kafka.consumer.listener;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.SneakyThrows;
import no.nav.dvh.kafka.consumer.controller.Metrikk;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NestedRuntimeException;
import org.springframework.kafka.listener.MessageListener;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static no.nav.dvh.kafka.consumer.controller.Metrikk.*;

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
            metrikk().tellepunkt(LEST);
        } catch (Exception e) {
            LOGGER.warn("Unable to increment the read messages metric counter");
        }
        try {
            prosseserMelding(record, kafkaMottatDato, lastetDato);
        } catch (NestedRuntimeException e) {
            throw e;
        } catch (MismatchedInputException e) {
            metrikk().tellepunkt(IKKE_PROSESSERT);
            LOGGER.error(
                    "Could not parse the following message from Kafka producer: " +
                            "Exception type: " + e.getClass().getName() +
                            ", Received message key: " + record.key() +
                            ", Topic: " + record.topic() +
                            ", Partition: " + record.partition() +
                            ", Offset: " + record.offset()
            );
            prosseserFeilendeMeilding(record, kafkaMottatDato, lastetDato);
        }
        try {
            metrikk().tellepunkt(PROSESSERT);
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

}
