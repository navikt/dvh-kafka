package no.nav.dvh.kafka.consumer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BatchInterval {
    private StartDate startDate;
    private StopDate stopDate;
    @Getter
    @Setter
    @NoArgsConstructor
    static class StartDate {
        private int year;
        private int month = 1;
        private int day = 1;
    }
    @Getter
    @Setter
    @NoArgsConstructor
    static class StopDate {
        private int year;
        private int month = 1;
        private int day = 1;
    }
}
