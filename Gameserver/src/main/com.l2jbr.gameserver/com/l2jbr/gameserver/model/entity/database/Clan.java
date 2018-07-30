package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.database.model.Entity;
import org.springframework.data.annotation.Id;

@Table("clan_data")
public class Clan extends Entity<Integer> {

    @Id
    private int id;
    @Column("clan_name")
    private String name;
    @Column("clan_level")
    private int level;
    private Integer reputation;
    private Integer castle;
    @Column("ally_id")
    private Integer allyId;
    @Column("ally_name")
    private String allyName;
    @Column("leader_id")
    private Integer leaderId;
    @Column("crest_id")
    private Integer crestId;
    @Column("crest_large_id")
    private Integer crestLargeId;
    @Column("ally_crest_id")
    private Integer allyCrestId;
    @Column("auction_bid_at")
    private Integer auctionBidAt;
    @Column("ally_penalty_expiry_time")
    private Long allyPenaltyExpiryTime;
    @Column("ally_penalty_type")
    private Integer allyPenaltyType;
    @Column("char_penalty_expiry_time")
    private Long charPenaltyExpiryTime;
    @Column("dissolving_expiry_time")
    private Long dissolvingExpiryTime;

    @Override
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String clanName) {
        this.name = clanName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(Integer clanLevel) {
        this.level = clanLevel;
    }

    public Integer getReputation() {
        return reputation;
    }

    public void setReputation(Integer reputation) {
        this.reputation = reputation;
    }

    public Integer getCastle() {
        return castle != null ? castle : 0;
    }

    public void setCastle(Integer hasCastle) {
        this.castle = hasCastle;
    }

    public Integer getAllyId() {
        return allyId;
    }

    public void setAllyId(Integer allyId) {
        this.allyId = allyId;
    }

    public String getAllyName() {
        return allyName;
    }

    public void setAllyName(String allyName) {
        this.allyName = allyName;
    }

    public Integer getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Integer leaderId) {
        this.leaderId = leaderId;
    }

    public Integer getCrestId() {
        return crestId != null ? crestId : 0;
    }

    public void setCrestId(Integer crestId) {
        this.crestId = crestId;
    }

    public Integer getCrestLargeId() {
        return crestLargeId != null ? crestLargeId : 0;
    }

    public void setCrestLargeId(Integer crestLargeId) {
        this.crestLargeId = crestLargeId;
    }

    public Integer getAllyCrestId() {
        return allyCrestId != null ? allyCrestId : 0;
    }

    public void setAllyCrestId(Integer allyCrestId) {
        this.allyCrestId = allyCrestId;
    }

    public Integer getAuctionBidAt() {
        return auctionBidAt != null ? auctionBidAt : 0;
    }

    public void setAuctionBidAt(Integer auctionBidAt) {
        this.auctionBidAt = auctionBidAt;
    }

    public Long getAllyPenaltyExpiryTime() {
        return allyPenaltyExpiryTime;
    }

    public void setAllyPenaltyExpiryTime(Long allyPenaltyExpiryTime) {
        this.allyPenaltyExpiryTime = allyPenaltyExpiryTime;
    }

    public int getAllyPenaltyType() {
        return allyPenaltyType;
    }

    public void setAllyPenaltyType(int allyPenaltyType) {
        this.allyPenaltyType = allyPenaltyType;
    }

    public Long getCharPenaltyExpiryTime() {
        return charPenaltyExpiryTime;
    }

    public void setCharPenaltyExpiryTime(Long charPenaltyExpiryTime) {
        this.charPenaltyExpiryTime = charPenaltyExpiryTime;
    }

    public Long getDissolvingExpiryTime() {
        return dissolvingExpiryTime;
    }

    public void setDissolvingExpiryTime(Long dissolvingExpiryTime) {
        this.dissolvingExpiryTime = dissolvingExpiryTime;
    }
}
