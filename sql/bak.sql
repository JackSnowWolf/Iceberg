-- MySQL dump 10.13  Distrib 8.0.18, for macos10.14 (x86_64)
--
-- Host: localhost    Database: iceberg
-- ------------------------------------------------------
-- Server version	8.0.18

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bill`
--

DROP TABLE IF EXISTS `bill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bill` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `userid` int(11) DEFAULT NULL COMMENT '用户id',
  `money` float(10,2) DEFAULT NULL COMMENT '金额',
  `typeid` int(1) NOT NULL COMMENT '类型 1 收入 2 支出',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `paywayid` int(11) DEFAULT NULL COMMENT '支付方式',
  `receiveraccount` varchar(255) DEFAULT NULL,
  `time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '交易时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `userid` (`userid`) USING BTREE,
  KEY `type` (`typeid`) USING BTREE,
  KEY `payway` (`paywayid`) USING BTREE,
  CONSTRAINT `bill_ibfk_2` FOREIGN KEY (`typeid`) REFERENCES `type` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `bill_ibfk_3` FOREIGN KEY (`paywayid`) REFERENCES `payway` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=584 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bill`
--

LOCK TABLES `bill` WRITE;
/*!40000 ALTER TABLE `bill` DISABLE KEYS */;
INSERT INTO `bill` VALUES (7,'taxi fee',25,10.00,3,'Transport',1,'sb-7agtb3731178@personal.example.com','2020-03-24 15:39:08'),(8,'taxi fee',1,10.00,1,'Transport',1,NULL,'2020-03-24 15:39:08'),(9,'taxi fee',2,1000.00,1,'Hotel/Travel',1,NULL,'2020-03-24 15:39:08'),(10,'paper fee',1,1000.00,1,'Hotel/Travel',1,'gg@paypal.com','2020-03-24 15:39:08'),(32,'rent',25,1000.00,1,'Transport',2,NULL,'2019-01-09 15:39:08'),(33,'rent',25,1000.00,1,'Transport',2,NULL,'2019-01-20 15:39:08'),(47,'food',1,800.00,1,'Catering',4,NULL,'2019-01-19 15:39:08'),(48,'food',25,800.00,2,'Catering',4,NULL,'2019-01-11 15:39:08'),(49,'food',25,800.00,1,'Catering',4,NULL,'2019-01-11 15:39:08'),(65,'travel',25,800.00,1,'Transport',3,NULL,'2019-01-16 15:39:08'),(67,'asd',25,123.00,1,'Life',1,NULL,'2019-01-26 14:14:11'),(68,'aaa',25,123.00,1,'Transport',1,NULL,'2019-01-26 14:23:52'),(69,'aaabbb',25,999.00,1,'Life',1,'sb-7agtb3731178@personal.example.com','2019-01-26 14:24:19'),(70,'asda',1,233.00,1,'Life',1,NULL,'2019-01-26 14:28:52'),(71,'dadada',25,123.00,1,'Life',1,'sb-7agtb3731178@personal.example.com','2019-01-26 14:29:53'),(72,'adasda',25,123.00,1,'Transport',1,NULL,'2019-01-26 14:33:09'),(74,'hah',1,123.00,1,'Transport',2,NULL,'2019-01-26 14:36:18'),(90,'Hotel fee',21,12.00,0,'Hotel/Travel',1,'test@paylal.com','2020-04-10 20:42:56'),(91,'Hotel fee',8888,100.00,0,'Hotel/Travel',1,'sb-7agtb3731178@personal.example.com','2020-05-21 20:42:56'),(92,'Hotel fee',21,12.00,0,'Hotel/Travel',1,'test@paylal.com','2020-07-22 20:42:56'),(93,'Hotel fee',21,100.00,0,'Hotel/Travel',1,'test@paylal.com','2020-10-23 20:42:56'),(94,'Hotel fee',8888,12.00,0,'Hotel/Travel',1,'sb-7agtb3731178@personal.example.com','2020-10-24 20:42:56'),(95,'Hotel fee',21,100.00,0,'Hotel/Travel',1,'test@paylal.com','2020-10-25 20:42:56'),(96,'Hotel fee',21,12.00,0,'Hotel/Travel',1,'test@paylal.com','2020-10-26 20:42:56'),(97,'Hotel fee',8888,100.00,0,'Hotel/Travel',1,'sb-7agtb3731178@personal.example.com','2020-10-27 20:42:56'),(98,'Hotel fee',21,12.00,0,'Hotel/Travel',1,'test@paylal.com','2020-10-28 20:42:56'),(99,'Hotel fee',8888,100.00,0,'Hotel/Travel',1,'sb-7agtb3731178@personal.example.com','2020-10-29 20:42:56'),(100,'Hotel fee',21,12.00,0,'Hotel/Travel',1,'test@paylal.com','2020-09-30 20:42:56'),(189,'Water fee',8888,100.00,0,'Catering',1,'test@paylal.com','2020-06-09 20:42:56'),(190,'Water fee',21,12.00,0,'Catering',1,'test@paylal.com','2020-04-10 20:42:56'),(191,'Water fee',21,100.00,0,'Catering',1,'test@paylal.com','2020-05-11 20:42:56'),(192,'Water fee',21,12.00,0,'Catering',1,'test@paylal.com','2020-07-12 20:42:56'),(193,'Water fee',21,100.00,0,'Catering',1,'test@paylal.com','2020-09-13 20:42:56'),(194,'Water fee',21,12.00,0,'Catering',1,'sb-7agtb3731178@personal.example.com','2020-11-14 20:42:56'),(195,'Water fee',21,100.00,0,'Catering',1,'test@paylal.com','2020-11-15 20:42:56'),(196,'Water fee',21,12.00,0,'Catering',1,'sb-7agtb3731178@personal.example.com','2020-11-16 20:42:56'),(197,'Food fee',8888,100.00,0,'Catering',1,'test@paylal.com','2020-11-17 20:42:56'),(198,'Food',21,8888.00,0,'Catering',1,'test@paylal.com','2020-11-18 20:42:56'),(199,'Hotel fee',21,100.00,0,'Hotel/Travel',1,'test@paylal.com','2020-10-19 20:42:56'),(200,'Hotel fee',21,12.00,0,'Hotel/Travel',1,'sb-7agtb3731178@personal.example.com','2020-09-20 20:42:56'),(210,'Test Usage',1,100.00,3,'Catering',1,NULL,'2020-11-17 20:23:20'),(217,'taxi',8888,12.00,3,'Transport',0,'sb-7agtb3731178@personal.example.com','2020-11-18 01:01:24'),(218,'Test Usage: findByWhereNoPage',1,100.00,3,'Catering',1,NULL,'2020-11-18 01:24:57'),(220,'Test Usage',1,100.00,3,'Catering',1,NULL,'2020-11-18 01:24:57'),(221,'Test Usage',1,100.00,3,'Catering',1,'sb-7agtb3731178@personal.example.com','2020-11-18 01:24:57'),(222,'Test Usage: findByWhereNoPage',1,100.00,3,'Comms',1,'test@paypal.com','2020-11-18 10:06:04'),(224,'Test Usage',1,100.00,3,'Life',1,'test@paypal.com','2020-11-18 10:06:04'),(225,'Test Usage',1,100.00,3,'Life',1,NULL,'2020-11-18 10:06:04'),(226,'Test Usage: findByWhereNoPage',1,100.00,3,'Life',1,'test@paypal.com','2020-11-18 10:17:29'),(228,'Test Usage',1,100.00,3,'Health',1,'test@paypal.com','2020-11-18 10:17:29'),(229,'Test Usage',1,100.00,3,'Health',1,NULL,'2020-11-18 10:17:29'),(230,'Test Usage: findByWhereNoPage',1,100.00,3,'Health',1,'test@paypal.com','2020-11-18 10:49:54'),(232,'Test Usage',1,100.00,3,'Health',1,'test@paypal.com','2020-11-18 10:49:54'),(233,'Test Usage',1,100.00,3,'Comms',1,NULL,'2020-11-18 10:49:54'),(234,'Test Usage: findByWhereNoPage',1,100.00,3,'Comms',1,'test@paypal.com','2020-11-18 11:53:00'),(236,'Test Usage',1,100.00,3,'For test',1,'test@paypal.com','2020-11-18 11:53:00'),(237,'Test Usage',1,100.00,3,'Comms',1,NULL,'2020-11-18 11:53:00'),(238,'Test Usage: findByWhereNoPage',1,100.00,3,'Comms',1,'test@paypal.com','2020-11-18 11:56:48'),(240,'Test Usage',1,100.00,3,'For test',1,'test@paypal.com','2020-11-18 11:56:48'),(241,'Test Usage',1,100.00,3,'Comms',1,NULL,'2020-11-18 11:56:48'),(242,'Test Usage: findByWhereNoPage',1,100.00,3,'Comms',1,'test@paypal.com','2020-11-18 11:57:34'),(244,'Test Usage',1,100.00,3,'For test',1,'test@paypal.com','2020-11-18 11:57:34'),(245,'Test Usage',1,100.00,3,'Comms',1,NULL,'2020-11-18 11:57:34'),(246,'taxi fee',101,11.00,0,'Transport',0,'ee@paypal.com','2020-11-18 12:19:45'),(247,'Test Usage: findByWhereNoPage',1,100.00,3,'Comms',1,'test@paypal.com','2020-11-18 12:44:01'),(249,'Test Usage',1,100.00,3,'For test',1,'test@paypal.com','2020-11-18 12:44:01'),(250,'Test Usage',1,100.00,3,'Transport',1,NULL,'2020-11-18 12:44:01'),(251,'Test Usage: findByWhereNoPage',1,100.00,3,'Transport',1,'test@paypal.com','2020-11-19 11:37:59'),(253,'Test Usage',1,100.00,3,'Transport',1,'test@paypal.com','2020-11-19 11:37:59'),(254,'Test Usage',1,100.00,3,'Transport',1,NULL,'2020-11-19 11:37:59'),(255,'test11183',3,11.00,0,'Transport',0,'test11183@paypal.com','2020-11-19 20:38:58'),(256,'health fee',101,12.00,0,'Health',0,'test101@paypal.com','2020-11-19 20:40:35'),(257,'Test Usage: findByWhereNoPage',1,100.00,3,'For test:7d1386c4-8b43-4ca9-91a7-787348b2129b',1,'test@paypal.com','2020-11-19 20:42:56'),(259,'Test Usage',1,100.00,3,'For test',1,'test@paypal.com','2020-11-19 20:42:56'),(260,'Test Usage',1,100.00,3,'For test:8099571e-5357-4a91-8f5d-5f0a9b2a8216',1,NULL,'2020-11-19 20:42:56'),(261,'Taxi fee',24,100.00,0,'Transport',1,'test@paylal.com','2020-11-15 20:42:56'),(262,'Taxi fee',24,12.00,0,'Transport',1,'test@paylal.com','2020-11-12 20:42:56'),(263,'Taxi fee',24,100.00,0,'Transport',1,'test@paylal.com','2020-11-13 20:42:56'),(264,'Taxi fee',24,12.00,0,'Transport',1,'test@paylal.com','2020-11-14 20:42:56'),(265,'Taxi fee',24,100.00,0,'Transport',1,'test@paylal.com','2020-10-15 20:42:56'),(266,'Taxi fee',24,12.00,0,'Transport',1,'test@paylal.com','2020-11-12 20:42:56'),(267,'Taxi fee',24,100.00,0,'Transport',1,'test@paylal.com','2020-11-10 20:42:56'),(268,'Taxi fee',24,12.00,0,'Transport',1,'test@paylal.com','2020-02-12 20:42:56'),(269,'Taxi fee',24,100.00,0,'Transport',1,'test@paylal.com','2020-03-15 20:42:56'),(270,'Taxi fee',24,12.00,0,'Transport',1,'test@paylal.com','2020-04-12 20:42:56'),(271,'Taxi fee',24,100.00,0,'Transport',1,'test@paylal.com','2020-05-15 20:42:56'),(272,'Taxi fee',24,12.00,0,'Transport',1,'test@paylal.com','2020-07-12 20:42:56'),(273,'Taxi fee',24,100.00,0,'Transport',1,'test@paylal.com','2020-09-15 20:42:56'),(274,'Taxi fee',24,12.00,0,'Transport',1,'test@paylal.com','2020-11-02 20:42:56'),(275,'Taxi fee',24,100.00,0,'Transport',1,'test@paylal.com','2020-11-04 20:42:56'),(276,'Taxi fee',24,12.00,0,'Transport',1,'test@paylal.com','2020-11-05 20:42:56'),(277,'Taxi fee',24,100.00,0,'Transport',1,'test@paylal.com','2020-11-09 20:42:56'),(278,'Taxi fee',24,12.00,0,'Transport',1,'test@paylal.com','2020-11-08 20:42:56'),(279,'Taxi fee',24,100.00,0,'Transport',1,'test@paylal.com','2020-10-15 20:42:56'),(280,'Taxi fee',24,12.00,0,'Transport',1,'test@paylal.com','2020-09-12 20:42:56'),(281,'Taxi fee',24,100.00,0,'Transport',1,'test@paylal.com','2020-11-15 20:42:56'),(282,'Taxi fee',24,12.00,0,'Transport',1,'test@paylal.com','2020-11-12 20:42:56'),(283,'Hotel fee',21,100.00,0,'Hotel/Travel',1,'test@paylal.com','2020-09-13 20:42:56'),(284,'Hotel fee',21,12.00,0,'Hotel/Travel',1,'test@paylal.com','2020-01-14 20:42:56'),(285,'Hotel fee',21,100.00,0,'Hotel/Travel',1,'test@paylal.com','2020-02-15 20:42:56'),(286,'Hotel fee',21,12.00,0,'Hotel/Travel',1,'test@paylal.com','2020-03-12 20:42:56'),(287,'Hotel fee',21,100.00,0,'Hotel/Travel',1,'test@paylal.com','2020-04-10 20:42:56'),(288,'Hotel fee',21,12.00,0,'Hotel/Travel',1,'test@paylal.com','2020-05-12 20:42:56'),(289,'Hotel fee',21,100.00,0,'Hotel/Travel',1,'test@paylal.com','2020-06-15 20:42:56'),(290,'Hotel fee',21,12.00,0,'Hotel/Travel',1,'test@paylal.com','2020-04-02 20:42:56'),(291,'Hotel fee',21,100.00,0,'Hotel/Travel',1,'test@paylal.com','2020-05-15 20:42:56'),(292,'Hotel fee',21,12.00,0,'Hotel/Travel',1,'test@paylal.com','2020-07-12 20:42:56'),(293,'Hotel fee',21,100.00,0,'Hotel/Travel',1,'test@paylal.com','2020-09-15 20:42:56'),(294,'Hotel fee',21,12.00,0,'Hotel/Travel',1,'test@paylal.com','2020-11-02 20:42:56'),(295,'Hotel fee',21,100.00,0,'Hotel/Travel',1,'test@paylal.com','2020-11-04 20:42:56'),(296,'Hotel fee',21,12.00,0,'Hotel/Travel',1,'test@paylal.com','2020-11-05 20:42:56'),(297,'Hotel fee',21,100.00,0,'Hotel/Travel',1,'test@paylal.com','2020-11-09 20:42:56'),(298,'Hotel fee',21,12.00,0,'Hotel/Travel',1,'test@paylal.com','2020-11-08 20:42:56'),(299,'Hotel fee',21,100.00,0,'Hotel/Travel',1,'test@paylal.com','2020-10-15 20:42:56'),(300,'Hotel fee',21,12.00,0,'Hotel/Travel',1,'test@paylal.com','2020-09-12 20:42:56'),(301,'Hotel fee',21,100.00,0,'Hotel/Travel',1,'test@paylal.com','2020-09-15 20:42:56'),(302,'Hotel fee',21,12.00,0,'Hotel/Travel',1,'test@paylal.com','2020-08-12 20:42:56'),(401,'Hotel fee',21,100.00,0,'Hotel/Travel',1,'test@paylal.com','2020-11-01 20:42:56'),(402,'Hotel fee',21,12.00,0,'Hotel/Travel',1,'test@paylal.com','2020-08-02 20:42:56'),(484,'Hotel fee',21,12.00,0,'Hotel/Travel',1,'test@paylal.com','2020-01-04 20:42:56'),(485,'Water fee',8888,100.00,0,'Catering',1,'sb-7agtb3731178@personal.example.com','2020-02-05 20:42:56'),(486,'Water fee',8888,12.00,0,'Catering',1,'sb-7agtb3731178@personal.example.com','2020-03-06 20:42:56'),(487,'Water fee',8888,100.00,0,'Catering',1,'sb-7agtb3731178@personal.example.com','2020-04-07 20:42:56'),(488,'Water fee',8888,12.00,0,'Catering',1,'sb-7agtb3731178@personal.example.com','2020-05-08 20:42:56'),(501,'Hotel fee',21,100.00,0,'Hotel/Travel',1,'test@paylal.com','2020-11-01 20:42:56'),(502,'Hotel fee',21,12.00,0,'Hotel/Travel',1,'test@paylal.com','2020-08-02 20:42:56'),(583,'Hotel fee',21,100.00,0,'Hotel/Travel',1,'test@paylal.com','2020-09-03 20:42:56');
/*!40000 ALTER TABLE `bill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `group`
--

DROP TABLE IF EXISTS `group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `group` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ownerid` int(11) NOT NULL COMMENT '户主编号',
  `groupname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '家庭住址',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `holderid` (`ownerid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group`
--

LOCK TABLES `group` WRITE;
/*!40000 ALTER TABLE `group` DISABLE KEYS */;
INSERT INTO `group` VALUES (1,3,'Columbia University'),(2,23,NULL),(3,20,NULL),(4,21,NULL);
/*!40000 ALTER TABLE `group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payway`
--

DROP TABLE IF EXISTS `payway`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payway` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `payway` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `extra` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payway`
--

LOCK TABLES `payway` WRITE;
/*!40000 ALTER TABLE `payway` DISABLE KEYS */;
INSERT INTO `payway` VALUES (0,'Paypal',NULL),(1,'Paypal',NULL),(2,'Cash',NULL),(3,'Debit',NULL),(4,'Venmo',NULL),(5,'Others',NULL);
/*!40000 ALTER TABLE `payway` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `privilege`
--

DROP TABLE IF EXISTS `privilege`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `privilege` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `privilegeNumber` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '权限编号',
  `privilegeName` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '权限名称',
  `privilegeTipflag` char(4) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '菜单级别',
  `privilegeTypeflag` char(4) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '1启用 0禁用',
  `privilegeUrl` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '权限URL',
  `icon` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '图标',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `privilege`
--

LOCK TABLES `privilege` WRITE;
/*!40000 ALTER TABLE `privilege` DISABLE KEYS */;
INSERT INTO `privilege` VALUES (62,'001','All Request','0','1','','&#xe698;'),(63,'001001','Request Details','1','1','review_details','&#xe698;'),(64,'002','Approved Request','0','1',NULL,'&#xe702;'),(65,'002001','Approval Details','1','1','reimbursement_details','&#xe702;'),(66,'003','Statistical Report','0','1',NULL,'&#xe757;'),(67,'003001','Statistical Report','1','1','chart_line','&#xe757;'),(68,'004','Group Members Mgmt','0','1',NULL,'&#xe726;'),(69,'005','System Mgmt','0','1','','&#xe696;'),(70,'005001','User Mgmt','1','1','sys_users','&#xe6b8;'),(71,'005002','Role Mgmt','1','1','sys_roles','&#xe70b;'),(74,'004001','Group Members Info','1','1','sys_users','&#xe726;');
/*!40000 ALTER TABLE `privilege` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `roleid` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `rolename` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '角色名称',
  PRIMARY KEY (`roleid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=113 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'Administrator'),(2,'Group Manager'),(3,'Normal User'),(102,'testRole'),(103,'testRole'),(104,'testRole'),(105,'testRole'),(108,'testRole'),(109,'testRole'),(110,'testRole'),(111,'testRole'),(112,'testRole');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roleprivilieges`
--

DROP TABLE IF EXISTS `roleprivilieges`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roleprivilieges` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `roleID` int(11) DEFAULT NULL COMMENT '角色维护表主键',
  `privilegeID` int(11) DEFAULT NULL COMMENT '权限维护表主键',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `roleID` (`roleID`) USING BTREE,
  KEY `privilegeID` (`privilegeID`) USING BTREE,
  CONSTRAINT `roleprivilieges_ibfk_1` FOREIGN KEY (`roleID`) REFERENCES `role` (`roleid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `roleprivilieges_ibfk_2` FOREIGN KEY (`privilegeID`) REFERENCES `privilege` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=1567 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roleprivilieges`
--

LOCK TABLES `roleprivilieges` WRITE;
/*!40000 ALTER TABLE `roleprivilieges` DISABLE KEYS */;
INSERT INTO `roleprivilieges` VALUES (841,2,62),(842,2,63),(843,2,64),(844,2,65),(845,2,66),(846,2,67),(851,2,68),(852,2,74),(879,8,62),(880,8,63),(881,8,64),(882,8,65),(883,8,66),(884,8,67),(885,9,62),(886,9,63),(887,9,64),(888,9,65),(889,9,66),(890,9,67),(927,1,62),(928,1,63),(929,1,64),(930,1,65),(931,1,66),(932,1,67),(933,1,70),(934,1,71),(935,1,74),(936,1,69),(1549,3,62),(1550,3,63),(1551,3,64),(1552,3,65),(1553,3,66),(1554,3,67);
/*!40000 ALTER TABLE `roleprivilieges` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `type`
--

DROP TABLE IF EXISTS `type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `type` (
  `id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `type`
--

LOCK TABLES `type` WRITE;
/*!40000 ALTER TABLE `type` DISABLE KEYS */;
INSERT INTO `type` VALUES (0,'PROCESSING'),(1,'MISSING_INFO'),(2,'DENIED'),(3,'APPROVED');
/*!40000 ALTER TABLE `type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '账号',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '密码',
  `realname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '真实姓名',
  `roleid` int(1) NOT NULL DEFAULT '3' COMMENT '角色编号',
  `groupid` int(11) DEFAULT NULL COMMENT '所属家庭编号',
  `photo` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户头像',
  `oauth_provider` varchar(15) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `houseid` (`groupid`) USING BTREE,
  KEY `roleid` (`roleid`) USING BTREE,
  CONSTRAINT `user_ibfk_2` FOREIGN KEY (`groupid`) REFERENCES `group` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `user_ibfk_3` FOREIGN KEY (`roleid`) REFERENCES `role` (`roleid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=45752184 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'hwj','hwj','hwj',1,2,NULL,NULL,'hwj97055@gmail.com'),(3,'test11182','123456','Wenjie Chen',2,1,NULL,NULL,NULL),(20,'test1118','123456','test1118',2,3,NULL,NULL,NULL),(21,'house2','123456','house2',2,4,NULL,NULL,NULL),(23,'1118test','123456','1118test',2,2,NULL,NULL,NULL),(24,'hello','world','hello',3,3,NULL,NULL,NULL),(25,'Jack','123','cwj',1,1,NULL,NULL,'229054463@qq.com'),(101,'test101','test101','test101',3,1,NULL,NULL,NULL),(8888,'czy','123456','testuser',3,4,NULL,NULL,'1234@qq.com'),(9001,'teste7de6450-8654-4f1b-8eb5-c3b6bed6f76c','123456',NULL,3,1,NULL,NULL,NULL),(9002,'testd8be0259-34c6-458d-8a3b-776e0deea72c','123456',NULL,3,3,NULL,NULL,NULL),(9003,'1117test2','123456','1117test2',3,3,NULL,NULL,'ee'),(9004,'test1116','test1116',NULL,3,1,NULL,NULL,NULL),(9005,'test11162','test11162',NULL,3,2,NULL,NULL,NULL),(9006,'test11163','test11163',NULL,3,3,NULL,NULL,NULL),(9007,'test11164','test11164',NULL,3,4,NULL,NULL,NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-11-20 11:22:48
