package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.database.model.Entity;
import com.l2jbr.gameserver.model.L2Spawn;
import org.springframework.data.annotation.Id;

@Table("spawnlist")
public class Spawnlist extends Entity<Integer> {

    @Id
    private int id;
    private String location;
    private int count;
    @Column("npc_templateid")
    private int npcTemplateId;
    private int locx;
    private int locy;
    private int locz;
    private int randomx;
    private int randomy;
    private int heading;
    @Column("respawn_delay")
    private int respawnDelay;
    @Column("loc_id")
    private int locId;
    private int periodOfDay;

    public Spawnlist() { }

    public Spawnlist(L2Spawn spawn) {
        this.id = spawn.getId();
        this.count = spawn.getAmount();
        this.npcTemplateId = spawn.getNpcid();
        this.locx = spawn.getLocx();
        this.locy = spawn.getLocy();
        this.locz = spawn.getLocz();
        this.heading = spawn.getHeading();
        this.respawnDelay = spawn.getRespawnDelay() / 1000;
        this.locId = spawn.getLocation();
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public int getCount() {
        return count;
    }

    public int getNpcTemplateId() {
        return npcTemplateId;
    }

    public int getLocx() {
        return locx;
    }

    public int getLocy() {
        return locy;
    }

    public int getLocz() {
        return locz;
    }

    public int getRandomx() {
        return randomx;
    }

    public int getRandomy() {
        return randomy;
    }

    public int getHeading() {
        return heading;
    }

    public int getRespawnDelay() {
        return respawnDelay;
    }

    public int getLocId() {
        return locId;
    }

    public int getPeriodOfDay() {
        return periodOfDay;
    }
}