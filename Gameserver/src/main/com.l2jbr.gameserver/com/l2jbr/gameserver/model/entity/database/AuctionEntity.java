package com.l2jbr.gameserver.model.entity.database;


import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.database.model.Entity;
import org.springframework.data.annotation.Id;

import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.isNull;

@Table("auction")
public class AuctionEntity extends Entity<Integer> {

    @Id
    private Integer id;
    private Integer sellerId;
    private String sellerName;
    private String sellerClanName;
    private String itemType;
    private Integer itemId;
    private Integer itemObjectId;
    private String itemName;
    private Integer itemQuantity;
    private Integer startingBid;
    private Integer currentBid;
    private Long endDate;
    @Column("auctionId")
    private Set<AuctionBid> bids;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public void setSellerClanName(String sellerClanName) {
        this.sellerClanName = sellerClanName;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public void setItemObjectId(Integer itemObjectId) {
        this.itemObjectId = itemObjectId;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemQuantity(Integer itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public void setStartingBid(Integer startingBid) {
        this.startingBid = startingBid;
    }

    public void setCurrentBid(Integer currentBid) {
        this.currentBid = currentBid;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public String getSellerClanName() {
        return sellerClanName;
    }

    public String getItemType() {
        return itemType;
    }

    public Integer getItemId() {
        return itemId;
    }

    public Integer getItemObjectId() {
        return itemObjectId;
    }

    public String getItemName() {
        return itemName;
    }

    public Integer getItemQuantity() {
        return itemQuantity;
    }

    public Integer getStartingBid() {
        return startingBid;
    }

    public Integer getCurrentBid() {
        return currentBid;
    }

    public Long getEndDate() {
        return endDate;
    }

    public Set<AuctionBid> getBids() {
        if(isNull(bids)) {
            bids = new HashSet<>();
        }
        return bids;
    }
}
