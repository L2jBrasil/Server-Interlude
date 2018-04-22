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

    @Query("SELECT clan_id FROM clan_data WHERE hasCastle=:castle")
    Integer findClanIdByCastle(@Param("castle") int castleId);

    @Modifying
    @Query("UPDATE clan_data SET hasCastle=0 WHERE hasCastle=:castle")
    int removeClastle(@Param("castle") int castleId);

    @Modifying
    @Query("UPDATE clan_data SET hasCastle=:castle WHERE clan_id=:objectId")
    int updateCastleById(@Param("objectId") int objectId, @Param("castle") int castleId);

    @Modifying
    @Query("UPDATE clan_data SET auction_bid_at=:auction WHERE clan_id=objectId")
    int updateAuctionBidById(@Param("objectId") int clanId, @Param("auction") int auction);

    @Modifying
    @Query("UPDATE clan_data SET clan_level=:level WHERE clan_id=:objectId")
    int updateLevelById(@Param("objectId") int clanId, @Param("level") int level);

    @Modifying
    @Query("UPDATE clan_data SET ally_crest_id=:crest WHERE ally_id=:ally")
    int updateAllyCrest(@Param("ally") int allyId, @Param("crest") int crestId);

    @Modifying
    @Query("UPDATE clan_data SET crest_large_id=:crest WHERE clan_id=:objectId")
    int updateLargeClanCrestById(@Param("objectId") int objectId, @Param("crest") int crestId);
}
