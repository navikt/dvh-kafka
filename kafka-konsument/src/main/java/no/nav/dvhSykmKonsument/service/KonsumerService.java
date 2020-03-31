package no.nav.dvhSykmKonsument.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.dvhSykmKonsument.model.dvh.DvhModel;
import no.nav.dvhSykmKonsument.model.dvh.MkIdent;
import no.nav.dvhSykmKonsument.model.kilde.MottattMelding;
import no.nav.dvhSykmKonsument.repository.DvhModelRepository;
import no.nav.dvhSykmKonsument.repository.MkIdentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class KonsumerService {
        //TODO: Sett til hva du ønsker at kildesystemnavnet skal være i db.
        public static final String KILDESYSTEM = "Konsumernavn";

        @Autowired
        MkIdentRepository mkIdentRepository;

        @Autowired
        DvhModelRepository dvhModelRepository;

        private ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //TODO: Settes til kilde
        public MottattMelding lagMottattMelding(String mottattMelding) throws Exception {
                return mapper.readValue(mottattMelding, MottattMelding.class);
        }

        // TODO: Sett ønskede verdier til feltene som skal i databasen.
        public DvhModel mottattMeldingTilDvhModelMapper(MottattMelding mottattMelding) throws Exception {
                DvhModel dvhModel = new DvhModel();

                MkIdent mkIdent = hentMkIdent(mottattMelding.getPersonNr());

                long akPerson1 = sperretAdresse(mkIdent) ? -1 : mkIdent.getAkPerson1();

                dvhModel.setAkPerson1(akPerson1);

                return dvhModel;
        }

        //TODO: Settes til riktig repository
        public void lagreMelding(DvhModel dvhModel) throws Exception {
                dvhModelRepository.save(dvhModel);
        }

        private MkIdent hentMkIdent(String fodselsnummer) {
                MkIdent mkIdent = mkIdentRepository.findByPersonIdOff(fodselsnummer);
                return mkIdent == null ? new MkIdent() : mkIdent;
        }

        private boolean sperretAdresse(MkIdent mkIdent) {
                final String kode6 = "SPSF"; // Sperret adresse, strengt fortrolig
                final String kode7 = "SPFO"; // Sperret adresse, fortrolig
                String speisalRegisterKode = mkIdent.getSpesialregisterKode();
                return kode6.equalsIgnoreCase(speisalRegisterKode) || kode7.equalsIgnoreCase(speisalRegisterKode);
        }
}
