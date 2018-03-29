-- needed only if your charater tables doesn't contains 'wantspeace' already
alter table `characters` add column `wantspeace` decimal(1,0) DEFAULT 0;