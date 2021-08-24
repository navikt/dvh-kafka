package no.nav.dvh.kafka.consumer.datasource.personidentity.dvhpersonidentoffid;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface DvhPersonIdentOffIdRepository extends CrudRepository<DvhPersonIdentOffId, Long> {
    @Query("select person from DvhPersonIdentOffId person where person.offId = :fnr and current_date <= gyldigTilDato")
    DvhPersonIdentOffId findByOffId(String fnr);
}
