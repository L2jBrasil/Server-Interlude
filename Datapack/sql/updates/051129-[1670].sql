-- needed only if your clan_data tables doesn't contains 'crest_id' and 'ally_crest_id' already
alter table `clan_data` add column `crest_id` INT DEFAULT 0;
alter table `clan_data` add column `ally_crest_id` INT DEFAULT 0;