/*
SQLyog Ultimate v12.2.6 (64 bit)
MySQL - 5.7.23-log : Database - test
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
USE `test`;

/*Table structure for table `t_doctor_info` */

DROP TABLE IF EXISTS `t_doctor_info`;

CREATE TABLE `t_doctor_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `doctor_id` varchar(20) DEFAULT NULL,
  `doctor_name` varchar(32) DEFAULT NULL,
  `phone` varchar(16) DEFAULT NULL,
  `sex` int(11) DEFAULT NULL,
  `doc_no` varchar(16) DEFAULT NULL,
  `image` varchar(128) DEFAULT NULL,
  `expert` varchar(256) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `level_code` int(11) DEFAULT NULL,
  `dep_id` int(11) DEFAULT NULL,
  `dep_name` varchar(32) DEFAULT NULL,
  `total_yuyue` int(11) DEFAULT NULL,
  `good_thks` int(11) DEFAULT NULL,
  `zc_name` varchar(16) DEFAULT NULL,
  `link_id` int(11) DEFAULT NULL,
  `zc_id` int(11) DEFAULT NULL,
  `is_expert` int(11) DEFAULT NULL,
  `is_recommend` int(11) DEFAULT NULL,
  `is_yuyue` int(11) DEFAULT NULL,
  `is_guahao` int(11) DEFAULT NULL,
  `ask` int(11) DEFAULT NULL,
  `sch` int(11) DEFAULT NULL,
  `vip` int(11) DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_subscribe_info` */

DROP TABLE IF EXISTS `t_subscribe_info`;

CREATE TABLE `t_subscribe_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `unit_id` varchar(10) DEFAULT NULL,
  `state` varchar(10) DEFAULT NULL,
  `doctor_id` varchar(16) DEFAULT NULL,
  `doctor_name` varchar(32) DEFAULT NULL,
  `to_date` varchar(32) DEFAULT NULL,
  `yuyue_max` varchar(10) DEFAULT NULL,
  `yuyue_num` varchar(10) DEFAULT NULL,
  `youzhi_max` varchar(10) DEFAULT NULL,
  `youzhi_num` varchar(10) DEFAULT NULL,
  `left_num` varchar(10) DEFAULT NULL,
  `time_type` varchar(10) DEFAULT NULL,
  `time_type_desc` varchar(10) DEFAULT NULL,
  `y_state` varchar(10) DEFAULT NULL,
  `y_state_desc` varchar(10) DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
