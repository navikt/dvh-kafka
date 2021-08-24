package no.nav.dvh.kafka.consumer.datasource.personidentity.dvhpersonidentoffid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "dt_person")
class DvhPersonIdentOffId {
    @Id
    private String offId;
    private long fkPerson1;
    private int skjermetKode;
    private LocalDate gyldigFraDato;
    private LocalDate gyldigTilDato;
}
