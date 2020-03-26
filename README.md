# Hvordan komme i gang med en kafka-konsument for datavarehus

Denne guiden er under utvikling og vill bestå av fem deler med alt en trenger for å utvikle __robuste__ kafka-konsumenter til bruk i datavarehuset. All funksjonalitet vill ikke forklares grundig, men guiden vill heller fungere som et rammeverk for hvordan nødvendig konfigurasjon kan gjøres basert på et ferdig oppsett. Dette vill forhåpentligvis minimere forskjellige løsninger internt i datavarehuset sånn at det er enklere å hjelpe hverandre på tvers av teams og skape en felles plattform for videreutvikling.

## Oversikt

* [Programvare](https://github.com/navikt/dvh-kafka/blob/master/README.md#Programvare)
  * [Programvareoversikt](https://github.com/navikt/dvh-kafka/blob/master/README.md#Programvareoversikt)
  * [Litt informasjon om programvaren](https://github.com/navikt/dvh-kafka/blob/master/README.md#Litt-informasjon-om-programvaren)
  * [Installasjon](https://github.com/navikt/dvh-kafka/blob/master/README.md#Installasjon)
* [Utviklingsmiljø](https://github.com/navikt/dvh-kafka/blob/master/utviklingsmiljo/README.md#)
  * [Installasjon](https://github.com/navikt/dvh-kafka/blob/master/utviklingsmiljo/README.md#Installasjon)
  * [Hvordan bruke miljøet](https://github.com/navikt/dvh-kafka/blob/master/utviklingsmiljo/README.md#Hvordan-bruke-miljøet)
* [Konfigurasjon av kafka-konsument](https://github.com/navikt/dvh-kafka/blob/master/kafka-konsument/README.md#)
* [Integrasjon](https://github.com/navikt/dvh-kafka/blob/master/kafka-konsument/README.md#Integrasjon)
* [Docker og Nais](https://github.com/navikt/dvh-kafka/blob/master/nais/README.md#)

## Programvare

Her er en oversikt over "utviklingstacken" med linker til informasjon om nedlasting og installasjon av programvaren. For å raskest komme i gang se [Installasjon](https://github.com/navikt/dvh-kafka/blob/master/README.md#Installasjon).

### Programvareoversikt

#### Nødvendig programvare

* [Git](https://git-scm.com)
  * [macOS](https://git-scm.com/downloads)
  * [Windows](https://git-scm.com/downloads)
* [IntelliJ](https://www.jetbrains.com/idea/download)
  * [macOS](https://www.jetbrains.com/idea/download)
  * [Windows](https://www.jetbrains.com/idea/download)
* [Docker](https://docker.com)
  * [macOS](https://docker.com/?overlay=onboarding)
  * [Windows](https://docker.com/?overlay=onboarding)
* [Python](https://www.python.org)
  * [macOS](https://www.python.org/downloads)
  * [Windows](https://www.python.org/downloads)

#### Anbefalt programvare for macOS

* [Homebrew](https://brew.sh/)

#### Anbefalt programvare for Windows

* [WSL2](https://docs.microsoft.com/en-us/windows/wsl/wsl2-install)

#### Anbefalt programvare for Python

* [PyEnv](https://github.com/pyenv/pyenv)
  * [macOS](https://github.com/pyenv/pyenv#homebrew-on-macos)
  * [Windows](https://github.com/pyenv-win/pyenv-win/blob/master/README.md#installation)
* [PipEnv](https://github.com/pypa/pipenv)
  * [macOS](https://pipenv.kennethreitz.org/en/latest/install/#homebrew-installation-of-pipenv)
  * [Windows](https://pipenv.kennethreitz.org/en/latest/install/#pragmatic-installation-of-pipenv)

### Litt informasjon om programvaren

For mer informasjon om programmene enn beskrevet her se linkene over.

#### Homebrew

Homebrew er en packamanager for macOS som gjør installasjon av programvare veldig enkelt. Homebrew gjør samme jobben som **apt-get** i Ubuntu.

#### Git

Git brukes til versjonskontroll av kildenkoden og er nødvendig for å laste ned og laste opp kildekoden til [Github](https://github.com/navikt)

#### IntelliJ

IntelliJ er den mest brukte IDE her i NAV IT og brukes for utvikling i Java.

#### PyEnv

PyEnv brukes av PipEnv for å laste ned og lage lokale python-miljøer for hvert enkelt prosjekt. Med andre ord så vill riktig Python versjon automatisk bli lastet ned og brukt i prosjektet i hennhold til hva som er satt i [Pipfile](https://github.com/navikt/dvh-kafka/blob/9f4003ce9f3fbcd38ca191849dd3b2b9a0210bfd/utviklingsmiljo/Pipfile#L13)

#### PipEnv

PipEnv lager automatisk lokale python-miljøer for hvert enkelt prosjekt i hennhold til [Pipfile](https://github.com/navikt/dvh-kafka/blob/master/utviklingsmiljo/Pipfile).

#### Docker Desktop

Docker brukes for å kjøre opp det vi trenger av servere i utviklingsmiljøet.

### Installasjon

Denne guiden fokuserer på den raskeste måten å installere nødvendig og anbefalt programvare for utvikling av kafka-konsumenter for datavarehus.

#### Installasjon for macOS og Windows med WSL2

Alle steder i guiden merket med `$ en kommando` betyr at du skal kjøre kommandoen (uten $) i terminalen din.

Eksempel:

`$ cd /home/`

Vi starter med å installere Homebrew.

`$ /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install.sh)"`

Du vill bli tilbudt å installere Command Line Developer Tools fra Apple. Trykk Install. Etter installasjonen er ferdig, trykk `Enter` igjen for å fortsette installasjonen av Homebrew. Etter installasjonen er ferdig:

`$ brew update`

`$ brew tap caskroom/cask`

`$ brew install git`

`$ brew install pyenv`

`$ brew install pipenv`

Velg hvem versjon av IntelliJ du ønsker å installere.

For IntelliJ Community Edition:

`$ brew cask install intellij-idea-ce`

For IntelliJ Ultimate Edition:

`$ brew cask install intellij-idea`

Docker installeres i hennhold til linken under [Programvareoversikt](https://github.com/navikt/dvh-kafka/blob/master/README.md#Programvareoversikt)

All done! :)

#### Installasjon for Windows

Siden installasjon av programvare er litt mer omfattende på Windows se må du følge guidene linket under [Programvareoversikt](https://github.com/navikt/dvh-kafka/blob/master/README.md#Programvareoversikt)

Hvis du har problemer med å få installert Docker på Windows kan [Docker Desktop WSL 2 backend](https://docs.docker.com/docker-for-windows/wsl-tech-preview/) være en mulig løsning.
