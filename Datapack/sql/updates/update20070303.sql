ALTER TABLE `clan_data`
ADD `ally_penalty_expiry_time` DECIMAL( 20,0 ) NOT NULL DEFAULT '0',
ADD `ally_penalty_type` DECIMAL( 1 ) NOT NULL DEFAULT '0',
ADD `char_penalty_expiry_time` DECIMAL( 20,0 ) NOT NULL DEFAULT '0',
ADD `dissolving_expiry_time` DECIMAL( 20,0 ) NOT NULL DEFAULT '0';

ALTER TABLE `characters`
ADD `clan_join_expiry_time` DECIMAL( 20,0 ) NOT NULL DEFAULT '0',
ADD `clan_create_expiry_time` DECIMAL( 20,0 ) NOT NULL DEFAULT '0';

ALTER TABLE `characters`
DROP `allyId`;

ALTER TABLE `characters`
DROP `deleteclan`;