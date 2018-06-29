package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.ClanWars;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ClanWarsRepository extends CrudRepository<ClanWars, String> {

    @Modifying
    @Query("DELETE FROM clan_wars WHERE clan1=:clan OR clan2=:clan")
    int deleteByClan(@Param("clan") int clanId);

    @Modifying
    @Query("REPLACE INTO clan_wars (clan1, clan2, wantspeace1, wantspeace2) VALUES(:clan1,:clan2,:wantsPeace1,:wantsPeace2)")
    int saveOrUpdate(@Param("clan1") int clanId1, @Param("clan2") int clanId2, @Param("wantsPeace1") int wantsPeace1,
                     @Param("wantsPeace2") int wantsPeace2);

    @Modifying
    @Query("DELETE FROM clan_wars WHERE clan1=:clan1 AND clan2=:clan2")
    int deleteWar(@Param("clan1") int clanId1, @Param("clan2") int clanId2);
}
