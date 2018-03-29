DELETE FROM character_quests WHERE var = 'awaitSealedMStone';

INSERT INTO character_quests
SELECT DISTINCT char_id, '374_WhisperOfDreams1','awaitSealedMStone',1,0
FROM character_quests
WHERE name LIKE '374%' AND var='cond' AND value='1';

DELETE FROM character_quests WHERE var = 'awaitLight';

INSERT INTO character_quests
SELECT DISTINCT char_id, '374_WhisperOfDreams1','awaitLight',1,0
FROM character_quests
WHERE name LIKE '374%' ;

DELETE FROM character_quests WHERE var = 'awaitTooth';

INSERT INTO character_quests
SELECT DISTINCT char_id, '374_WhisperOfDreams1','awaitTooth',1,0
FROM character_quests
WHERE name LIKE '374%' ;