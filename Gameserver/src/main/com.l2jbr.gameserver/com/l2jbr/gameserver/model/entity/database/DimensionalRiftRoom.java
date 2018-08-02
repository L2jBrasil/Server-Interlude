package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.util.Rnd;
import com.l2jbr.gameserver.model.L2Spawn;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Table("dimensional_rift")
public class DimensionalRiftRoom {

    @Id
    private byte type;
    @Column("room_id")
    private byte roomId;
    private int xMin;
    private int xMax;
    private int yMin;
    private int yMax;
    private int zMin;
    private int zMax;
    private int xT;
    private int yT;
    private int zT;
    @Column("boss")
    private boolean bossRoom;

    @Transient private List<L2Spawn> spawns;
    @Transient private int[] teleportCoords;

    public boolean checkIfInZone(int x, int y, int z) {
        return (xMin <= x && xMax >= x) && (yMin <= y && yMax >= y) && (zMin <= z && zMax >= z);
    }

    public int getRandomX() {
        return Rnd.get(xMin + 128, xMax -128);
    }

    public int getRandomY() {
        return Rnd.get(yMin + 128, yMax -128);
    }

    public void unspawn() {
        spawns.forEach(spawn -> {
            spawn.stopRespawn();
            if(nonNull(spawn.getLastSpawn())) {
                spawn.getLastSpawn().deleteMe();
            }
        });
    }

    public void spawn() {
        spawns.forEach(spawn -> {
            spawn.doSpawn();
            spawn.startRespawn();
        });
    }


    public  List<L2Spawn> getSpawns() {
        if(isNull(spawns)) {
            spawns = new ArrayList<>();
        }
        return spawns;
    }

    public int[] getTeleportCoords() {
        if(isNull(teleportCoords)) {
            teleportCoords = new int[] { xT, yT, zT};
        }
        return teleportCoords;
    }

    public byte getType() {
        return type;
    }

    public byte getRoomId() {
        return roomId;
    }

    public int getxMin() {
        return xMin;
    }

    public int getxMax() {
        return xMax;
    }

    public int getyMin() {
        return yMin;
    }

    public int getyMax() {
        return yMax;
    }

    public int getzMin() {
        return zMin;
    }

    public int getzMax() {
        return zMax;
    }

    public int getTeleportX() {
        return xT;
    }

    public int getTeleportY() {
        return yT;
    }

    public int getTeleportZ() {
        return zT;
    }
    public boolean isBossRoom() {
        return bossRoom;
    }
}
