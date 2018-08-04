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
    private int canProduce;
    @Column("start_produce")
    private int startProduce;
    @Column("seed_price")
    private int seedPrice;
    private int period;

    public SeedProduction() {}

    public SeedProduction(int castleId, int seedId, int canProduce, int startProduce, int price, int period) {
        this.castleId = castleId;
        this.seedId = seedId;
        this.canProduce = canProduce;
        this.startProduce = startProduce;
        this.seedPrice = price;
        this.period = period;
    }

    public SeedProduction(int id, int castleId) {
        this.seedId = id;
        this.castleId = castleId;
    }

    public SeedProduction(int id, int amount, int price, int sales) {
        this.seedId = id;
        this.canProduce = amount;
        this.seedPrice = price;
        this.startProduce = sales;
    }

    @Override
    public Integer getId() {
        return castleId;
    }

    public int getSeedId() {
        return seedId;
    }

    public int getCanProduce() {
        return canProduce;
    }

    public int getStartProduce() {
        return startProduce;
    }

    public void setCanProduce(int produce) {
        this.canProduce = produce;
    }

    public int getSeedPrice() {
        return seedPrice;
    }

    public int getPeriod() {
        return period;
    }
}
