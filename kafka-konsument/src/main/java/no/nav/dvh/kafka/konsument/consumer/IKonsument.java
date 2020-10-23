package no.nav.dvh.kafka.konsument.consumer;

import lombok.SneakyThrows;
import no.nav.dvh.kafka.konsument.controller.Metrikk;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NestedRuntimeException;
import org.springframework.kafka.listener.MessageListener;

import java.util.Date;

public interface IKonsument extends MessageListener<String, String> {

    String LEST = "meldinger_lest";
    String PROSESSERT = "meldinger_prosessert";

    Logger LOGGER =
            LoggerFactory.getLogger(IKonsument.class);

    Metrikk metrikk();

    @SneakyThrows
    @Override
    default void onMessage(ConsumerRecord<String,String> record) {
        try {
            metrikk().tellepunkt(LEST);
        } catch (Exception e) {
            LOGGER.warn("Unable to increment the read messages metric counter");
        }
        try {
            prosseserMelding(record.value(), record.topic(), record.partition(), record.offset(), new Date(record.timestamp()));
        } catch (NestedRuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new ParseReceivedSykmeldingException(record, e);
        }
        try {
            metrikk().tellepunkt(PROSESSERT);
        } catch (Exception e) {
            LOGGER.warn("Could not increment the processed messages metric counter");
        }

    }

    void prosseserMelding(String mottatMelding, String topic, int partisjon, long offset, Date mottatDato) throws Exception;

    class ParseReceivedSykmeldingException extends Exception {
        ParseReceivedSykmeldingException(ConsumerRecord<String, String> record, Exception e) {
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
