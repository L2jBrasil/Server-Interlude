-- ---------------------------
-- Table structure for clanhall
-- ---------------------------
DROP TABLE IF EXISTS `clanhall` ;

CREATE TABLE IF NOT EXISTS `clanhall` (
  `id` INT(11) NOT NULL DEFAULT '0',
  `name` VARCHAR(40) NOT NULL DEFAULT '',
  `ownerId` INT(11) NOT NULL DEFAULT '0',
  `lease` INT(10) NOT NULL DEFAULT '0',
  description TEXT NOT NULL,
  `location` VARCHAR(15) NOT NULL DEFAULT '',
  `paidUntil` DECIMAL(20,0) NOT NULL DEFAULT '0',
  `Grade` DECIMAL(1,0) NOT NULL DEFAULT '0',
  `paid` BOOL NOT NULL DEFAULT false,
  PRIMARY KEY (`id`)
);

-- ----------------------------
-- Records 
-- ----------------------------
INSERT IGNORE INTO `clanhall` VALUE
('21','Fortress of Resistance','0','100000','Ol Mahum Fortress of Resistance','Dion','0','0',false),
('22','Moonstone Hall','0','100000','Clan hall located in the Town of Gludio','Gludio','0','2',false),
('23','Onyx Hall','0','100000','Clan hall located in the Town of Gludio','Gludio','0','2',false),
('24','Topaz Hall','0','100000','Clan hall located in the Town of Gludio','Gludio','0','2',false),
('25','Ruby Hall','0','100000','Clan hall located in the Town of Gludio','Gludio','0','2',false),
('26','Crystal Hall','0','100000','Clan hall located in Gludin Village','Gludin','0','2',false),
('27','Onyx Hall','0','100000','Clan hall located in Gludin Village','Gludin','0','2',false),
('28','Sapphire Hall','0','100000','Clan hall located in Gludin Village','Gludin','0','2',false),
('29','Moonstone Hall','0','100000','Clan hall located in Gludin Village','Gludin','0','2',false),
('30','Emerald Hall','0','100000','Clan hall located in Gludin Village','Gludin','0','2',false),
('31','The Atramental Barracks','0','100000','Clan hall located in the Town of Dion','Dion','0','1',false),
('32','The Scarlet Barracks','0','100000','Clan hall located in the Town of Dion','Dion','0','1',false),
('33','The Viridian Barracks','0','100000','Clan hall located in the Town of Dion','Dion','0','1',false),
('34','Devastated Castle','0','100000','Contestable Clan Hall','Aden','0','0',false),
('35','Bandit Stronghold','0','100000','Contestable Clan Hall','Oren','0','0',false),
('36','The Golden Chamber','0','100000','Clan hall located in the Town of Aden','Aden','0','3',false),
('37','The Silver Chamber','0','100000','Clan hall located in the Town of Aden','Aden','0','3',false),
('38','The Mithril Chamber','0','100000','Clan hall located in the Town of Aden','Aden','0','3',false),
('39','Silver Manor','0','100000','Clan hall located in the Town of Aden','Aden','0','3',false),
('40','Gold Manor','0','100000','Clan hall located in the Town of Aden','Aden','0','3',false),
('41','The Bronze Chamber','0','100000','Clan hall located in the Town of Aden','Aden','0','3',false),
('42','The Golden Chamber','0','100000','Clan hall located in the Town of Giran','Giran','0','3',false),
('43','The Silver Chamber','0','100000','Clan hall located in the Town of Giran','Giran','0','3',false),
('44','The Mithril Chamber','0','100000','Clan hall located in the Town of Giran','Giran','0','3',false),
('45','The Bronze Chamber','0','100000','Clan hall located in the Town of Giran','Giran','0','3',false),
('46','Silver Manor','0','100000','Clan hall located in the Town of Giran','Giran','0','3',false),
('47','Moonstone Hall','0','100000','Clan hall located in the Town of Goddard','Goddard','0','3',false),
('48','Onyx Hall','0','100000','Clan hall located in the Town of Goddard','Goddard','0','3',false),
('49','Emerald Hall','0','100000','Clan hall located in the Town of Goddard','Goddard','0','3',false),
('50','Sapphire Hall','0','100000','Clan hall located in the Town of Goddard','Goddard','0','3',false),
('51','Mont Chamber','0','100000','An upscale Clan hall located in the Rune Township','Rune','0','3',false),
('52','Astaire Chamber','0','100000','An upscale Clan hall located in the Rune Township','Rune','0','3',false),
('53','Aria Chamber','0','100000','An upscale Clan hall located in the Rune Township','Rune','0','3',false),
('54','Yiana Chamber','0','100000','An upscale Clan hall located in the Rune Township','Rune','0','3',false),
('55','Roien Chamber','0','100000','An upscale Clan hall located in the Rune Township','Rune','0','3',false),
('56','Luna Chamber','0','100000','An upscale Clan hall located in the Rune Township','Rune','0','3',false),
('57','Traban Chamber','0','100000','An upscale Clan hall located in the Rune Township','Rune','0','3',false),
('58','Eisen Hall','0','100000','Clan hall located in the Town of Schuttgart','Schuttgart','0','2',false),
('59','Heavy Metal Hall','0','100000','Clan hall located in the Town of Schuttgart','Schuttgart','0','2',false),
('60','Molten Ore Hall','0','100000','Clan hall located in the Town of Schuttgart','Schuttgart','0','2',false),
('61','Titan Hall','0','100000','Clan hall located in the Town of Schuttgart','Schuttgart','0','2',false),
('62','Rainbow Springs','0','100000','','Goddard','0','0',false),
('63','Beast Farm','0','100000','','Rune','0','0',false),
('64','Fortress of the Dead','0','100000','','Rune','0','0',false);