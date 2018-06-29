package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.SiegeClan;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface SiegeClanRepository extends CrudRepository<SiegeClan, Integer> {

    @Query("SELECT EXISTS ( SELECT 1 FROM siege_clans WHERE clan_id=:clan AND castle_id=:castle)")
    boolean existsByClanAndCastle(@Param("clan") int clanId, @Param("castle") int castleid);

    @Modifying
    @Query("DELETE FROM siege_clans WHERE clan_id=:clan")
    int deleteByClan(@Param("clan") int clanId);

    @Modifying
    @Query("DELETE FROM siege_clans WHERE castle_id=:castle and type=:type")
    int deleteByCastleAndType(@Param("castle") int castleId, @Param("type") int type);

    @Modifying
    @Query("DELETE FROM siege_clans WHERE castle_id=:castle and clan_id=:clan")
    int deleteByCastleAndClan(@Param("castle") int castleId, @Param("clan") int clanId);

    @Query("SELECT * FROM siege_clans where castle_id=:castle")
    Iterable<SiegeClan> findByCastle(@Param("castle") int castleId);

    @Modifying
    @Query("UPDATE siege_clans SET type=:type WHERE castle_id=:castle AND clan_id=:clan")
    int updateTypeByClan(@Param("castle") int castleId, @Param("clan") int clanId, @Param("type") int typeId);
}
