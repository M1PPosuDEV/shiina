CREATE TABLE IF NOT EXISTS `userpages` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `html` text,
  `raw` text,
  `raw_type` enum('tiptap') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;
