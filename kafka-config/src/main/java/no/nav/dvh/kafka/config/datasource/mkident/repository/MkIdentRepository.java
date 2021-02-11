package no.nav.dvh.kafka.config.datasource.mkident.repository;


import no.nav.dvh.kafka.config.datasource.mkident.model.MkIdent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MkIdentRepository extends CrudRepository<MkIdent, Long> {

    MkIdent findByPersonIdOff(String personIdOff);
}
