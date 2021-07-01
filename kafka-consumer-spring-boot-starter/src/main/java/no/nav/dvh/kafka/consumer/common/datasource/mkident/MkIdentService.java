package no.nav.dvh.kafka.consumer.common.datasource.mkident;

import no.nav.dvh.kafka.consumer.common.datasource.mkident.model.MkIdent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MkIdentService {
    @Autowired
    MkIdentRepository mkIdentRepository;

    public MkIdent hentMkIdent(String fodselsnummer) {
        MkIdent mkIdent = mkIdentRepository.findByPersonIdOff(fodselsnummer);
        return mkIdent == null ? new MkIdent() : mkIdent;
    }

    public boolean sperretAdresse(MkIdent mkIdent) {
        final String kode6 = "SPSF"; // Sperret adresse, strengt fortrolig
        final String kode7 = "SPFO"; // Sperret adresse, fortrolig
        String speisalRegisterKode = mkIdent.getSpesialregisterKode();
        return kode6.equalsIgnoreCase(speisalRegisterKode) || kode7.equalsIgnoreCase(speisalRegisterKode);
    }
}
