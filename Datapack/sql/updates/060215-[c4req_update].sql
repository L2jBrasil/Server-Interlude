ALTER TABLE character_recipebook ADD type INT NOT NULL DEFAULT 0;
UPDATE character_recipebook set type = 1;