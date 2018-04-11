package com.l2jbr.gameserver.model.dao;

import com.l2jbr.commons.database.dao.DAO;

public class Spawnlist implements DAO {
    private int id;
    private String location;
    private int count;
    private int npc_templateid;
    private int locx;
    private int locy;
    private int locz;
    private int randomx;
    private int randomy;
    private int heading;
    private int respawn_delay;
    private int loc_id;
    private int periodOfDay;
}
