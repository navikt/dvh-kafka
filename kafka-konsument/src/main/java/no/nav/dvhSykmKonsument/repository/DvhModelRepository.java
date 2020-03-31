package no.nav.dvhSykmKonsument.repository;

import no.nav.dvhSykmKonsument.model.dvh.DvhModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DvhModelRepository extends CrudRepository<DvhModel, Long> {
}
