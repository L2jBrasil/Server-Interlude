-- ---------------------------
-- Table structure for augmentations
-- ---------------------------

CREATE TABLE IF NOT EXISTS augmentations (
  item_id INT NOT NULL,
  attributes INT default 0,
  skill INT default 0,
  level INT default 0,
  PRIMARY KEY  (item_id)
);