package no.nav.dvh.kafka.konsument.controller;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import org.springframework.stereotype.Controller;

@Controller
public class Metrikk {

    private final MeterRegistry registry;

    public Metrikk(MeterRegistry registry) {
        this.registry = registry;
    }

    public void tellepunkt(String navn) {
        registry.counter(
                addPrefix(navn),
                Tags.of("type", "info")
        ).increment();
    }

    private String addPrefix(String navn) {
        String METRIKK_PREFIX = "dvh-sykm-konsument_";
        return METRIKK_PREFIX + navn;
    }
}
