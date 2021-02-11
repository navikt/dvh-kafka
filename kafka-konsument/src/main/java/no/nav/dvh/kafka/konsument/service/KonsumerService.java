package no.nav.dvh.kafka.konsument.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import no.nav.dvh.kafka.config.datasource.mkident.model.MkIdent;
import no.nav.dvh.kafka.konsument.model.dvh.DvhModel;
import no.nav.dvh.kafka.konsument.model.kilde.MottattMelding;
import no.nav.dvh.kafka.konsument.repository.DvhModelRepository;
import no.nav.dvh.kafka.config.datasource.mkident.repository.MkIdentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class KonsumerService {

        @Autowired
        MkIdentRepository mkIdentRepository;

        @Autowired
        DvhModelRepository dvhModelRepository;

        private ObjectMapper mottattMeldingMapper = new ObjectMapper()
                .setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE)
                .setVisibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.NONE)
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .setVisibility(PropertyAccessor.CREATOR, JsonAutoDetect.Visibility.ANY)
                .registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        //TODO: Settes til kilde
        public MottattMelding lagMottattMelding(String mottattMelding) throws Exception {
                return mottattMeldingMapper.readValue(mottattMelding, MottattMelding.class);
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
