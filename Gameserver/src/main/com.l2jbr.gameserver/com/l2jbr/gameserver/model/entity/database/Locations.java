package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import org.springframework.data.annotation.Id;

@Table("location")
public class Locations  {
    @Id
    @Column("loc_id")
    private int locId;
    @Column("loc_x")
    private int locX;
    @Column("loc_y")
    private int locY;
    @Column("loc_zmin")
    private int locZMin;
    @Column("loc_zmax")
    private int locZMax;
    private int proc;

    public int getLocId() {
        return locId;
    }

    public int getLocX() {
        return locX;
    }

    public int getLocY() {
        return locY;
    }

    public int getLocZMin() {
        return locZMin;
    }

    public int getLocZMax() {
        return locZMax;
    }

    public int getProc() {
        return proc;
    }
}
