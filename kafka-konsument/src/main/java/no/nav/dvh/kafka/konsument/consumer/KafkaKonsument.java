package no.nav.dvh.kafka.konsument.consumer;

import no.nav.dvh.kafka.konsument.model.dvh.DvhModel;
import no.nav.dvh.kafka.konsument.service.KonsumerService;
import no.nav.dvh.kafka.konsument.controller.Metrikk;
import no.nav.dvh.kafka.konsument.model.kilde.MottattMelding;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class KafkaKonsument implements IKonsument {
    //TODO: Sett til hva du ønsker at kildesystemnavnet skal være i db.
    public static final String KILDESYSTEM = "Kildenavn";

    @Autowired
    Metrikk metrikk;

    @Autowired
    KonsumerService service;

    @Override
    public Metrikk metrikk() {
        return metrikk;
    }

    //TODO: initier prosesseringen og lagring til database
    @Override
    public void prosseserMelding(String mottatMelding, String topic, int partisjon, long offset, Date mottatDato) throws Exception {
        MottattMelding mottattMelding = service.lagMottattMelding(mottatMelding);
        DvhModel dvhModel = service.mottattMeldingTilDvhModelMapper(mottattMelding);

        dvhModel.setLastetDato(new Date());

        dvhModel.setKafkaTopic(topic);
        dvhModel.setKafkaPartition(partisjon);
        dvhModel.setKafkaOffset(offset);
        dvhModel.setKafkaMottattDato(mottatDato);

        dvhModel.setKildesystem(KILDESYSTEM);

        service.lagreMelding(dvhModel);
    }

}
