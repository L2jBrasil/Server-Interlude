ALTER TABLE character_subclasses ADD COLUMN class_index INT(1) NOT NULL DEFAULT 0 AFTER level;

UPDATE weapon SET soulshots=1,spiritshots=1 WHERE weaponType='pet';