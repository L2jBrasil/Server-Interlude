package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.database.model.Entity;
import com.l2jbr.gameserver.datatables.NpcTable;
import com.l2jbr.gameserver.templates.ISpawn;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

@Table("raidboss_spawnlist")
public class RaidbossSpawn extends Entity<Integer> implements ISpawn {

    @Id
    @Column("boss_id")
    private int id;
    private int amount;
    @Column("loc_x")
    private int x;
    @Column("loc_y")
    private int y;
    @Column("loc_z")
    private int z;
    private int heading;
    @Column("respawn_min_delay")
    private int respawnMinDelay;
    @Column("respawn_max_delay")
    private int respawnMaxDelay;
    @Column("respawn_time")
    private long respawnTime;
    private double currentHp;
    private double currentMp;

    @Transient  private NpcTemplate template;

    public RaidbossSpawn() {
    }

    public RaidbossSpawn(int npcid, int amount, int locx, int locy, int locz, int heading, long respawnTime, double hp, double mp) {
        this.id = npcid;
        this.amount = amount;
        this.x = locx;
        this.y = locy;
        this.z = locz;
        this.heading = heading;
        this.respawnTime = respawnTime;
        this.currentHp = hp;
        this.currentMp = mp;
    }

    @Override
    public void onLoad() {
        super.onLoad();

        template = NpcTable.getInstance().getTemplate(id);
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getCount() {
        return amount;
    }

    @Override
    public void setCount(int amount) {
        this.amount = amount;
    }

    @Override
    public int getRespawnDelay() {
        return 0;
    }

    @Override
    public void setRespawnDelay(int delay) {

    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public void setX(int locx) {
        this.x = locx;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setY(int locy) {
        this.y = locy;
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public void setZ(int locz) {
        this.z = locz;
    }

    @Override
    public int getHeading() {
        return heading;
    }

    @Override
    public void setHeading(int heading) {
        this.heading = heading;
    }

    @Override
    public int getLocId() {
        return 0;
    }

    @Override
    public void setLocId(int location) {

    }

    @Override
    public NpcTemplate getNpcTemplate() {
        return template;
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
