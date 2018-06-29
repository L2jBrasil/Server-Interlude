package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.AuctionBid;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AuctionBidRepository extends CrudRepository<AuctionBid, Integer> {

    @Query("SELECT * FROM auction_bid WHERE auctionId=:auctionId ORDER BY maxBid DESC")
    Iterable<AuctionBid> findByAuctionId(@Param("auctionId") int auctionId);

    @Modifying
    @Query("UPDATE auction_bid SET bidderName=:bidderName, maxBid=:bid, time_bid=:timeBid WHERE auctionId=:auction AND bidderId=:bidder")
    int updateByAuctionAndBidder(@Param("auction") int auctionId, @Param("bidder") int bidderId, @Param("bidderName") String leaderName,
                                 @Param("bid") int bid, @Param("timeBid") long timeBid);

    @Modifying
    @Query("DELETE FROM auction_bid WHERE auctionId=:auction")
    int deleteByAuction(@Param("auction") int auctionId);

    @Modifying
    @Query("DELETE FROM auction_bid WHERE auctionId=:auction AND bidderId=:bidder")
    int deleteByAuctionAndBidder(@Param("auction") int auctionId, @Param("bidder") int bidderId);
}
