package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.Character;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CharacterRepository extends CrudRepository<Character, Integer> {

    @Query("SELECT * FROM characters WHERE char_name=:charName")
    Optional<Character> findByCharName(@Param("charName") String charName);

    @Modifying
    @Query("UPDATE characters SET accesslevel=:accessLevel WHERE char_name=:name")
    int updateAccessLevelByCharName(@Param("name") String charName, @Param("accessLevel") int accessLevel);

    @Query("SELECT * FROM characters WHERE account_name=:account AND obj_Id<>:objectId")
    Iterable<Character> findOthersCharactersOnAccount(@Param("account") String accountName, @Param("objectId") int objectId);

    @Modifying
    @Query("UPDATE characters SET deletetime=:deleteTime WHERE obj_id=:objectId")
    int updateDeleteTime(@Param("objectId") int objectId, @Param("deleteTime") long deleteTime);

    @Query("SELECT * FROM characters WHERE account_name=:account")
    Iterable<Character> findAllByAccountName(@Param("account") String account);

    @Modifying
    @Query("UPDATE characters SET clanid=0 WHERE clanid=:clanId")
    int removeClanId(@Param("clanId") int clanId);

    @Query("SELECT * FROM characters WHERE clanId=:clanId")
    Iterable<Character> findAllByClanId(@Param("clanId") int clanId);

    @Query("SELECT account_name FROM characters WHERE char_name=:name")
    String findAccountByName(@Param("name") String charName);

    @Query("SELECT EXISTS (SELECT 1 FROM characters WHERE char_name=:name LIMIT 1)")
    boolean existsByName(@Param("name") String name);

    @Query("SELECT COUNT(1) FROM characters WHERE account_name=:account")
    int countByAccount(@Param("account") String account);

    @Query("SELECT clanid FROM characters WHERE obj_Id =:objectId")
    int findClanIdById(@Param("objectId") int charId);

    @Modifying
    @Query("UPDATE characters SET x=:x, y=:y, z=:z, in_jail=:inJail, jail_timer=:jailTime WHERE char_name=:name")
    int updateJailStatusByName(@Param("name") String charName, @Param("x") int x, @Param("y") int y, @Param("z") int z,
                                @Param("inJail") int inJail, @Param("jailTime") long jailTime);

    @Modifying
    @Query("UPDATE characters SET apprentice=0 WHERE apprentice=:apprenticeId")
    int removeApprentice(@Param("apprenticeId") int apprenticeId);

    @Modifying
    @Query("UPDATE characters SET sponsor=0 WHERE sponsor=:sponsorId")
    int removeSponsor(@Param("sponsorId") int sponsorId);

    @Modifying
    @Query("UPDATE characters SET karma=:karma, pkkills=:pkKills WHERE obj_id=:objectId")
    int updatePKAndKarma(@Param("objectId") int objectId, @Param("pkKills") int playerPkKills, @Param("karma") int playerKarma);

    @Modifying
    @Query("UPDATE characters SET subpledge=:subpledge WHERE obj_id=:objectId")
    void updateSubpledge(@Param("objectId") int objectId, @Param("subpledge") int subpledge);

    @Modifying
    @Query("UPDATE characters SET online=:online, lastAccess=:lastAccess WHERE obj_id=:objectId")
    int updateOnlineStatus(@Param("objectId") int objectId, @Param("online") int online, @Param("lastAccess") long lastAccess);

    @Modifying
    @Query("UPDATE characters SET isIn7sDungeon=:inDungeon WHERE obj_id=:objectId")
    int updateSevenSignsDungeonStatus(@Param("objectId") int objectId, @Param("inDungeon") int isInDungeon);

    @Modifying
    @Query("UPDATE characters SET power_grade=:powerGrade WHERE obj_id=:objectId")
    int updatePowerGrade(@Param("objectId") int objectId, @Param("powerGrade") int powerGrade);

    @Query("SELECT clanid FROM characters WHERE char_name=:name")
    int findClanIdByName(@Param("name") String name);

    @Query("SELECT obj_Id FROM characters WHERE char_name=:name")
    int findIdByName(@Param("name") String name);

    @Modifying
    @Query("UPDATE characters SET online=:online")
    int updateAllOnlineStatus(@Param("online") int online);

    @Modifying
    @Query("UPDATE characters SET apprentice=:apprentice,sponsor=:sponsor WHERE obj_Id=:objectId")
    int updateApprenticeAndSponsor(@Param("objectId") int objectId, @Param("apprentice") int apprentice, @Param("sponsor") int sponsor);
}
