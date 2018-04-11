package com.l2jbr.gameserver.model.dao;

import com.l2jbr.commons.database.dao.DAO;

public class RaidbossSpawnList implements DAO {
    private int boss_id;
    private int amount;
    private int loc_x;
    private int loc_y;
    private int loc_z;
    private int heading;
    private int respawn_min_delay;
    private int respawn_max_delay;
    private int respawn_time;
    private int currentHp;
    private int currentMp;
}
