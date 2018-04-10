package com.l2jbr.gameserver.model.dao;

import com.l2jbr.commons.database.dao.DAO;

public class Auction implements DAO {

    private int id;
    private int sellerId;
    private String sellerName;
    private String sellerClanName;
    private String itemType;
    private int itemId;
    private int itemObjectId;
    private String itemName;
    private int itemQuantity;
    private int startingBid;
    private int currentBid;
    private long endDate;
}
