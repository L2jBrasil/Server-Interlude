ALTER TABLE `character_subclasses` CHANGE `exp` `exp` DECIMAL( 20, 0 ) DEFAULT '0' NOT NULL;
ALTER TABLE `characters` CHANGE `exp` `exp` DECIMAL( 20, 0 ) DEFAULT NULL;
ALTER TABLE `pets_stats` CHANGE `expMax` `expMax` INT( 20 ) DEFAULT '0' NOT NULL;
ALTER TABLE `pets` CHANGE `exp` `exp` DECIMAL( 20, 0 ) DEFAULT NULL;
