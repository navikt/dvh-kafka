# Utviklingsmiljø

Miljøet består av tre deler: Kafka -server, mysql-database (mariaDB) og et adminpanel for databasen (adminer). Alle delene kjøres i docker-kontainere og kan ankelt startes med `$ docker-compose up` og enkelt byttes ut/konfigureres i [docker-compose.yml](https://github.com/navikt/dvh-kafka/blob/master/utviklingsmiljo/docker-compose.yml) Tanken bak miljøet er at en kan kjøre kafka-konsumeren fra et integrert utviklingsmiljø (IDE ekempelvis. IntelliJ) for å teste hendelseforløpet fra en melding blir lagt ut på et kafka-emne (topic) til den er prosessert og skrevet til databasen.

## Installasjon

Uten anbefalt programvare:

`$ pip install kafka-python`

Med anbefalt programvare:

`$ pipenv install`

## Hvordan bruke miljøet

### Starte miljøet

`$ docker-compose up`

### Produsere testmeldinger

Uten anbefalt programvare:

`$ python producer.py`

Med anbefalt programvare:

`$ pipenv run python producer.py`

> [producer.py](https://github.com/navikt/dvh-kafka/blob/master/utviklingsmiljo/producer.py) går igjennom alle filene undermappen kafka-meldinger med navngigningen "melding-*.json" Følg denne navngivingen hvis du vill legge til, fjerne eller forandre meldingene. Kafka-emne modifiseres i `producer.py`

## Teknisk informasjon

Under utvikling

### Om databasen

Under utvikling

**Adminpanel for databasen:** \
<http://localhost:8090>

### Om produsenten

Under utvikling
