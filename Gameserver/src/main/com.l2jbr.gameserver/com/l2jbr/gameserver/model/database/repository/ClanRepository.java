package com.l2jbr.gameserver.model.database.repository;

import com.l2jbr.gameserver.model.database.ClanData;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ClanRepository extends CrudRepository<ClanData, Integer> {

    @Modifying
    @Query("UPDATE clan_data SET crest_id=:crestId WHERE clan_id=:clanId")
    int updateClanCrestById(@Param("clanId") int clanId, @Param("crestId") int crestId);
}
