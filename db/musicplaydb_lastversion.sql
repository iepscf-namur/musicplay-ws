-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost:3306
-- Généré le :  sam. 06 oct. 2018 à 08:26
-- Version du serveur :  5.7.19
-- Version de PHP :  7.1.20

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `musicplaydb`
--
DROP DATABASE IF EXISTS musicplaydb;
CREATE DATABASE IF NOT EXISTS `musicplaydb` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `musicplaydb`;

-- --------------------------------------------------------

--
-- Structure de la table `author`
--

CREATE TABLE IF NOT EXISTS `author` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(400) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `ligne`
--

CREATE TABLE IF NOT EXISTS `ligne` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `accord` varchar(70) NOT NULL,
  `text` varchar(70) NOT NULL,
  `position` INT(10) NOT NULL,
  `fkIdStrophe` int(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FkLigneStrophe` (`fkIdStrophe`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `partitonmusicplay`
--

CREATE TABLE IF NOT EXISTS `partitonmusicplay` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(400) NOT NULL,
  `urlImage` text NOT NULL,
  `userValidation` int(1) NOT NULL DEFAULT '0',
  `moderatorValidation` int(1) NOT NULL DEFAULT '0',
  `creatorFKIdUser` int(10) UNSIGNED NOT NULL,
  `authorFkIdAuthor` int(10) UNSIGNED NOT NULL,
  `creationDate` datetime NOT NULL,
  `modificationDate` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FkPartitionMusicPlayAuthor` (`authorFkIdAuthor`),
  KEY `FkPartitionMusicPlayUser` (`creatorFKIdUser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `role`
--

CREATE TABLE IF NOT EXISTS `role` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `strophe`
--

CREATE TABLE IF NOT EXISTS `strophe` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `fkIdPartition` int(10) UNSIGNED NOT NULL,
  `position` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FkStrophePartitionMusicPlay` (`fkIdPartition`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `user`
--
/*
CREATE TABLE IF NOT EXISTS `user` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `login` varchar(20) NOT NULL,
  `password` varchar(250) NOT NULL,
  `salt` varchar(75) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
*/

CREATE TABLE user (
	id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
	login VARCHAR(32) NOT NULL UNIQUE,
	salt BINARY(20) NOT NULL,
	password BINARY(20) NOT NULL
) ENGINE = INNODB;

DELIMITER //
DROP PROCEDURE IF EXISTS user_auth //
CREATE PROCEDURE user_auth(IN login CHAR(32), IN pass CHAR(64), OUT idOut INT)
BEGIN
	SELECT user.id, user.login, user.salt INTO @id, @login, @salt FROM user WHERE user.login = login;
	IF (SELECT COUNT(user.id) FROM user WHERE user.login = login AND user.password = UNHEX(SHA1(CONCAT(HEX(@salt), pass)))) != 1 THEN
		SET @message_text = CONCAT('Login incorrect for user \'', @login, '\'');
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = @message_text;
	ELSE
		SET idOut = @id ;
		SELECT idOut ;
	END IF;
END//

DROP PROCEDURE  IF EXISTS user_create //
CREATE PROCEDURE user_create(IN login CHAR(32), IN pass CHAR(64), OUT idOut INT)
BEGIN
	IF (SELECT COUNT(user.id) FROM user WHERE user.login = login) > 0 THEN
		SET @message_text = CONCAT('User \'', login, '\' already exists');
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = @message_text;
	ELSE
		SET @salt = UNHEX(SHA1(CONCAT(RAND(), RAND(), RAND())));
		INSERT INTO user(login, salt, password) VALUES (login, @salt, UNHEX(SHA1(CONCAT(HEX(@salt), pass))));
	END IF;
	SET idOut = LAST_INSERT_ID();
	Select idOut ;
END//

DELIMITER ;

-- --------------------------------------------------------

--
-- Structure de la table `user-role`
--

CREATE TABLE IF NOT EXISTS `user-role` (
  `fkIdUser` int(10) UNSIGNED NOT NULL,
  `fkIdRole` int(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`fkIdUser`,`fkIdRole`),
  KEY `fkUser-Role-Role` (`fkIdRole`),
  KEY `fkUser-Role-User` (`fkIdUser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `ligne`
--
ALTER TABLE `ligne`
  ADD CONSTRAINT `FkLigneStrophe` FOREIGN KEY (`fkIdStrophe`) REFERENCES `strophe` (`id`);

--
-- Contraintes pour la table `partitonmusicplay`
--
ALTER TABLE `partitonmusicplay`
  ADD CONSTRAINT `FkPartitionMusicPlayAuthor` FOREIGN KEY (`authorFkIdAuthor`) REFERENCES `author` (`id`),
  ADD CONSTRAINT `FkPartitionMusicPlayUser` FOREIGN KEY (`creatorFKIdUser`) REFERENCES `user` (`id`);

--
-- Contraintes pour la table `strophe`
--
ALTER TABLE `strophe`
  ADD CONSTRAINT `FkStrophePartitionMusicPlay` FOREIGN KEY (`fkIdPartition`) REFERENCES `partitonmusicplay` (`id`);

--
-- Contraintes pour la table `user-role`
--
ALTER TABLE `user-role`
  ADD CONSTRAINT `fkUser-Role-Role` FOREIGN KEY (`fkIdRole`) REFERENCES `role` (`id`),
  ADD CONSTRAINT `fkUser-Role-User` FOREIGN KEY (`fkIdUser`) REFERENCES `user` (`id`);

--
-- ajout callable statement
--




--
-- Ajout data
--
INSERT INTO role (name) VALUES ('member'), ('moderator'), ('administrator');

COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;


