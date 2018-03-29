-- add colum friend_id
ALTER TABLE character_friends ADD COLUMN friend_id INT(11) DEFAULT 0 NOT NULL AFTER char_id;

-- get the friend_id
UPDATE character_friends SET friend_id=(SELECT obj_Id FROM characters WHERE char_name=friend_name); 