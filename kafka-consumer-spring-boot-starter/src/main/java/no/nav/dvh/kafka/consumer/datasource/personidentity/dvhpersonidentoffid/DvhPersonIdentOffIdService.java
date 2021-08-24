package no.nav.dvh.kafka.consumer.datasource.personidentity.dvhpersonidentoffid;

import no.nav.dvh.kafka.consumer.datasource.personidentity.PersonIdentity;
import no.nav.dvh.kafka.consumer.datasource.personidentity.PersonIdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
class DvhPersonIdentOffIdService implements PersonIdentityService {
    @Autowired
    private DvhPersonIdentOffIdRepository repo;

    @Override
    public PersonIdentity getPersonIdent(String fnr) {
        var dvhIdent = getDvhPersonIdentOffId(fnr);
        return PersonIdentity.builder()
                .fkPerson1(dvhIdent.getFkPerson1())
                .kode67(sperretAdresse(dvhIdent.getSkjermetKode()))
                .build();
    }

    private DvhPersonIdentOffId getDvhPersonIdentOffId(String fnr) {
        var identity = repo.findByOffId(fnr);
        return identity != null ? identity : DvhPersonIdentOffId.builder()
                .fkPerson1(-1)
                .skjermetKode(0)
                .build();
    }

    private boolean sperretAdresse(int skjermetKode) {
        final int kode6 = 6; // Sperret adresse, strengt fortrolig
        final int kode7 = 7; // Sperret adresse, fortrolig
        return skjermetKode == kode6 || skjermetKode == kode7;
    }
}
