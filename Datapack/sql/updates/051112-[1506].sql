-- needed only if your charater tables doesn't contains 'deleteclan' already
alter table `characters` add column `deleteclan` decimal(20,0) NOT NULL DEFAULT 0;