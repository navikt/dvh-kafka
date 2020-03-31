package no.nav.dvhSykmKonsument.repository;

import no.nav.dvhSykmKonsument.model.dvh.MkIdent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MkIdentRepository extends CrudRepository<MkIdent, Long> {

    MkIdent findByPersonIdOff(String personIdOff);
}
