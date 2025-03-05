CREATE TABLE IF NOT EXISTS `sh_audit` (
  `id` int NOT NULL AUTO_INCREMENT,
  `action` varchar(15) NOT NULL,
  `user_id` int NOT NULL,
  `target_id` int DEFAULT NULL,
  `status` int DEFAULT NULL,
  `reason` text,
  `mode` int DEFAULT NULL,
  `privs` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;