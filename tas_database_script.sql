/*
SQLyog Community v12.09 (64 bit)
MySQL - 5.6.32 : Database - grib
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`grib` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `grib`;

/*Table structure for table `tb_extas_device_mst` */

DROP TABLE IF EXISTS `tb_extas_device_mst`;

CREATE TABLE `tb_extas_device_mst` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `device_id` varchar(100) DEFAULT NULL,
  `device_name` varchar(50) DEFAULT NULL,
  `service_id` varchar(50) DEFAULT NULL,
  `onem2mpath` varchar(500) DEFAULT NULL,
  `device_status` varchar(5000) DEFAULT NULL,
  `device_execute_flag` varchar(50) DEFAULT NULL,
  `base64` varchar(50) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1030 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

/*Data for the table `tb_extas_device_mst` */



/*Table structure for table `tb_extas_service_info` */

DROP TABLE IF EXISTS `tb_extas_service_info`;

CREATE TABLE `tb_extas_service_info` (
  `service_id` varchar(50) DEFAULT NULL,
  `service_name` varchar(50) DEFAULT NULL,
  `service_ip` varchar(50) DEFAULT NULL,
  `service_port` varchar(50) DEFAULT NULL,
  `sevice_path` varchar(50) DEFAULT NULL,
  `si_ip` varchar(50) DEFAULT NULL,
  `si_port` varchar(50) DEFAULT NULL,
  `si_path` varchar(50) DEFAULT NULL,
  `token` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tb_extas_service_info` */



/*Table structure for table `tb_smartthings` */

DROP TABLE IF EXISTS `tb_smartthings`;

CREATE TABLE `tb_smartthings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `device_id` varchar(50) DEFAULT NULL,
  `device_name` varchar(50) DEFAULT NULL,
  `response_value` varchar(50) DEFAULT NULL,
  `path_status` varchar(250) DEFAULT NULL,
  `path_command` varchar(250) DEFAULT NULL,
  `path_result` varchar(250) DEFAULT NULL,
  `path_polling` varchar(250) DEFAULT NULL,
  `token` varchar(250) DEFAULT NULL,
  `api_url` varchar(250) DEFAULT NULL,
  `response_date` varchar(50) DEFAULT NULL,
  `ae` varchar(250) DEFAULT NULL,
  `api_token` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=155 DEFAULT CHARSET=utf8;

/*Data for the table `tb_smartthings` */



/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
