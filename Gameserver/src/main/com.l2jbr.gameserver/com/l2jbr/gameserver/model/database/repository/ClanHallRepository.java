package com.l2jbr.gameserver.model.database.repository;

import com.l2jbr.gameserver.model.database.ClanHallData;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ClanHallRepository extends CrudRepository<ClanHallData, Integer> {

    @Modifying
    @Query("UPDATE clanhall SET ownerId=:owner, paidUntil=:paidUntil, paid=:paid WHERE id=:id")
    int updateOwner(@Param("id") int clanHallId, @Param("owner") int ownerId, @Param("paidUntil") long paidUntil, @Param("paid") int paid);
}
