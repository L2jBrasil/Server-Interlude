package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.database.model.Entity;
import com.l2jbr.gameserver.datatables.NpcTable;
import com.l2jbr.gameserver.model.L2Spawn;
import com.l2jbr.gameserver.templates.ISpawn;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

@Table("spawnlist")
public class Spawn extends Entity<Integer> implements ISpawn {

    @Id
    private int id;
    private String location;
    private int count;
    @Column("npc_templateid")
    private int npcTemplateId;
    @Column("locx")
    private int x;
    @Column("locy")
    private int y;
    @Column("locz")
    private int z;
    private int randomx;
    private int randomy;
    private int heading;
    @Column("respawn_delay")
    private int respawnDelay;
    @Column("loc_id")
    private int locId;
    private int periodOfDay;

    @Transient private NpcTemplate npcTemplate;

    public Spawn(L2Spawn spawn) {

    }

    public Spawn() {}


    @Override
    public void onLoad() {
        super.onLoad();
        npcTemplate = NpcTable.getInstance().getTemplate(npcTemplateId);
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getNpcTemplateId() {
        return npcTemplateId;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
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

    public void setHeading(int heading) {
        this.heading = heading;
    }

    public int getRespawnDelay() {
        return respawnDelay;
    }

    public void setRespawnDelay(int respawnDelay) {
        this.respawnDelay = respawnDelay;
    }

    public int getLocId() {
        return locId;
    }

    public void setLocId(int locId) {
        this.locId = locId;
    }

    public int getPeriodOfDay() {
        return periodOfDay;
    }

    public NpcTemplate getNpcTemplate() {
        return npcTemplate;
    }

    public void setNpcTemplate(NpcTemplate npcTemplate) {
        this.npcTemplate = npcTemplate;
    }
}