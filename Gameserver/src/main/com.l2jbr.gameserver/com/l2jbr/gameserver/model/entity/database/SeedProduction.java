package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.database.model.Entity;
import org.springframework.data.annotation.Id;

@Table("castle_manor_production")
public class SeedProduction extends Entity<Integer> {

    @Id
    @Column("castle_id")
    private int castleId;
    @Column("seed_id")
    private int seedId;
    @Column("can_produce")
    private int amount;
    @Column("start_produce")
    private int startAmount;
    @Column("seed_price")
    private int price;
    private int period;

    public SeedProduction() {}

    public SeedProduction(int castleId, int seedId, int amount, int startAmount, int price, int period) {
        this.castleId = castleId;
        this.seedId = seedId;
        this.amount = amount;
        this.startAmount = startAmount;
        this.price = price;
        this.period = period;
    }

    public SeedProduction(int id, int castleId) {
        this.seedId = id;
        this.castleId = castleId;
    }

    public SeedProduction(int id, int amount, int price, int sales) {
        this.seedId = id;
        this.amount = amount;
        this.price = price;
        this.startAmount = sales;
    }

    @Override
    public Integer getId() {
        return castleId;
    }

    public int getSeedId() {
        return seedId;
    }

    public int getAmount() {
        return amount;
    }

    public int getStartAmount() {
        return startAmount;
    }

    public void setAmount(int produce) {
        this.amount = produce;
    }

    public int getPrice() {
        return price;
    }

    public int getPeriod() {
        return period;
    }
}
