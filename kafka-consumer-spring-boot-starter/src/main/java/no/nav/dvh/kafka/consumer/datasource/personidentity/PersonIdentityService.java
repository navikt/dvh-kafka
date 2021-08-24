package no.nav.dvh.kafka.consumer.datasource.personidentity;

public interface PersonIdentityService {
    PersonIdentity getPersonIdent(String fnr);
}
