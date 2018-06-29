package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import org.springframework.data.annotation.Id;

@Table("dimensional_rift")
public class DimensionalRift  {
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
    private boolean boss;

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

    public int getxT() {
        return xT;
    }

    public int getyT() {
        return yT;
    }

    public int getzT() {
        return zT;
    }

    public boolean getBoss() {
        return boss;
    }
}
