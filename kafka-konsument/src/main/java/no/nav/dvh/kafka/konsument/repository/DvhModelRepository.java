package no.nav.dvh.kafka.konsument.repository;

import no.nav.dvh.kafka.konsument.model.dvh.DvhModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DvhModelRepository extends CrudRepository<DvhModel, Long> {
}
