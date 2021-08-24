package no.nav.dvh.kafka.consumer.datasource.personidentity.mkident;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "fk_person")
class MkIdent {
    @Id
    private String personIdOff;
    @Getter
    private Long akPerson1;
    @Getter
    private String spesialregisterKode;
    public MkIdent() {
        akPerson1 = (long) -1;
    }
}
