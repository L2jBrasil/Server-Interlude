ALTER TABLE clanhall ADD paid INT( 1 ) NOT NULL DEFAULT '0';
UPDATE clanhall SET paid = 1 WHERE paidUntil >0; 