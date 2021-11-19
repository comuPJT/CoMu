-- MariaDB dump 10.19  Distrib 10.6.4-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: comu
-- ------------------------------------------------------
-- Server version	10.6.4-MariaDB-1:10.6.4+maria~focal

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `comu`
--

/*!40000 DROP DATABASE IF EXISTS `comu`*/;

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `comu` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `comu`;

--
-- Table structure for table `history`
--

DROP TABLE IF EXISTS `history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `contents` varchar(1000) DEFAULT NULL,
  `timestamp` datetime DEFAULT current_timestamp(),
  `room_id` bigint(20) DEFAULT NULL,
  `user_seq` bigint(20) DEFAULT NULL,
  `music_id` bigint(20) DEFAULT NULL,
  `likes` bigint(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `history_music_idx` (`music_id`),
  KEY `history_applicant_idx` (`user_seq`),
  KEY `history_room_idx` (`room_id`),
  CONSTRAINT `history_applicant` FOREIGN KEY (`user_seq`) REFERENCES `user` (`user_seq`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `history_music` FOREIGN KEY (`music_id`) REFERENCES `music` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `history_room` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `history`
--

LOCK TABLES `history` WRITE;
/*!40000 ALTER TABLE `history` DISABLE KEYS */;
/*!40000 ALTER TABLE `history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `music`
--

DROP TABLE IF EXISTS `music`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `music` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `spotify_id` varchar(50) DEFAULT NULL,
  `thumbnail` varchar(100) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `singer` varchar(100) DEFAULT NULL,
  `source` varchar(100) DEFAULT NULL,
  `album` varchar(200) DEFAULT NULL,
  `on_cloud` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `music`
--

LOCK TABLES `music` WRITE;
/*!40000 ALTER TABLE `music` DISABLE KEYS */;
INSERT INTO `music` VALUES (1,'0','https://i.scdn.co/image/ab67616d0000b273cb81eb3c1238c60f2bbfd3b5','Fly me to the moon','Frank Sinatra','https://youtu.be/CFlMy48ui9s','Nothing But The Best (Remastered)',1),(2,'1','https://i.scdn.co/image/ab67616d0000b2730e27181dac939d599730bed0','Reality','Richard Sanderson','https://www.youtube.com/watch?v=arg-TuFxmq0','La boum (Bande originale du film de Claude Pinoteau)',1),(3,'2','https://i.scdn.co/image/ab67616d00001e02f96cefb0197694ad440c3314','Can\'t Help Falling In Love','Elvis Presley','https://www.youtube.com/watch?v=jk6trADQHY4','Love Me Tender',1),(4,'3','https://i.scdn.co/image/ab67616d00001e02dafd1cd6e9537ec8463ea691','Sunny','Boney M. ','https://www.youtube.com/watch?v=ghGiv7YLC7Q','Sunny',1),(5,'4','https://i.scdn.co/image/ab67616d00001e020085dd4362653ef4c54ebbeb','Vincent','Don McLean','https://www.youtube.com/watch?v=bk-82ebJyZ0','American Pie',1),(6,'5','https://i.scdn.co/image/ab67616d00001e020085dd4362653ef4c54ebbeb','Moon River','Andy Williams ','https://www.youtube.com/watch?v=L_jgIezosVA','Moon River And Other Great Movie Themes',1),(7,'6','https://i.scdn.co/image/ab67616d00001e02c75e9332256738669a2e3628','1 and 1/2','twotwo','https://youtu.be/NokWzy3USWo','일과 이분의 일',1),(8,'7','https://i.scdn.co/image/ab67616d00001e0299245de8bd696b9495af5d1c','상상속의 너','노이즈','https://youtu.be/be8KS6v3g6c','Noise 3rd Revolution',1),(9,'8','https://i.scdn.co/image/ab67616d00001e02b7a43a79a5e462807c888921','칵테일 사랑','마로니에(권인하/신윤미)','https://youtu.be/sp5ujZzMRZ8','칵테일 사랑',1),(10,'9','https://i.scdn.co/image/ab67616d00001e024422aac0b636fc07c0f860c8','Checklist','MAX','https://www.youtube.com/watch?v=g5WaJyEPP5g','Checklist (Feat. Chromeo)',1),(11,'10','https://i.scdn.co/image/ab67616d00001e02200f527dd4459a53436383c8','Younger','Jonas Blue, HRVY','https://www.youtube.com/watch?v=WGB0UpLNK8I','Younger',1),(12,'11','https://i.scdn.co/image/ab67616d00001e02e419ccba0baa8bd3f3d7abf2','Uptown Funk','Mark Ronson','https://www.youtube.com/watch?v=OPf0YbXqDm0)','Uptown Funk',1),(13,'12','https://i.scdn.co/image/ab67616d00001e02ca0d8cf6d25fea47c4d0a100','cocoa','aimless','https://www.youtube.com/watch?v=M2BMSh2vP0s','Ikigai',1),(14,'13','https://i.scdn.co/image/ab67616d00001e0218ffd6edc9a59a7234d0347b','Still Woozy','Lucy(ft. Odie)','https://www.youtube.com/watch?v=Un5BfXOWiFo','Lucy',1),(15,'14','https://i.scdn.co/image/ab67616d00001e0290189bfc9a5d39e0d88e4103','I Love You 3000','Stephanie Poetri','https://www.youtube.com/watch?v=cPkE0IbDVs4','I Love You 3000',1),(16,'15','https://i.scdn.co/image/ab67616d00001e023bd78d11bb721874ba0818ff','Claire de Lune','Debussy','https://www.youtube.com/watch?v=zpIgoy3Q1OE','Classical Chillout Lounge',1),(17,'16','https://i.scdn.co/image/ab67616d00001e02ab71994a68ab6a92275e6668','Libestraume','Liszt','https://www.youtube.com/watch?v=woiGoHKyqVo','봄날의 휴식 같은 음악 Vol. 9',1),(18,'17','https://i.scdn.co/image/ab67616d00001e029c7ada27ec0de1e5d43943ce','Lost Stars(Piano Cover)','Adam Levine','https://www.youtube.com/watch?v=ENvI05RfCmM','Begin Again - Music From And Inspired By The Original Motion Picture (Streaming Ver.)',1),(19,'2FWquqPNxte8iqZ3ATQG0p','https://i.scdn.co/image/ab67616d00001e0282ecc5ea89bf34479a71a297','All I Wanna Do (K) (Feat. Hoody & Loco)','박재범, Hoody, 로꼬','https://www.youtube.com/watch?v=lT7Q93fy1us','EVERYTHING YOU WANTED',0),(20,'71WZ7yFuwxmQz5jJUpvkGv','https://i.scdn.co/image/ab67616d00001e02ec55d2dad1aefc7d57745633','예뻤어 You Were Beautiful','DAY6','https://www.youtube.com/watch?v=QMgEsc4hbpE','SUNRISE',0),(21,'5WitNasXEIRptoLIQUcXMx','https://i.scdn.co/image/ab67616d00001e02733b6abfa76a28b999feb572','Décalcomanie','마마무','https://www.youtube.com/watch?v=wK1i-ckkYAQ','MEMORY',0),(22,'5GZK31rZaYfZuw6ALDVEDt','https://i.scdn.co/image/ab67616d00001e02c87dfc6c6dcd7e6290b893ca','Peppermint Chocolate (inst)','케이윌, 마마무','https://www.youtube.com/watch?v=U8zhLjll3fA','Peppermint Chocolate',0),(23,'3ysqqOVRD76lKS22FwXAnr','https://i.scdn.co/image/ab67616d00001e02a1a3a82cd4927d05cfd1c568','Feel Like Falling in Love','멜로망스','https://www.youtube.com/watch?v=v3OzE2w64X4','Because This Is My First Life (Original Television Soundtrack)',0),(24,'3uA8SjMyDtwtt0jLPMQbVD','https://i.scdn.co/image/ab67616d00001e0218974569625e8449a5497ef3','D (Half Moon)','DEAN, 개코','https://www.youtube.com/watch?v=RBJVGbJLYjI','130 Mood : TRBL',0),(25,'1hOEq5q9L41E2YbLhVvW5x','https://i.scdn.co/image/ab67616d00001e026fa3ae4086a3822771daec6d','Aloha','조정석','https://www.youtube.com/watch?v=LIhFZ3zmRKg','HOSPITAL PLAYLIST (Original Television Soundtrack), Pt. 3',0);
/*!40000 ALTER TABLE `music` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `my_main_playlist`
--

DROP TABLE IF EXISTS `my_main_playlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `my_main_playlist` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `music_id` bigint(20) NOT NULL,
  `play_order` int(11) DEFAULT NULL,
  `user_seq` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_seq_idx` (`user_seq`),
  KEY `music_id_idx` (`music_id`),
  CONSTRAINT `my_main_playlist_music` FOREIGN KEY (`music_id`) REFERENCES `music` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `my_main_playlist_user` FOREIGN KEY (`user_seq`) REFERENCES `user` (`user_seq`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `my_main_playlist`
--

LOCK TABLES `my_main_playlist` WRITE;
/*!40000 ALTER TABLE `my_main_playlist` DISABLE KEYS */;
INSERT INTO `my_main_playlist` VALUES (2,4,0,3),(3,2,1,3),(5,5,1,2),(6,6,2,2),(7,7,3,2),(8,3,2,3),(9,19,0,12),(10,19,1,12),(11,20,2,12),(12,21,3,12),(13,22,4,12),(14,23,5,12),(15,24,6,12);
/*!40000 ALTER TABLE `my_main_playlist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `myplaylist`
--

DROP TABLE IF EXISTS `myplaylist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `myplaylist` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) DEFAULT NULL,
  `created_at` datetime DEFAULT current_timestamp(),
  `user_seq` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_seq_idx` (`user_seq`),
  CONSTRAINT `myplaylist_user` FOREIGN KEY (`user_seq`) REFERENCES `user` (`user_seq`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `myplaylist`
--

LOCK TABLES `myplaylist` WRITE;
/*!40000 ALTER TABLE `myplaylist` DISABLE KEYS */;
INSERT INTO `myplaylist` VALUES (1,'hi','2021-11-17 08:26:14',2),(4,'홈트할  때 듣는 플리','2021-11-19 00:05:29',12),(5,'내가 좋아하는 노래로만','2021-11-19 00:09:08',12),(6,'얼른 금요일 됐으면','2021-11-19 00:09:50',12),(7,'과제 하면서 듣는 플리','2021-11-19 00:10:37',12),(8,'박재범 사랑해','2021-11-19 00:10:58',12),(9,'잔잔한 노래로만','2021-11-19 00:11:52',12),(10,'나의 최애 드라마 ost 모음집','2021-11-19 00:12:34',12),(11,'옛날 노래 모음집','2021-11-19 00:14:30',12),(12,'개발할 때 들으면 집중 더 잘되는 음악들','2021-11-19 00:15:04',12),(13,'친구한테 추천 받은 노래들','2021-11-19 00:15:55',12);
/*!40000 ALTER TABLE `myplaylist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `myplaylist_music`
--

DROP TABLE IF EXISTS `myplaylist_music`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `myplaylist_music` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `myplaylist_id` bigint(20) DEFAULT NULL,
  `music_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `myplaylist_id_idx` (`myplaylist_id`),
  KEY `music_id_idx` (`music_id`),
  CONSTRAINT `myplaylist_music` FOREIGN KEY (`music_id`) REFERENCES `music` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `myplaylist_no` FOREIGN KEY (`myplaylist_id`) REFERENCES `myplaylist` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `myplaylist_music`
--

LOCK TABLES `myplaylist_music` WRITE;
/*!40000 ALTER TABLE `myplaylist_music` DISABLE KEYS */;
INSERT INTO `myplaylist_music` VALUES (1,12,1),(2,12,2),(3,12,3),(4,12,4),(5,12,5);
/*!40000 ALTER TABLE `myplaylist_music` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `playlist_like`
--

DROP TABLE IF EXISTS `playlist_like`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `playlist_like` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `history_id` bigint(20) NOT NULL,
  `user_seq` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_seq_idx` (`user_seq`),
  KEY `like_history_idx` (`history_id`),
  CONSTRAINT `like_history` FOREIGN KEY (`history_id`) REFERENCES `history` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `user_like` FOREIGN KEY (`user_seq`) REFERENCES `user` (`user_seq`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `playlist_like`
--

LOCK TABLES `playlist_like` WRITE;
/*!40000 ALTER TABLE `playlist_like` DISABLE KEYS */;
/*!40000 ALTER TABLE `playlist_like` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `room` (
  `id` bigint(20) NOT NULL,
  `room_theme` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
/*!40000 ALTER TABLE `room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `user_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `email` varchar(512) DEFAULT NULL,
  `email_verified_yn` varchar(1) DEFAULT NULL,
  `first_visit_yn` varchar(1) DEFAULT 'Y',
  `modified_at` datetime(6) NOT NULL,
  `password` varchar(128) NOT NULL,
  `provider_type` varchar(20) NOT NULL,
  `role_type` varchar(20) NOT NULL,
  `user_id` varchar(64) DEFAULT NULL,
  `username` varchar(100) DEFAULT NULL,
  `character_num` int(11) DEFAULT 0,
  PRIMARY KEY (`user_seq`),
  UNIQUE KEY `UK_ob8kqyqqgmefl0aco34akdtpe` (`email`),
  UNIQUE KEY `UK_a3imlf41l37utmxiquukk8ajc` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (2,'2021-11-17 08:23:55.000000','comelynike@gmail.com','Y','N','2021-11-17 08:23:55.000000','NO_PASS','GOOGLE','USER','117530665892750766708','안녕 나는 지수',6),(3,'2021-11-17 08:34:59.000000','aoffltk88@gmail.com','Y','N','2021-11-17 08:34:59.000000','NO_PASS','GOOGLE','USER','103866708591396004823','우현우현',6),(4,'2021-11-17 17:36:09.000000','wonji616@gmail.com','Y','N','2021-11-17 17:36:09.000000','NO_PASS','GOOGLE','USER','106696491925243070153','원쥐',3),(5,'2021-11-17 17:41:11.000000',NULL,'Y','N','2021-11-17 17:41:11.000000','NO_PASS','KAKAO','USER','1995588490','헤이~',0),(6,'2021-11-17 08:54:15.000000',NULL,'Y','Y','2021-11-17 08:54:15.000000','NO_PASS','KAKAO','USER','1995070134',NULL,0),(7,'2021-11-17 08:54:32.000000',NULL,'Y','Y','2021-11-17 08:54:32.000000','NO_PASS','KAKAO','USER','1993368718',NULL,10),(8,'2021-11-17 09:04:35.000000',NULL,'Y','Y','2021-11-17 09:04:35.000000','NO_PASS','KAKAO','USER','1995622843',NULL,0),(9,'2021-11-17 09:36:37.000000','jihyunee95@gmail.com','Y','N','2021-11-17 09:36:37.000000','NO_PASS','GOOGLE','USER','108223654202209917027','WOW',0),(10,'2021-11-17 13:20:19.000000','qer7391@gmail.com','Y','N','2021-11-17 13:20:19.000000','NO_PASS','GOOGLE','USER','113372064892290193734','원웨이',2),(11,'2021-11-18 16:44:13.000000','aoffltk951@gmail.com','Y','N','2021-11-18 16:44:13.000000','NO_PASS','GOOGLE','USER','105470650711731543662','우현부캐',0),(12,'2021-11-18 23:09:49.000000','ssafycomutest@gmail.com','Y','N','2021-11-18 23:09:49.000000','NO_PASS','GOOGLE','USER','106275402865911599766','안녕 나는 지수',1);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_refresh_token`
--

DROP TABLE IF EXISTS `user_refresh_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_refresh_token` (
  `refresh_token_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `refresh_token` varchar(256) NOT NULL,
  `user_id` varchar(64) NOT NULL,
  PRIMARY KEY (`refresh_token_seq`),
  UNIQUE KEY `UK_qca3mjxv5a1egwmn4wnbplfkt` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_refresh_token`
--

LOCK TABLES `user_refresh_token` WRITE;
/*!40000 ALTER TABLE `user_refresh_token` DISABLE KEYS */;
INSERT INTO `user_refresh_token` VALUES (1,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI5MjZEOTZDOTAwMzBERDU4NDI5RDI3NTFBQzFCREJCQyIsImV4cCI6MTYzNzc0MjM1NX0.eiZH_IR7ohtTAx0PdjTqHtRaqTo7cCeXQ1NhxnAUhkI','117530665892750766708'),(2,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI5MjZEOTZDOTAwMzBERDU4NDI5RDI3NTFBQzFCREJCQyIsImV4cCI6MTYzNzc0Mjg5OX0.GbkyYVV4jfAg0gkCuVcaO25Q_PrrF3dg-pL5guZtKlQ','103866708591396004823'),(3,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI5MjZEOTZDOTAwMzBERDU4NDI5RDI3NTFBQzFCREJCQyIsImV4cCI6MTYzNzc0Mjk3MH0.oO8dvUnlZ_U42zr-YFjOlFGZUgh8nqtYQgyT1dNaFhM','106696491925243070153'),(4,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI5MjZEOTZDOTAwMzBERDU4NDI5RDI3NTFBQzFCREJCQyIsImV4cCI6MTYzNzc0MzI3MX0.gBcKumtjUp8GlVvDFXDzk0sEGjuSGz1TB-R55IeYdQ4','1995588490'),(5,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI5MjZEOTZDOTAwMzBERDU4NDI5RDI3NTFBQzFCREJCQyIsImV4cCI6MTYzNzc0NDA1NX0.1DFVuW85qScvDN1b1iCQ5EAxTvXmfQ_vGNNG1T9asDU','1995070134'),(6,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI5MjZEOTZDOTAwMzBERDU4NDI5RDI3NTFBQzFCREJCQyIsImV4cCI6MTYzNzc0NDA3Mn0.0oGuWl25Igr-Wkix16_HWUZPPMJhFktEd49YO4GQOC4','1993368718'),(7,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI5MjZEOTZDOTAwMzBERDU4NDI5RDI3NTFBQzFCREJCQyIsImV4cCI6MTYzNzc0NDY3NX0.8UxuW8HOmQNRBPF4mE_ddjroBZa1SoXtai1zXNElgJI','1995622843'),(8,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI5MjZEOTZDOTAwMzBERDU4NDI5RDI3NTFBQzFCREJCQyIsImV4cCI6MTYzNzc0NjU5N30.DIxJhCX8F4kGxA15KKeDVlEQLo2m5jigq3Htw-YvgAo','108223654202209917027'),(9,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI5MjZEOTZDOTAwMzBERDU4NDI5RDI3NTFBQzFCREJCQyIsImV4cCI6MTYzNzc2MDAxOX0.FjSYSKgr1Idk7ZL-9HQXheXxXq4dCWxPY4XutABHkmk','113372064892290193734'),(10,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI5MjZEOTZDOTAwMzBERDU4NDI5RDI3NTFBQzFCREJCQyIsImV4cCI6MTYzNzg1ODY1M30.z94dD9cWQ2_bwutxa8_Gd0K_vYdbPm9UKDFbxqy8mVQ','105470650711731543662'),(11,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI5MjZEOTZDOTAwMzBERDU4NDI5RDI3NTFBQzFCREJCQyIsImV4cCI6MTYzNzg4MTc4OX0.o1E8y7j0JnO1XLicQWbVgDRPpRMh93_EKX4AWYDZ4RI','106275402865911599766');
/*!40000 ALTER TABLE `user_refresh_token` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-11-19  2:46:57
