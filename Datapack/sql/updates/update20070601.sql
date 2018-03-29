ALTER TABLE accounts
ADD lastServer int(4) default '1'
AFTER lastIP; 