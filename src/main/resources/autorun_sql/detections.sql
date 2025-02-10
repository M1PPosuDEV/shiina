CREATE TABLE IF NOT EXISTS `sh_detections` (
    `user` INT NOT NULL,
    `target` INT NOT NULL,
    `detection` DATETIME NOT NULL,
    `score` TINYINT NOT NULL,
    PRIMARY KEY (`target`, `user`)
) ENGINE = InnoDB;