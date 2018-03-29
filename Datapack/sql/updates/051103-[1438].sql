-- needed only if your charater tables doesn't contains clan_privs already
ALTER TABLE `characters` ADD `clan_privs` INT DEFAULT '0' NOT NULL ;