package no.nav.dvh.kafka.consumer.common.datasource.config;

import java.util.Map;

class DsUtil {
    static Map<String, String> hibernateNamingStrategy() {
        return Map.of(
                "hibernate.physical_naming_strategy", "org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy",
                "hibernate.implicit_naming_strategy", "org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy"
        );
    }
}
