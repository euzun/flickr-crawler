/*
SQLyog Ultimate v8.3 
MySQL - 5.6.17 : Database - flickr
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`flickr` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `flickr`;

/*Table structure for table `bbox` */

DROP TABLE IF EXISTS `bbox`;

CREATE TABLE `bbox` (
  `bbox_id` int(16) NOT NULL AUTO_INCREMENT,
  `min_long` double NOT NULL DEFAULT '0',
  `min_lat` double NOT NULL DEFAULT '0',
  `max_long` double NOT NULL DEFAULT '0',
  `max_lat` double NOT NULL DEFAULT '0',
  `is_done` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`bbox_id`),
  UNIQUE KEY `unique_index` (`min_long`,`min_lat`,`max_long`,`max_lat`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `bbox` */

/*Table structure for table `photo` */

DROP TABLE IF EXISTS `photo`;

CREATE TABLE `photo` (
  `user_id` varchar(255) NOT NULL,
  `photo_id` varchar(255) NOT NULL,
  `flickr_make` varchar(1000) NOT NULL,
  `flickr_model` varchar(1000) NOT NULL,
  `flickr_software` varchar(1000) DEFAULT NULL,
  `img_width` int(10) NOT NULL,
  `img_height` int(10) NOT NULL,
  `exposure_time` varchar(255) NOT NULL,
  `exposure_bias` varchar(255) NOT NULL,
  `exposure_mode` varchar(255) DEFAULT NULL,
  `iso_speed` varchar(255) NOT NULL,
  `img_url` varchar(1000) NOT NULL,
  `signature_key` int(10) DEFAULT NULL,
  `is_downloaded` tinyint(1) NOT NULL DEFAULT '0',
  `is_valid` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`photo_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `photo` */

/*Table structure for table `signature` */

DROP TABLE IF EXISTS `signature`;

CREATE TABLE `signature` (
  `signature_key` int(10) NOT NULL AUTO_INCREMENT,
  `signature_code` varchar(1000) DEFAULT NULL,
  `make` varchar(1000) DEFAULT NULL,
  `model` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`signature_key`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `signature` */

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `bbox_id` int(16) NOT NULL,
  `user_id` varchar(255) NOT NULL,
  `user_name` varchar(1000) DEFAULT NULL,
  `is_done` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`user_id`),
  KEY `NewIndex1` (`bbox_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `user` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
