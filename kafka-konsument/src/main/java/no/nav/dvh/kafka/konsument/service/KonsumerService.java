package no.nav.dvh.kafka.konsument.service;

import no.nav.dvh.kafka.config.datasource.mkident.MkIdentService;
import no.nav.dvh.kafka.config.datasource.mkident.model.MkIdent;
import no.nav.dvh.kafka.konsument.model.dvh.DvhModel;
import no.nav.dvh.kafka.konsument.model.kilde.MottattMelding;
import no.nav.dvh.kafka.konsument.repository.DvhModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static no.nav.dvh.kafka.config.MappingUtil.mottattMeldingMapper;


@Service
@Transactional
public class KonsumerService {

        @Autowired
        MkIdentService mkIdentService;

        @Autowired
        DvhModelRepository dvhModelRepository;

        //TODO: Settes til kilde
        public MottattMelding lagMottattMelding(String mottattMelding) throws Exception {
                return mottattMeldingMapper.readValue(mottattMelding, MottattMelding.class);
        }

        // TODO: Sett ønskede verdier til feltene som skal i databasen.
        public DvhModel mottattMeldingTilDvhModelMapper(MottattMelding mottattMelding) throws Exception {
                DvhModel dvhModel = new DvhModel();

                MkIdent mkIdent = mkIdentService.hentMkIdent(mottattMelding.getPersonNr());

                long akPerson1 = mkIdentService.sperretAdresse(mkIdent) ? -1 : mkIdent.getAkPerson1();

                dvhModel.setAkPerson1(akPerson1);

                return dvhModel;
        }

        //TODO: Settes til riktig repository
        public void lagreMelding(DvhModel dvhModel) throws Exception {
                dvhModelRepository.save(dvhModel);
        }
}
