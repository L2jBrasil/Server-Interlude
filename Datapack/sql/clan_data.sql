-- ---------------------------
-- Table structure for clan_data
-- ---------------------------
CREATE TABLE IF NOT EXISTS clan_data (
  id INT NOT NULL default 0,
  clan_name varchar(45) ,
  clan_level INT,
  reputation INT NOT NULL default 0,
  castle INT,
  ally_id INT,
  ally_name varchar(45),
  leader_id INT,
  crest_id INT,
  crest_large_id INT,
  ally_crest_id INT,
  auction_bid_at INT NOT NULL default 0,
  ally_penalty_expiry_time DECIMAL( 20,0 ) NOT NULL DEFAULT 0,
  ally_penalty_type DECIMAL( 1 ) NOT NULL DEFAULT 0,
  char_penalty_expiry_time DECIMAL( 20,0 ) NOT NULL DEFAULT 0,
  dissolving_expiry_time DECIMAL( 20,0 ) NOT NULL DEFAULT 0,
  PRIMARY KEY  (id),
  UNIQUE KEY `leader_id` (`leader_id`),
  INDEX (`ally_id`),
  INDEX (castle),
  INDEX (auction_bid_at)
);