ALTER TABLE characters ADD COLUMN lvl_joined_academy int(1) NOT NULL DEFAULT 0 AFTER last_recom_date;
ALTER TABLE characters ADD COLUMN apprentice int(1) NOT NULL DEFAULT 0 AFTER lvl_joined_academy;
ALTER TABLE characters ADD COLUMN sponsor int(1) NOT NULL DEFAULT 0 AFTER apprentice;