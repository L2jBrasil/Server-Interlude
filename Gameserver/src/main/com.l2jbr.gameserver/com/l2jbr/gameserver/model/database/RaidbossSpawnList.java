package com.l2jbr.gameserver.model.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import org.springframework.data.annotation.Id;

@Table("raidboss_spawnlist")
public class RaidbossSpawnList  {

    @Id
    @Column("boss_id")
    private int bossId;
    private int amount;
    @Column("loc_x")
    private int locX;
    @Column("loc_y")
    private int locY;
    @Column("loc_z")
    private int locZ;
    private int heading;
    @Column("respawn_min_delay")
    private int respawnMinDelay;
    @Column("respawn_max_delay")
    private int respawnMaxDelay;
    @Column("respawn_time")
    private long respawnTime;
    private double currentHp;
    private double currentMp;

    public RaidbossSpawnList() {
    }

    public RaidbossSpawnList(int npcid, int amount, int locx, int locy, int locz, int heading, long respawnTime, double hp, double mp) {
        this.bossId = npcid;
        this.amount = amount;
        this.locX = locx;
        this.locY = locy;
        this.locZ = locz;
        this.heading = heading;
        this.respawnTime = respawnTime;
        this.currentHp = hp;
        this.currentMp = mp;
    }

    public int getBossId() {
        return bossId;
    }

    public int getAmount() {
        return amount;
    }

    public int getLocX() {
        return locX;
    }

    public int getLocY() {
        return locY;
    }

    public int getLocZ() {
        return locZ;
    }

    public int getHeading() {
        return heading;
    }

    public int getRespawnMinDelay() {
        return respawnMinDelay;
    }

    public int getRespawnMaxDelay() {
        return respawnMaxDelay;
    }

    public long getRespawnTime() {
        return respawnTime;
    }

    public double getCurrentHp() {
        return currentHp;
    }

    public double getCurrentMp() {
        return currentMp;
    }
}
