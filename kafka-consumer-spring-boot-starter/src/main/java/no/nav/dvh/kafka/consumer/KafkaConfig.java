package no.nav.dvh.kafka.consumer;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.*;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;

import static io.confluent.kafka.serializers.KafkaAvroDeserializerConfig.*;
import static org.apache.kafka.clients.CommonClientConfigs.*;
import static org.apache.kafka.common.config.SaslConfigs.*;
import static org.apache.kafka.common.config.SslConfigs.*;
import static org.apache.kafka.common.security.auth.SecurityProtocol.*;
import static org.springframework.util.StringUtils.hasText;

@EnableKafka
@Configuration
class KafkaConfig {

    @Value("${dvh-kafka.topics:topic1}")
    private String[] topics;
    @Value("${dvh-kafka.serviceuser.username:}")
    private String serviceuserUsername;
    @Value("${dvh-kafka.serviceuser.password:}")
    private String serviceuserPassword;
    @Value("${KAFKA_BROKERS:localhost:9092}")
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
    private KafkaProperties kafkaProperties;

    @Autowired
    MessageListener listener;

    Map<String, Object> consumerProperties() {
        var props = kafkaProperties.buildConsumerProperties();
        var defaultBootstrapServers = Arrays.asList("localhost:9092");

        if (props.get(BOOTSTRAP_SERVERS_CONFIG).equals(defaultBootstrapServers)) {
            props.put(BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
        }

        if (hasText(serviceuserUsername)) {
            String saslJaasConfig = String.format("org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\" password=\"%s\";", serviceuserUsername, serviceuserPassword);
            props.putIfAbsent(SECURITY_PROTOCOL_CONFIG, SASL_SSL.name);
            props.putIfAbsent(SASL_MECHANISM, "PLAIN");
            props.putIfAbsent(SASL_JAAS_CONFIG, saslJaasConfig);
        }

        if (hasText(truststorePath)) {
            props.putIfAbsent(SSL_TRUSTSTORE_LOCATION_CONFIG, truststorePath);
            props.putIfAbsent(SSL_TRUSTSTORE_PASSWORD_CONFIG, truststorePassword);
        }

        if (hasText(keystorePath)) {
            props.putIfAbsent(SECURITY_PROTOCOL_CONFIG, SSL.name);
            props.putIfAbsent(SSL_KEYSTORE_LOCATION_CONFIG, keystorePath);
            props.putIfAbsent(SSL_KEYSTORE_PASSWORD_CONFIG, keystorePassword);
        }

        if (props.get(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG).equals(KafkaAvroDeserializer.class)) {
            props.putIfAbsent(BASIC_AUTH_CREDENTIALS_SOURCE, "USER_INFO");
            props.putIfAbsent(SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryURL);
            props.putIfAbsent(USER_INFO_CONFIG, "$schemaRegistryUser:$schemaRegistryUserPassword");
            props.putIfAbsent(SPECIFIC_AVRO_READER_CONFIG, true);
        }

        return props;
    }

    private ContainerProperties containerProperties() {
        ContainerProperties props = new ContainerProperties(topics);
        props.setAuthorizationExceptionRetryInterval(Duration.ofMinutes(1L));
        return props;
    }

    private ErrorHandler containerErrorHandler() {
        return new ContainerStoppingErrorHandler();
    }

    private ConsumerFactory consumerFactory() {
        return new DefaultKafkaConsumerFactory(consumerProperties());
    }

    @Bean
    KafkaMessageListenerContainer container() {
        KafkaMessageListenerContainer container =
                new KafkaMessageListenerContainer(consumerFactory(), containerProperties());
        container.setupMessageListener(listener);
        container.setErrorHandler(containerErrorHandler());
        return container;
    }
}
