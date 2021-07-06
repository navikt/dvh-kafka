package no.nav.dvh.kafka.consumer.avro;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.security.auth.SecurityProtocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.ContainerStoppingErrorHandler;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;

import java.time.Duration;
import java.util.Map;

import static org.springframework.util.StringUtils.hasText;

@EnableKafka
@Configuration
class AvroConfig {

    @Value("${kafka.topics:topic1}")
    private String[] topics;
    @Value("${KAFKA_BROKERS:localhost:9091}")
    private String kafkaBootstrapServers;
    @Value("${KAFKA_TRUSTSTORE_PATH:}")
    private String truststorePath;
    @Value("${KAFKA_CREDSTORE_PASSWORD:}")
    private String truststorePassword;
    @Value("${KAFKA_KEYSTORE_PATH:}")
    private String keystorePath;
    @Value("${KAFKA_CREDSTORE_PASSWORD:}")
    private String keystorePassword;
    @Value("${KAFKA_SCHEMA_REGISTRY:http://localhost:8081}")
    private String schemaRegistryURL;
    @Value("${KAFKA_SCHEMA_REGISTRY_USER:}")
    private String schemaRegistryUser;
    @Value("${KAFKA_SCHEMA_REGISTRY_PASSWORD:}")
    private String schemaRegistryUserPassword;

    @Autowired
    KafkaProperties kafkaProperties;

    @Autowired
    AvroListener consumer;

    public Map<String, Object> consumerProperties() {
        var props = kafkaProperties.buildConsumerProperties();

        props.putIfAbsent(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);

        if (hasText(truststorePath)) {
            props.putIfAbsent(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, truststorePath);
            props.putIfAbsent(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, truststorePassword);
        }

        if (hasText(keystorePath)) {
            props.putIfAbsent(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, SecurityProtocol.SSL.name);
            props.putIfAbsent(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, keystorePath);
            props.putIfAbsent(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, keystorePassword);
        }

        props.putIfAbsent(KafkaAvroDeserializerConfig.BASIC_AUTH_CREDENTIALS_SOURCE, "USER_INFO");
        props.putIfAbsent(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryURL);
        props.putIfAbsent(KafkaAvroDeserializerConfig.USER_INFO_CONFIG, "$schemaRegistryUser:$schemaRegistryUserPassword");

        // Override this as spring kafka sets it to StringDeserializer by default
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);

        return props;
    }

    @Bean
    public KafkaMessageListenerContainer<String, GenericRecord> container() {
        KafkaMessageListenerContainer<String, GenericRecord> container =
                new KafkaMessageListenerContainer<>(consumerFactory(), containerProperties());
        container.setupMessageListener(consumer);
        container.setErrorHandler(new ContainerStoppingErrorHandler());
        return container;
    }

    public ConsumerFactory<String, GenericRecord> consumerFactory() {
        return new DefaultKafkaConsumerFactory(consumerProperties());
    }

    public ContainerProperties containerProperties() {
        ContainerProperties props = new ContainerProperties(topics);
        props.setAuthorizationExceptionRetryInterval(Duration.ofMinutes(1L));
        return props;
    }

}
