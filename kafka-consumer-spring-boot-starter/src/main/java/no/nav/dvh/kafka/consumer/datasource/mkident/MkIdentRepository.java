package no.nav.dvh.kafka.consumer.datasource.mkident;


import no.nav.dvh.kafka.consumer.datasource.mkident.model.MkIdent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface MkIdentRepository extends CrudRepository<MkIdent, Long> {

    MkIdent findByPersonIdOff(String personIdOff);
}
