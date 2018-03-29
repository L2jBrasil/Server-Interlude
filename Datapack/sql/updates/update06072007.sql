ALTER TABLE pets_stats ADD owner_exp_taken DECIMAL(2,2) DEFAULT '0' NOT NULL;
UPDATE pets_stats SET owner_exp_taken = 0.1 WHERE type LIKE 'baby%' AND level >= 20;
UPDATE pets_stats SET owner_exp_taken = 0.3 WHERE type LIKE 'baby%' AND level < 20;
UPDATE npc SET type = 'L2BabyPet' WHERE name IN ('Baby Buffalo','Baby Kookaburra','Baby Cougar');