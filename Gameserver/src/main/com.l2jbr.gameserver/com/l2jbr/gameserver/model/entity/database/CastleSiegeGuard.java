package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Table;
import org.springframework.data.annotation.Id;

@Table("castle_siege_guards")
public class CastleSiegeGuard {

    @Id
    private int id;
    private int castleId;
    private int npcId;
    private int x;
    private int y;
    private int z;
    private int heading;
    private int respawnDelay;
    private int isHired;

    public CastleSiegeGuard() {}

    public CastleSiegeGuard(int castleId, int npcId, int x, int y, int z, int heading, int respawnDelay, int isHire) {
        this.castleId = castleId;
        this.npcId = npcId;
        this.x = x;
        this.y = y;
        this.z = z;
        this.heading = heading;
        this.respawnDelay = respawnDelay;
        this.isHired = isHire;
    }

    public int getId() {
        return id;
    }

    public int getCastleId() {
        return castleId;
    }

    public int getNpcId() {
        return npcId;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int getHeading() {
        return heading;
    }

    public int getRespawnDelay() {
        return respawnDelay;
    }

    public int getIsHired() {
        return isHired;
    }
}
