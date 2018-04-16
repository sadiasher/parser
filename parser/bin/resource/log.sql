
CREATE DATABASE IF NOT EXISTS `db_log`;

use `db_log`;


CREATE TABLE IF NOT EXISTS `db_log`.`log` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `date` DATETIME NOT NULL,
  `ip` VARCHAR(45) NOT NULL,
  `request` VARCHAR(45) NOT NULL,
  `status` VARCHAR(45) NOT NULL,
  `user_agent` VARCHAR(200) NOT NULL,
  PRIMARY KEY (`id`));


CREATE TABLE IF NOT EXISTS `db_log`.`request_detail` (
  `id` INT NOT NULL  AUTO_INCREMENT ,
  `ip` VARCHAR(45) NOT NULL,
  `start_date` DATETIME NOT NULL,
  `duration` VARCHAR(45) NOT NULL,
  `threshold` VARCHAR(45) NOT NULL,
  `request_count` INT NOT NULL,
  `comments` VARCHAR(200) NOT NULL,
  PRIMARY KEY (`id`));


CREATE TABLE IF NOT EXISTS `db_log`.`file` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(200) NOT NULL,
  `uploaded_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`));


