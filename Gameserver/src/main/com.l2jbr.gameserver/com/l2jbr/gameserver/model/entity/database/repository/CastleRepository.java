package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.castleEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CastleRepository extends CrudRepository<castleEntity, Integer> {

    @Modifying
    @Query("UPDATE castle SET siegeDate=:siegeDate WHERE id=:castle")
    int updateSiegeDateById(@Param("castle") int castleId, @Param("siegeDate") long timeInMillis);

    @Modifying
    @Query("UPDATE castle SET treasury=:treasury WHERE id=:castle")
    int updateTreasuryById(@Param("castle") int castleId, @Param("treasury") int treasury);

    @Modifying
    @Query("UPDATE castle SET taxPercent=:tax WHERE id=:castle")
    int updateTaxById(@Param("castle") int castleId, @Param("tax") int taxPercent);
}
