package no.nav.dvh.kafka.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@ComponentScan
@PropertySource(value = "file:${vault.path}", ignoreResourceNotFound = true)
@PropertySource(value = "file:/var/run/secrets/nais.io/vault/secret.properties", ignoreResourceNotFound = true)
public class DVHKafkaStarterConfig {
}
