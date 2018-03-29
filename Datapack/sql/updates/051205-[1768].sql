-- Needed only if your character tables are needed to be preserved.
ALTER TABLE `character_hennas` ADD `class_index` int(1) NOT NULL default '0', DROP PRIMARY KEY, ADD PRIMARY KEY (`char_obj_id`,`slot`,`class_index`);

ALTER TABLE `character_quests` ADD `class_index` int(1) NOT NULL default '0', DROP PRIMARY KEY, ADD PRIMARY KEY (`char_id`,`name`,`var`,`class_index`);

ALTER TABLE `character_shortcuts` CHANGE `unknown` `class_index` int(1) NOT NULL default '0', DROP PRIMARY KEY, ADD PRIMARY KEY (`char_obj_id`,`slot`,`page`,`class_index`);

ALTER TABLE `character_skills` ADD `class_index` int(1) NOT NULL default '0', DROP PRIMARY KEY, ADD PRIMARY KEY (`char_obj_id`,`skill_id`,`class_index`);

ALTER TABLE `character_skills_save` ADD `class_index` int(1) NOT NULL default '0', DROP PRIMARY KEY, ADD PRIMARY KEY (`char_obj_id`,`skill_id`,`class_index`);

ALTER TABLE `characters` ADD `base_class` int(2) NOT NULL default '0';