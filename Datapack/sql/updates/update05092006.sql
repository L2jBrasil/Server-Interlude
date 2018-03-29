UPDATE npc SET type = 'L2PenaltyMonster' WHERE id BETWEEN 13245 AND 13252;
UPDATE etcitem SET item_type = 'lure' WHERE name LIKE '%lure%' AND name NOT LIKE '%chest%';