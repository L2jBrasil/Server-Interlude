package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.util.Rnd;
import org.springframework.data.annotation.Id;

@Table("minions")
public class Minions  {
    @Id
    @Column("boss_id")
    private int bossId;
    @Column("minion_id")
    private int minionId;
    @Column("amount_min")
    private int amountMin;
    @Column("amount_max")
    private int amountMax;

    public int getBossId() {
        return bossId;
    }

    public int getMinionId() {
        return minionId;
    }

    public int getAmountMin() {
        return amountMin;
    }

    public int getAmountMax() {
        return amountMax;
    }

    public int getAmount() {
        if (amountMax > amountMin) {
            return Rnd.get(amountMin, amountMax);
        }
        return amountMin;
    }
}
