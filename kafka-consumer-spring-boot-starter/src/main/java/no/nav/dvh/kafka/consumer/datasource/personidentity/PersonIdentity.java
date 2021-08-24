package no.nav.dvh.kafka.consumer.datasource.personidentity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PersonIdentity {
    private long fkPerson1;
    private boolean kode67;
}
