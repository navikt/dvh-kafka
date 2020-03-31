package no.nav.dvhSykmKonsument.config;

import kafka.Kafka;
import no.nav.dvhSykmKonsument.consumer.IKonsument;
import no.nav.dvhSykmKonsument.consumer.KafkaKonsument;
import no.nav.dvhSykmKonsument.model.dvh.Secret;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.*;
import org.springframework.util.backoff.FixedBackOff;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static no.nav.dvhSykmKonsument.consumer.IKonsument.*;
import static org.springframework.util.backoff.FixedBackOff.UNLIMITED_ATTEMPTS;

@EnableKafka
@Configuration
public class KafkaConfig {
    //TODO: Sett til topic du ønsker
    @Value("${kafka.topic.topic1}")
    private String topic1;

    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaBootstrapServers;

    @Value("${truststore.path}")
    private String truststorePath;

    @Value("${truststore.password}")
    private String truststorePassword;

    private String kafkaUsername;
    private String kafkaPassword;

    private String saslJaasConfig;

    @Autowired
    KafkaProperties kafkaProperties;

    @Autowired
    Secret secret;

    @Autowired
    ContainerProperties props;

    //TODO: Sett topic du ønsker
    @Bean
    public ContainerProperties containerProperties() {
        return new ContainerProperties(topic1);
    }

    //TODO: Sett til Konsumenerklassen
    @Bean
    public MessageListener<String, String> listener() {
        return new KafkaKonsument();
    }

    @Bean
    public KafkaMessageListenerContainer<String, String> container() {
        KafkaMessageListenerContainer<String, String> container =
                new KafkaMessageListenerContainer<>(consumerFactory(), props);
        container.setupMessageListener(listener());
        container.setErrorHandler(errorHandler());
        return container;
    }

    public KafkaConfig(Secret secret) {
        this.secret = secret;
        kafkaUsername = secret.getKafkaUsername();
        kafkaPassword = secret.getKafkaPassword();
    }

    @Bean
    public Map<String, Object> consumerConfigs() {

        kafkaProperties.setBootstrapServers(Arrays.asList(kafkaBootstrapServers));
        return kafkaProperties.buildConsumerProperties();
    }

    public ConsumerFactory<String, String> consumerFactory() {

        saslJaasConfig = String.format("org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\" password=\"%s\";", kafkaUsername, kafkaPassword);
        consumerConfigs().put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, truststorePath);
        consumerConfigs().put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, truststorePassword);
        consumerConfigs().put(SaslConfigs.SASL_JAAS_CONFIG, saslJaasConfig);

        ConsumerFactory<String, String> consumerFactory =
                new DefaultKafkaConsumerFactory<>(consumerConfigs());
        return consumerFactory;
    }

    @Bean
    public SeekToCurrentErrorHandler errorHandler() {
        final long THIRTY_MINUTES_INTERVAL = 1800000;
        SeekToCurrentErrorHandler handler = new SeekToCurrentErrorHandler(
                new FixedBackOff(THIRTY_MINUTES_INTERVAL, UNLIMITED_ATTEMPTS)
        );
        handler.addNotRetryableException(ParseReceivedSykmeldingException.class);
        return handler;
    }

    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                kafkaBootstrapServers);
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate(producerFactory());
    }


}
