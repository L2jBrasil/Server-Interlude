package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.ClanHallEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ClanHallRepository extends CrudRepository<ClanHallEntity, Integer> {

    @Modifying
    @Query("UPDATE clanhall SET ownerId=:owner, paidUntil=:paidUntil, paid=:paid WHERE id=:id")
    int updateOwner(@Param("id") int clanHallId, @Param("owner") int ownerId, @Param("paidUntil") long paidUntil, @Param("paid") int paid);

    @Modifying
    @Query("UPDATE clanhall SET sellerId=0, sellerName='NPC', sellerClanName='NPC Clan', itemObjectId=0, startingBid=:bid, " +
            "currentBid=0, endDate=1164841200000 WHERE id=:id")
    int updateWithInitialData(@Param("id") int id, @Param("bid") int startingBid);
}
