-- 
-- Table structure for table `mods_couples`
-- 

CREATE TABLE IF NOT EXISTS `mods_wedding` (
  `id` int(11) NOT NULL auto_increment,
  `player1Id` int(11) NOT NULL default '0',
  `player2Id` int(11) NOT NULL default '0',
  `married` BOOL default NULL,
  `affianceDate` decimal(20,0) default '0',
  `weddingDate` decimal(20,0) default '0',
  PRIMARY KEY  (`id`)
);
