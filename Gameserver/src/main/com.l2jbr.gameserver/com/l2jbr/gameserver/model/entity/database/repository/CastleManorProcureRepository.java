package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.CropProcure;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CastleManorProcureRepository extends CrudRepository<CropProcure, Integer> {

    @Modifying
    @Query("DELETE FROM castle_manor_procure WHERE castle_id=:castle AND period=:period")
    int deleteByIdAndPeriod(@Param("castle") int castleId, @Param("period") int period);

    @Modifying
    @Query("UPDATE castle_manor_procure SET can_buy=:amount WHERE crop_id=:crop AND castle_id=:castle AND period=:period")
    int updateCanBuyCrop(@Param("castle") int castleId, @Param("crop") int cropId, @Param("period") int period,
                         @Param("amount") int amount);


    @Query("SELECT * FROM castle_manor_procure WHERE castle_id=:castle")
    Iterable<CropProcure> findAllByCastleId(@Param("castle") int castleId);
}
