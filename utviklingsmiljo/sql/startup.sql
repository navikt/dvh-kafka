-- Data

-- Adminer 4.7.5 MySQL dump

SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

DROP TABLE IF EXISTS `hibernate_sequence`;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `hibernate_sequence` (`next_val`) VALUES
(1),
(1),
(1);

DROP TABLE IF EXISTS `mk_ident`;
CREATE TABLE `mk_ident` (
  `ak_person1` int(38) NOT NULL,
  `person_id_off` varchar(11) DEFAULT NULL,
  `spesialregister_kode` char(4) DEFAULT NULL,
  `gyldig_fra_dato` datetime DEFAULT NULL,
  `gyldig_til_dato` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `mk_ident` (`ak_person1`, `person_id_off`, `spesialregister_kode`, `gyldig_fra_dato`, `gyldig_til_dato`) VALUES
(6,	'21312313',	'SPSF',	NULL,	NULL),
(7,	'01234',	'SPFO',	NULL,	NULL),
(1,	'08048842411',	'VABO',	NULL,	NULL),
(0,	'56789',	NULL,	NULL,	NULL);

DROP TABLE IF EXISTS `sykm_sykmelding`;
CREATE TABLE `sykm_sykmelding` (
  `id` int(11) NOT NULL,
  `andre_tiltak_flagg` int(11) NOT NULL,
  `arbeidsgiver_navn` varchar(255) DEFAULT NULL,
  `avsender_system` varchar(255) DEFAULT NULL,
  `behandler_aktor_id` varchar(255) DEFAULT NULL,
  `behandler_fnr` varchar(255) DEFAULT NULL,
  `behandler_navn` varchar(255) DEFAULT NULL,
  `behandlet_dato` datetime DEFAULT NULL,
  `hoved_diagnose_kode` varchar(255) DEFAULT NULL,
  `hoved_diagnose_system` varchar(255) DEFAULT NULL,
  `kafka_mottatt_dato` datetime DEFAULT NULL,
  `kafka_offset` bigint(20) NOT NULL,
  `kafka_partisjon` int(11) NOT NULL,
  `kafka_topic` varchar(255) DEFAULT NULL,
  `kildesystem` varchar(255) DEFAULT NULL,
  `lastet_dato` datetime DEFAULT NULL,
  `melding_til_arbeidsgiver_flagg` int(11) NOT NULL,
  `melding_til_nav_flagg` int(11) NOT NULL,
  `mottatt_dato` datetime DEFAULT NULL,
  `pasient_aktor_id` varchar(255) DEFAULT NULL,
  `pasient_fk_person1` bigint(20) DEFAULT NULL,
  `slettet_flagg` int(11) NOT NULL,
  `syketilfelle_start_dato` date DEFAULT NULL,
  `sykm_id` varchar(255) DEFAULT NULL,
  `tiltak_arbeidsplassen_flagg` int(11) NOT NULL,
  `tiltak_nav_flagg` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `sykm_sykmelding_bidiagnose`;
CREATE TABLE `sykm_sykmelding_bidiagnose` (
  `id` int(11) NOT NULL,
  `diagnose_kode` varchar(255) DEFAULT NULL,
  `diagnose_system` varchar(255) DEFAULT NULL,
  `kildesystem` varchar(255) DEFAULT NULL,
  `lastet_dato` datetime DEFAULT NULL,
  `sykm_sykmelding_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrrle6p2tfv443onahl9m9slbi` (`sykm_sykmelding_id`),
  CONSTRAINT `FKrrle6p2tfv443onahl9m9slbi` FOREIGN KEY (`sykm_sykmelding_id`) REFERENCES `sykm_sykmelding` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `sykm_sykmelding_periode`;
CREATE TABLE `sykm_sykmelding_periode` (
  `id` int(11) NOT NULL,
  `avvent_flagg` int(11) NOT NULL,
  `gradering` int(11) NOT NULL,
  `kildesystem` varchar(255) DEFAULT NULL,
  `lastet_dato` datetime DEFAULT NULL,
  `sykmelding_fom` date DEFAULT NULL,
  `sykmelding_tom` date DEFAULT NULL,
  `sykm_sykmelding_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK75p5f55a8vv8r241uhiqxm0n3` (`sykm_sykmelding_id`),
  CONSTRAINT `FK75p5f55a8vv8r241uhiqxm0n3` FOREIGN KEY (`sykm_sykmelding_id`) REFERENCES `sykm_sykmelding` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- 2020-01-31 09:06:03

-- User priviliges
--
--CREATE USER 'user' IDENTIFIED BY 'password';
--
--GRANT USAGE ON *.* TO 'user'@'%' IDENTIFIED BY 'password';
--
--GRANT ALL PRIVILEGES ON dvh_sykm_konsument.hibernate_sequence TO 'user'@'%';
--GRANT ALL PRIVILEGES ON dvh_sykm_konsument.mk_ident TO 'user'@'%';
--GRANT ALL PRIVILEGES ON dvh_sykm_konsument.sykm_sykmelding TO 'user'@'%';
--GRANT ALL PRIVILEGES ON dvh_sykm_konsument.sykm_sykmelding_bidiagnose TO 'user'@'%';
--
--FLUSH PRIVILEGES;