ALTER TABLE `spawnlist` ADD `periodOfDay` decimal(2,0) default 0;
update spawnlist set periodOfDay = 0;

-- 0 = Default
-- 1 = Day mob
-- 2 = Night mob