-- needed only if your charater tables doesn't contains 'deletetime' already
alter table `characters` modify `deletetime` decimal(20,0) NOT NULL DEFAULT 0;