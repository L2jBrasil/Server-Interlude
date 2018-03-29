-- add column onlinetime
ALTER TABLE `characters` ADD `onlinetime` DECIMAL( 20, 0 ) DEFAULT '0' NOT NULL AFTER `online`; 