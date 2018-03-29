ALTER TABLE characters ADD COLUMN death_penalty_level int(2) NOT NULL DEFAULT 0 AFTER clan_create_expiry_time;
