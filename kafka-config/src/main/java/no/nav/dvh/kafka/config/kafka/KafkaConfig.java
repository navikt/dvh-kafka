package no.nav.dvh.kafka.config.kafka;

import no.nav.dvh.kafka.config.consumer.IKonsument;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.*;
import org.springframework.util.backoff.FixedBackOff;

import java.util.Arrays;
import java.util.Map;

import static org.springframework.util.backoff.FixedBackOff.UNLIMITED_ATTEMPTS;

@EnableKafka
@Configuration
public class KafkaConfig {
    //TODO: Sett til topic du ønsker
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
    ContainerProperties props;

    @Autowired
    IKonsument konsument;

    //TODO: Sett topic du ønsker
    @Bean
    public ContainerProperties containerProperties() {
        return new ContainerProperties(topics);
    }

    //TODO: Sett til Konsumenerklassen
    @Bean
    public MessageListener<String, String> listener() {
        return konsument;
    }

    @Bean
    public KafkaMessageListenerContainer<String, String> container() {
        KafkaMessageListenerContainer<String, String> container =
                new KafkaMessageListenerContainer<>(consumerFactory(), props);
        container.setupMessageListener(listener());
        container.setErrorHandler(errorHandler());
        return container;
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
        handler.addNotRetryableException(IKonsument.ParseReceivedMessageException.class);
        return handler;
    }


}
