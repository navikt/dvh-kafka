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
