package no.nav.dvh.kafka.konsument.model.dvh;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(schema = "fk_model")
public class DvhModel {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    private long akPerson1;

    private Date lastetDato;

    private Date kafkaMottattDato;

    private String kafkaTopic;

    private int kafkaPartition;

    private long kafkaOffset;

    private String kildesystem;
}
