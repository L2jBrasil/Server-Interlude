package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.MerchantLease;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface MerchantLeaseRepository extends CrudRepository<MerchantLease, Integer> {

    @Modifying
    @Query("DELETE FROM merchant_lease WHERE player_id=:player")
    int deleteByPlayer(@Param("player") int playerObjectId);
}
