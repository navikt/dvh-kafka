package no.nav.dvh.kafka.consumer;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SaslConfigs;
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
import org.springframework.kafka.listener.*;

import java.time.Duration;
import java.util.Map;

import static org.springframework.util.StringUtils.hasText;

@EnableKafka
@Configuration
class KafkaConfig {

    @Value("${kafka.topics:topic1}")
    private String[] topics;
    @Value("${kafka.serviceuser.username:}")
    private String serviceuserUsername;
    @Value("${kafka.serviceuser.password:}")
    private String serviceuserPassword;
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
    private KafkaProperties kafkaProperties;

    @Autowired
    MessageListener listener;

    Map<String, Object> consumerProperties() {
        var props = kafkaProperties.buildConsumerProperties();

        props.putIfAbsent(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);

        if (hasText(serviceuserUsername)) {
            String saslJaasConfig = String.format("org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\" password=\"%s\";", serviceuserUsername, serviceuserPassword);
            props.putIfAbsent(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, SecurityProtocol.SASL_PLAINTEXT.name);
            props.putIfAbsent(SaslConfigs.SASL_JAAS_CONFIG, saslJaasConfig);
        }

        if (hasText(truststorePath)) {
            props.putIfAbsent(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, truststorePath);
            props.putIfAbsent(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, truststorePassword);
        }

        if (hasText(keystorePath)) {
            props.putIfAbsent(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, SecurityProtocol.SSL.name);
            props.putIfAbsent(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, keystorePath);
            props.putIfAbsent(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, keystorePassword);
        }

        if (props.get(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG).equals(KafkaAvroDeserializer.class)) {
            props.putIfAbsent(KafkaAvroDeserializerConfig.BASIC_AUTH_CREDENTIALS_SOURCE, "USER_INFO");
            props.putIfAbsent(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryURL);
            props.putIfAbsent(KafkaAvroDeserializerConfig.USER_INFO_CONFIG, "$schemaRegistryUser:$schemaRegistryUserPassword");
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
