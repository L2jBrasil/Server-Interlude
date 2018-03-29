CREATE TABLE `repair_character_quests` (
	  `char_id` INT NOT NULL DEFAULT 0,
 	  `cond` VARCHAR(40) NOT NULL DEFAULT '',
 	  PRIMARY KEY  (`char_id`,`cond`)
 	  );
 	
INSERT INTO `repair_character_quests` SELECT `char_id`,`value` FROM `character_quests` WHERE `name` = '336_CoinOfMagic' and `var`= 'cond';

UPDATE `character_quests`,repair_character_quests  SET 
character_quests.`value` = 'Solo' 
WHERE character_quests.`name` = '336_CoinOfMagic' and 
character_quests.`var` = '<state>' and character_quests.`value` = 'Started' and 
character_quests.`char_id` =  repair_character_quests.`char_id` AND  repair_character_quests.`cond` < 4;

UPDATE `character_quests`,repair_character_quests  SET 
character_quests.`value` = 'Party' WHERE character_quests.`name` = '336_CoinOfMagic' and 
character_quests.`var` = '<state>' and character_quests.`value` = 'Started' and 
character_quests.`char_id` =  repair_character_quests.`char_id` AND  repair_character_quests.`cond` >= 4;

DROP TABLE `repair_character_quests`;