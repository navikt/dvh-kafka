package no.nav.dvhSykmKonsument.producer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class DLQProdusent {
    @Value("${kafka.topic.dlq}")
    String topic;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(ConsumerRecord<String, String> record) {
        if (!record.topic().equals(topic))
            kafkaTemplate.send(topic, record.key(), record.value());
    }
}
