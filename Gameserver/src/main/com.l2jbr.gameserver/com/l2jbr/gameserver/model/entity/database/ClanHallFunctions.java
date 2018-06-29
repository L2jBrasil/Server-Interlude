package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.database.model.Entity;
import org.springframework.data.annotation.Id;

@Table("clanhall_functions")
public class ClanHallFunctions extends Entity<Integer> {

    @Id
    @Column("hall_Id")
    private int hallId;
    private int type;
    private int lvl;
    private int lease;
    private long rate;
    private long endTime;

    public  ClanHallFunctions() {}

    public ClanHallFunctions(int id, int type, int lvl, int lease, long rate, long endTime) {
        this.hallId = id;
        this.type = type;
        this.lvl =lvl;
        this.lease = lease;
        this.rate = rate;
        this.endTime = endTime;
    }

    @Override
    public Integer getId() {
        return hallId;
    }

    public int getType() {
        return type;
    }

    public int getLvl() {
        return lvl;
    }

    public int getLease() {
        return lease;
    }

    public long getRate() {
        return rate;
    }

    public long getEndTime() {
        return endTime;
    }
}
