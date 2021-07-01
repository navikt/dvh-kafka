package no.nav.dvh.kafka.consumer.common.datasource.mkident.model;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(schema = "fk_person")
public class MkIdent {

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
