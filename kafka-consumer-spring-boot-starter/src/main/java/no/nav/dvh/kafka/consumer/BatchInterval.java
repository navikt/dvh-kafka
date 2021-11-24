package no.nav.dvh.kafka.consumer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BatchInterval {
    private String startDate;
    private String stopDate;
}
