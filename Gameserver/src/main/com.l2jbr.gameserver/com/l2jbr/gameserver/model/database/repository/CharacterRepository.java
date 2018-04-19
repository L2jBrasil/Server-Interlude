package com.l2jbr.gameserver.model.database.repository;

import com.l2jbr.gameserver.model.database.Character;
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
}
