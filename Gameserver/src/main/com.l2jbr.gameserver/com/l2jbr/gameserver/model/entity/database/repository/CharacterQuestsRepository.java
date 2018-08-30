package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.CharacterQuests;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CharacterQuestsRepository extends CrudRepository<CharacterQuests, Integer> {

    @Query("SELECT * FROM character_quests WHERE char_id=:objectId AND var='<state>'")
    Iterable<CharacterQuests> findAllByState(@Param("objectId") int charObjectId);

    @Modifying
    @Query("DELETE FROM character_quests WHERE char_id=:objectId and name=:name")
    int deleteByName(@Param("objectId") int charObjectId, @Param("name") String questName);

    @Query("SELECT * FROM character_quests WHERE char_id=:objectId")
    Iterable<CharacterQuests> findAllByCharacter(@Param("objectId") int charObjectId);

    @Modifying
    @Query("DELETE FROM character_quests WHERE char_id=:objectId AND name=:name AND var=:var")
    int deleteByNameAndVar(@Param("objectId") int charObjectId, @Param("name") String questName, @Param("var") String var);

    @Modifying
    @Query("UPDATE character_quests SET value=:value WHERE char_id=:objectId AND name=:name AND var=:var")
    int updateQuestVar(@Param("objectId") int charObjectId, @Param("name") String questName, @Param("var") String var, @Param("value") String value);

    @Modifying
    @Query("DELETE FROM character_quests WHERE name=:name and char_id IN (SELECT obj_id FROM characters WHERE clanId=:clan AND online=0")
    int deleteAllByOfflineClanMembers(@Param("name") String questName, @Param("clan") int clanId);

    @Query("SELECT * FROM character_quests WHERE char_id=:char AND name=:name AND var=:var")
    Iterable<CharacterQuests> findByNameAndVar(@Param("char") int charObjectId, @Param("name") String questName, @Param("var") String var);
}
