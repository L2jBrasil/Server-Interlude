package com.l2jbr.gameserver.model.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import org.springframework.data.annotation.Id;

@Table("pets")
public class Pets  {

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
}
