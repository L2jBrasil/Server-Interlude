package com.l2jbr.gameserver.model.dao;

import com.l2jbr.commons.database.dao.DAO;

public class AuctionBid implements DAO {
    private int id;
    private int auctionId;
    private int bidderId;
    private String bidderName;
    private String clanName;
    private int maxBid;
    private long timeBid;
}
