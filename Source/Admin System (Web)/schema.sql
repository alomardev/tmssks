DROP DATABASE IF EXISTS `tmssks_db`;
CREATE DATABASE IF NOT EXISTS `tmssks_db` CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `tmssks_db`;

CREATE TABLE `users` (
	`id` INTEGER PRIMARY KEY AUTO_INCREMENT,
	`name` VARCHAR(255) NOT NULL,
	`username` VARCHAR(255) NOT NULL UNIQUE,
	`password` VARCHAR(255) NOT NULL,
	`email` VARCHAR(255) UNIQUE,
	`nid` VARCHAR(255),
	`role` TINYINT NOT NULL
);

CREATE TABLE `phones` (
	`user_id` INTEGER,
	`phone` VARCHAR(255),
	PRIMARY KEY(`user_id`, `phone`)
);

CREATE TABLE `kids` (
	`nid` VARCHAR(255) PRIMARY KEY,
	`key` VARCHAR(255) NOT NULL,
	`name` VARCHAR(255) NOT NULL,
	`level` VARCHAR(20),
	`address_title` VARCHAR(255),
	`address_latitude` DOUBLE,
	`address_longitude` DOUBLE,
	`school_id` INTEGER NOT NULL,
	`trans_id` INTEGER
);

CREATE TABLE `parents` (
	`kid_id` VARCHAR(255),
	`user_id` INTEGER,
	PRIMARY KEY(`kid_id`, `user_id`)
);

CREATE TABLE `schools` (
	`id` INTEGER PRIMARY KEY AUTO_INCREMENT,
	`name` VARCHAR(255) NOT NULL,
	`address` TEXT,
	`admin_id` INTEGER,
	`device_id` INTEGER UNIQUE
);

CREATE TABLE `trans` (
	`id` INTEGER PRIMARY KEY AUTO_INCREMENT,
	`num_plate` VARCHAR(255) NOT NULL,
	`driver_name` VARCHAR(255) NOT NULL,
	`driver_phone` VARCHAR(255),
	`device_id` INTEGER UNIQUE
);

CREATE TABLE `devices` (
	`id` INTEGER PRIMARY KEY,
	`type` TINYINT NOT NULL
);

CREATE TABLE `readings` (
	`id` INTEGER PRIMARY KEY AUTO_INCREMENT,
	`device_id` INTEGER NOT NULL,
	`value` VARCHAR(255) NOT NULL,
	`time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `sessions` (
	`id` INTEGER PRIMARY KEY AUTO_INCREMENT,
	`date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`device_id` INTEGER NOT NULL
);

CREATE TABLE `sessionKids` (
	`kid_id` VARCHAR(255),
	`session_id` INTEGER,
	PRIMARY KEY(`kid_id`, `session_id`)
);

CREATE TABLE `records` (
	`id` INTEGER PRIMARY KEY AUTO_INCREMENT,
	`time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`latitude` DOUBLE NOT NULL,
	`longitude` DOUBLE NOT NULL,
	`session_id` INTEGER NOT NULL
);

CREATE TABLE `messages` (
	`id` INTEGER PRIMARY KEY AUTO_INCREMENT,
	`subject` VARCHAR(255) NOT NULL,
	`body` TEXT NOT NULL,
	`date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`from_id` INTEGER NOT NULL,
	`to_id` INTEGER NOT NULL,
	`is_read` TINYINT(1) DEFAULT 0 NOT NULL
);
