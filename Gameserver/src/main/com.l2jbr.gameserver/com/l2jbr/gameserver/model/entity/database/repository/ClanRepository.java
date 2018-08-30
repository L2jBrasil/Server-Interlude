package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.Clan;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClanRepository extends CrudRepository<Clan, Integer> {

    @Modifying
    @Query("UPDATE clan_data SET crest_id=:crestId WHERE id=:clanId")
    int updateClanCrestById(@Param("clanId") int clanId, @Param("crestId") int crestId);

    @Query("SELECT id FROM clan_data WHERE castle=:castle")
    Optional<Integer> findClanIdByCastle(@Param("castle") int castleId);

    @Modifying
    @Query("UPDATE clan_data SET castle=0 WHERE castle=:castle")
    int removeClastle(@Param("castle") int castleId);

    @Modifying
    @Query("UPDATE clan_data SET castle=:castle WHERE id=:objectId")
    int updateCastleById(@Param("objectId") int objectId, @Param("castle") int castleId);

    @Modifying
    @Query("UPDATE clan_data SET auction_bid_at=:auction WHERE id=objectId")
    int updateAuctionBidById(@Param("objectId") int clanId, @Param("auction") int auction);

    @Modifying
    @Query("UPDATE clan_data SET clan_level=:level WHERE id=:objectId")
    int updateLevelById(@Param("objectId") int clanId, @Param("level") int level);

    @Modifying
    @Query("UPDATE clan_data SET ally_crest_id=:crest WHERE ally_id=:ally")
    int updateAllyCrest(@Param("ally") int allyId, @Param("crest") int crestId);

    @Modifying
    @Query("UPDATE clan_data SET crest_large_id=:crest WHERE id=:objectId")
    int updateLargeClanCrestById(@Param("objectId") int objectId, @Param("crest") int crestId);

    @Query("SELECT * FROM clan_data WHERE leader_id=:leader")
    Optional<Clan> findByLeaderId(@Param("leader") int charObjectId);

    @Query("SELECT c.* FROM clan_data c " +
           "LEFT JOIN clan_wars w ON c.id=w.clan2 " +
           "WHERE w.clan1=:clan AND NOT EXISTS (SELECT 1 FROM clan_wars WHERE clan2=:clan AND clan1=c.id)")
    Iterable<Clan> findAllByOnlyAttacker(@Param("clan") int clanId);

    @Query("SELECT c.* FROM clan_data c " +
           "LEFT JOIN clan_wars w ON c.id=w.clan1 " +
           "WHERE w.clan2=:clan AND NOT EXISTS (SELECT 1 FROM clan_wars WHERE clan1=:clan AND clan2=c.id)")
    Iterable<Clan> findAllByOnlyUnderAttack(@Param("clan") int clanId);

    @Query("SELECT c.* FROM clan_data c " +
           "LEFT JOIN clan_wars w ON c.id=w.clan2 " +
           "WHERE w.clan1=:clan AND EXISTS (SELECT 1 FROM clan_wars WHERE clan2=:clan AND clan1=c.id)")
    Iterable<Clan> findAllInWar(@Param("clan") int clanId);


}
