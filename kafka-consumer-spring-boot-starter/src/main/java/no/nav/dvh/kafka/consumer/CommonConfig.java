package no.nav.dvh.kafka.consumer;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@ComponentScan(basePackages = "no.nav.dvh.kafka.consumer.common")
@PropertySource(value = "file:${vault.path}", ignoreResourceNotFound = true)
@PropertySource(value = "file:/var/run/secrets/nais.io/vault/secret.properties", ignoreResourceNotFound = true)
class CommonConfig {
}
