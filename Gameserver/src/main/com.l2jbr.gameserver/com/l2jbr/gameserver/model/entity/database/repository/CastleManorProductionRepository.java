package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.SeedProduction;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CastleManorProductionRepository extends CrudRepository<SeedProduction, Integer> {

    @Modifying
    @Query("DELETE FROM castle_manor_production WHERE castle_id=:castle AND period=:period")
    int deleteByIdAndPeriod(@Param("castle") int castleId, @Param("period") int period);

    @Modifying
    @Query("UPDATE castle_manor_production SET can_produce=:amount WHERE seed_id=:seed AND castle_id=:castle AND period=:period")
    int updateSeedAmountInPeriod(@Param("castle") int castleId, @Param("seed") int seedId, @Param("amount") int amount,
                                 @Param("period") int period);

    @Query("SELECT * FROM castle_manor_production WHERE castle_id=:castle")
    Iterable<SeedProduction> findAllByCastle(@Param("castle") int castleId);
}
