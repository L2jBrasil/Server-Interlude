package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.AuctionEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AuctionRepository extends CrudRepository<AuctionEntity, Integer> {

    @Modifying
    @Query("UPDATE auction SET endDate=:endDate WHERE id=:id")
    int updateEndDateById(@Param("id") int id, @Param("endDate") long endDate);

    @Modifying
    @Query("DELETE FROM auction WHERE itemId=:itemId")
    int deleteByItemId(@Param("itemId") int itemId);
}
