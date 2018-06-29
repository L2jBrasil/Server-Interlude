package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.database.model.Entity;
import org.springframework.data.annotation.Id;

@Table("pets")
public class Pets extends Entity<Integer> {

    @Id
    @Column("item_obj_id")
    private int itemObjId;
    private String name;
    private byte level;
    private double curHp;
    private double curMp;
    private long exp;
    private int sp;
    private int karma;
    private int pkkills;
    private int fed;

    public Pets() {}

    public Pets( int objectId, String name, byte level, double currentHp, double currentMp, long exp, int sp, int karma, int pkKills, int currentFed) {
        this.itemObjId = objectId;
        this.name = name;
        this.level = level;
        this.curHp = currentHp;
        this.curMp = currentMp;
        this.exp = exp;
        this.sp = sp;
        this.karma = karma;
        this.pkkills = pkKills;
        this.fed = currentFed;
    }

    @Override
    public Integer getId() {
        return itemObjId;
    }

    public String getName() {
        return name;
    }

    public byte getLevel() {
        return level;
    }

    public double getCurHp() {
        return curHp;
    }

    public double getCurMp() {
        return curMp;
    }

    public long getExp() {
        return exp;
    }

    public int getSp() {
        return sp;
    }

    public int getKarma() {
        return karma;
    }

    public int getPkkills() {
        return pkkills;
    }

    public int getFed() {
        return fed;
    }
}
