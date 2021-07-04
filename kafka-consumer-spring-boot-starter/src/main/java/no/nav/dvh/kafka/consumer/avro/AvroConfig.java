package no.nav.dvh.kafka.consumer.avro;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
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
import org.springframework.kafka.listener.MessageListener;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;

@EnableKafka
@Configuration
class AvroConfig {

    @Value("${kafka.topics}")
    private String[] topics;

    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaBootstrapServers;

    @Value("${truststore.path}")
    private String truststorePath;

    @Value("${truststore.password}")
    private String truststorePassword;

    @Value("${serviceuser.username}")
    private String kafkaUsername;

    @Value("${serviceuser.password}")
    private String kafkaPassword;

    private String saslJaasConfig;

    @Autowired
    KafkaProperties kafkaProperties;

    @Autowired
    AvroListener konsument;

    public ContainerProperties containerProperties() {
        ContainerProperties props = new ContainerProperties(topics);
        props.setAuthorizationExceptionRetryInterval(Duration.ofMinutes(1L));
        return props;
    }

    public MessageListener<String, GenericRecord> listener() {
        return konsument;
    }

    @Bean
    public KafkaMessageListenerContainer<String, GenericRecord> container() {
        KafkaMessageListenerContainer<String, GenericRecord> container =
                new KafkaMessageListenerContainer<>(consumerFactory(), containerProperties());
        container.setupMessageListener(listener());
        container.setErrorHandler(errorHandler());
        return container;
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        kafkaProperties.setBootstrapServers(Arrays.asList(kafkaBootstrapServers));
        return kafkaProperties.buildConsumerProperties();
    }

    @Bean
    public ConsumerFactory<String, GenericRecord> consumerFactory() {

        saslJaasConfig = String.format("org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\" password=\"%s\";", kafkaUsername, kafkaPassword);
        consumerConfigs().put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, truststorePath);
        consumerConfigs().put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, truststorePassword);
        consumerConfigs().put(SaslConfigs.SASL_JAAS_CONFIG, saslJaasConfig);
        consumerConfigs().put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerConfigs().put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);

        ConsumerFactory<String, GenericRecord> consumerFactory =
                new DefaultKafkaConsumerFactory<>(consumerConfigs());
        return consumerFactory;
    }

    @Bean
    public ContainerStoppingErrorHandler errorHandler() {
        return new ContainerStoppingErrorHandler();
    }

}
