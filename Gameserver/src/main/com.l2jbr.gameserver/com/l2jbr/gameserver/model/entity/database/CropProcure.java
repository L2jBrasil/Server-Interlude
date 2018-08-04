package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.database.model.Entity;
import org.springframework.data.annotation.Id;

@Table("castle_manor_procure")
public class CropProcure extends Entity<Integer> {

    @Id
    @Column("castle_id")
    private int castleId;
    @Column("crop_id")
    private int cropId;
    @Column("can_buy")
    private int canBuy;
    @Column("start_buy")
    private int startBuy;
    private int price;
    @Column("reward_type")
    private int rewardType;
    private int period;

    public CropProcure() {}

    public CropProcure(Integer cropId, int castleId){
        this.cropId = cropId;
        this.castleId = castleId;
    }

    public CropProcure(int castleId, int id, int amount, int startAmount, int price, int reward, int period) {
        this.castleId = castleId;
        this.cropId= id;
        this.canBuy = amount;
        this.startBuy = startAmount;
        this.price = price;
        this.rewardType = reward;
        this.period = period;
    }

    public CropProcure(int id, int amount, int type, int buy, int price) {
        this.cropId = id;
        this.canBuy = amount;
        this.rewardType = type;
        this.startBuy = buy;
        this.price = price;
    }


    @Override
    public Integer getId() {
        return castleId;
    }

    public int getCropId() {
        return cropId;
    }

    public int getCanBuy() {
        return canBuy;
    }

    public int getStartBuy() {
        return startBuy;
    }

    public int getPrice() {
        return price;
    }

    public int getRewardType() {
        return rewardType;
    }

    public int getPeriod() {
        return period;
    }

    public void setCanBuy(int buy) {
        this.canBuy = buy;
    }
}
