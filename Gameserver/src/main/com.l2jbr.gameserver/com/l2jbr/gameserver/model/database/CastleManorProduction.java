package com.l2jbr.gameserver.model.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.database.model.Entity;
import org.springframework.data.annotation.Id;

@Table("castle_manor_production")
public class CastleManorProduction  extends Entity<Integer> {

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

    public CastleManorProduction() {}

    public CastleManorProduction(int castleId, int seedId, int canProduce, int startProduce, int price, int period) {
        this.castleId = castleId;
        this.seedId = seedId;
        this.canProduce = canProduce;
        this.startProduce = startProduce;
        this.seedPrice = price;
        this.period = period;
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

    public int getSeedPrice() {
        return seedPrice;
    }

    public int getPeriod() {
        return period;
    }
}
