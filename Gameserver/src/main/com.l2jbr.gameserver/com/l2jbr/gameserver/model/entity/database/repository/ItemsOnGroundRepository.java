package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.ItemsOnGround;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ItemsOnGroundRepository extends CrudRepository<ItemsOnGround, Integer> {

    @Modifying
    @Query("UPDATE itemsonground SET drop_time=:dropTime WHERE drop_time=-1 AND equipable=0")
    int updateDropTimeNotEquipable(@Param("dropTime") long dropTime);

    @Modifying
    @Query("UPDATE itemsonground SET drop_time=:dropTime WHERE drop_time=-1")
    int updateDropTime(@Param("dropTime") long dropTime);
}
