-- 
-- Alter `characters` table
-- 
ALTER TABLE `characters` ADD COLUMN in_jail decimal(1,0) DEFAULT 0;
ALTER TABLE `characters` ADD COLUMN jail_timer decimal(20,0) DEFAULT 0;


-- 
-- Insert data in table `zone`
-- 

INSERT INTO `zone` VALUES (1, 'Jail', 'GM Jail', -115600, -250700, -113500, -248200, 0, 0);