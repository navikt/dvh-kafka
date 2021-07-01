package no.nav.dvh.kafka.consumer.common.controller;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

@Controller
public class Metrikk {

    public static final String LEST = "meldinger_lest";
    public static final String IKKE_PROSESSERT = "meldinger_ikke_prosessert";
    public static final String PROSESSERT = "meldinger_prosessert";

    private final MeterRegistry registry;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    public Metrikk(MeterRegistry registry) {
        this.registry = registry;
    }

    @PostConstruct
    private void init() {
        registry.counter(
                addPrefix("info"),
                Tags.of("group", groupId)
        ).increment();
    }

    public void tellepunkt(String navn) {
        registry.counter(
                addPrefix(navn)
        ).increment();
    }

    private String addPrefix(String navn) {
        String METRIKK_PREFIX = "kafka_";
        return METRIKK_PREFIX + navn;
    }
}
