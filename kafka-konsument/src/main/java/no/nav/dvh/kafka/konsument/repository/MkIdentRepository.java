package no.nav.dvh.kafka.konsument.repository;

import no.nav.dvh.kafka.konsument.model.dvh.MkIdent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MkIdentRepository extends CrudRepository<MkIdent, Long> {

    MkIdent findByPersonIdOff(String personIdOff);
}
