package no.nav.dvh.kafka.konsument.model.dvh;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

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
