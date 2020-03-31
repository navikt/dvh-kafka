package no.nav.dvhSykmKonsument.consumer;

import no.nav.dvhSykmKonsument.controller.Metrikk;
import no.nav.dvhSykmKonsument.model.dvh.DvhModel;
import no.nav.dvhSykmKonsument.model.kilde.MottattMelding;
import no.nav.dvhSykmKonsument.producer.DLQProdusent;
import no.nav.dvhSykmKonsument.service.KonsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Date;

public class KafkaKonsument implements IKonsument {
    @Autowired
    Metrikk metrikk;

    @Autowired
    DLQProdusent dlqProdusent;

    @Autowired
    KonsumerService service;

    @Override
    public DLQProdusent dlqProdusent() {
        return dlqProdusent;
    }

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

        dvhModel.setKildesystem(service.KILDESYSTEM);

        service.lagreMelding(dvhModel);
    }

}
