ALTER TABLE `merchant_buylists` ADD COLUMN `count` INT( 11 ) NOT NULL default '-1' AFTER `order`;
ALTER TABLE `merchant_buylists` ADD COLUMN `currentCount` INT( 11 ) NOT NULL default '-1' AFTER `count`;
ALTER TABLE `merchant_buylists` ADD COLUMN `time` INT NOT NULL DEFAULT '0' AFTER `currentCount`;
ALTER TABLE `merchant_buylists` ADD COLUMN `savetimer` DECIMAL(20,0) NOT NULL DEFAULT '0' AFTER `time`;