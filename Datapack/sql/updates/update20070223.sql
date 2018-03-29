ALTER TABLE raidboss_spawnlist DROP respawn_delay;
ALTER TABLE raidboss_spawnlist ADD respawn_min_delay INT( 11 ) NOT NULL default '43200' AFTER heading; -- 12 (36-24) hours
ALTER TABLE raidboss_spawnlist ADD respawn_max_delay INT( 11 ) NOT NULL default '129600' AFTER respawn_min_delay; -- 36 hours
DELETE FROM raidboss_spawnlist WHERE boss_id IN (25328, 25339, 25342, 25346, 25349); -- remove Shadow of Halisha and Hellman spawns (possible exploits)