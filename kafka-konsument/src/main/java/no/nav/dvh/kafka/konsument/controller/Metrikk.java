package no.nav.dvh.kafka.konsument.controller;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import org.springframework.stereotype.Controller;

@Controller
public class Metrikk {

    public static final String LEST = "meldinger_lest";
    public static final String PROSESSERT = "meldinger_prosessert";
    public static final String IKKE_PROSSESERT = "meldinger_ikke_prossesert";

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
        //TODO: Sett prefiks til premetheus som brukes av grafana
        String METRIKK_PREFIX = "kafka_";
        return METRIKK_PREFIX + navn;
    }
}
