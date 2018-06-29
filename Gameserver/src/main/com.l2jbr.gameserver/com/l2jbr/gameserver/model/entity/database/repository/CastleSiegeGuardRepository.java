package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.CastleSiegeGuard;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CastleSiegeGuardRepository extends CrudRepository<CastleSiegeGuard, Integer> {

    @Modifying
    @Query("DELETE FROM castle_siege_guards WHERE npcId=:npc AND x=:x AND y=:y AND z=:z AND isHired=1")
    int deleteMercenaryByNpcAndLocation(@Param("npc") int npcId, @Param("x") int x, @Param("y") int y, @Param("z") int z);

    @Query("SELECT * FROM castle_siege_guards WHERE isHired=1")
    Iterable<CastleSiegeGuard> findAllMercenary();

    @Modifying
    @Query("DELETE FROM castle_siege_guards WHERE castleId=:castle AND isHired=:1")
    int deleteMercenaryByCastle(@Param("castle") int castleId);

    @Query("SELECT * FROM castle_siege_guards WHERE castleId=:castle AND isHired=:hired")
    Iterable<CastleSiegeGuard> findAllByCastleAndHired(@Param("castle") int castleId, @Param("hired") int hired);
}
