package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import org.springframework.data.annotation.Id;

@Table("walker_routes")
public class WalkerRouteNode {
    @Id
    @Column("route_id")
    private int routeId;
    @Column("npc_id")
    private int npcId;
    @Column("move_point")
    private int movePoint;
    private String chatText;
    @Column("move_x")
    private int moveX;
    @Column("move_y")
    private int moveY;
    @Column("move_z")
    private int moveZ;
    private int delay;
    private boolean running;

    public int getRouteId() {
        return routeId;
    }

    public int getNpcId() {
        return npcId;
    }

    public int getMovePoint() {
        return movePoint;
    }

    public String getChatText() {
        return chatText;
    }

    public int getMoveX() {
        return moveX;
    }

    public int getMoveY() {
        return moveY;
    }

    public int getMoveZ() {
        return moveZ;
    }

    public int getDelay() {
        return delay;
    }

    public boolean getRunning() {
        return running;
    }
}
