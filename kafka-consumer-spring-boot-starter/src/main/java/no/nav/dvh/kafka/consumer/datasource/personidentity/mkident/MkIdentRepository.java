package no.nav.dvh.kafka.consumer.datasource.personidentity.mkident;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface MkIdentRepository extends CrudRepository<MkIdent, Long> {
    MkIdent findByPersonIdOff(String personIdOff);
}
