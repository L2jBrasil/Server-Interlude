package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.ClanSubpledges;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ClanSubpledgesRepository extends CrudRepository<ClanSubpledges, Integer> {

    @Query("SELECT * FROM clan_subpledges WHERE clan_id=:clan")
    Iterable<ClanSubpledges> findAllByClan(@Param("clan") int clanId);

    @Modifying
    @Query("UPDATE clan_subpledges SET leader_name=:leader WHERE clan_id=:clan AND sub_pledge_id=:subpledge")
    int updateLeader(@Param("clan") int clanId, @Param("subpledge") int pledgeId, @Param("leader") String leaderName);
}
