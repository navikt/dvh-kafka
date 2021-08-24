package no.nav.dvh.kafka.consumer.datasource.personidentity.mkident;

import no.nav.dvh.kafka.consumer.datasource.personidentity.PersonIdentity;
import no.nav.dvh.kafka.consumer.datasource.personidentity.PersonIdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("mkident")
class MkIdentService implements PersonIdentityService {
    @Autowired
    MkIdentRepository repo;

    public PersonIdentity getPersonIdent(String fnr) {
        var ident = getMkIdent(fnr);
        return PersonIdentity.builder()
                .fkPerson1(ident.getAkPerson1())
                .kode67(sperretAdresse(ident))
                .build();
    }

    private MkIdent getMkIdent(String fnr) {
        var mkIdent = repo.findByPersonIdOff(fnr);
        return mkIdent == null ? new MkIdent() : mkIdent;
    }

    private boolean sperretAdresse(MkIdent mkIdent) {
        final String kode6 = "SPSF"; // Sperret adresse, strengt fortrolig
        final String kode7 = "SPFO"; // Sperret adresse, fortrolig
        String speisalRegisterKode = mkIdent.getSpesialregisterKode();
        return kode6.equalsIgnoreCase(speisalRegisterKode) || kode7.equalsIgnoreCase(speisalRegisterKode);
    }
}
