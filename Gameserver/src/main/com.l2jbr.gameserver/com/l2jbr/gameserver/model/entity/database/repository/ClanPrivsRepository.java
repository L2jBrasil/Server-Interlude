package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.ClanPrivs;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ClanPrivsRepository extends CrudRepository<ClanPrivs, Integer> {

    @Query("SELECT * FROM clan_privs WHERE clan_id=:clan")
    Iterable<ClanPrivs> findAllByClan(@Param("clan") int clanId);

    @Modifying
    @Query("INSERT INTO clan_privs (clan_id,rank,party,privs) VALUES (:clan,:rank,:party,:privs) ON DUPLICATE KEY UPDATE privs=:privs")
    int saveOrUpdate(@Param("clan") int clanId, @Param("rank") int rank, @Param("party") int party, @Param("privs") int privs);
}
