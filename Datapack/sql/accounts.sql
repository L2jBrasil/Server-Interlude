-- ---------------------------
-- Table structure for accounts
-- ---------------------------
CREATE TABLE IF NOT EXISTS `accounts` (
  `login` VARCHAR(45) NOT NULL DEFAULT '',
  `password` VARCHAR(45)  DEFAULT NULL,
  `lastactive` DECIMAL(20,0)  DEFAULT NULL,
  `access_level` INT  DEFAULT 0,
  `lastIP` VARCHAR(20)  DEFAULT NULL,
  `lastServer` INT NOT NULL DEFAULT 1,
  `newbieCharacterId` DECIMAL(11,0) NULL,
  PRIMARY KEY (`login`))
ENGINE = InnoDB