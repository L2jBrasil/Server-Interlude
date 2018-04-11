package com.l2jbr.gameserver.model.dao;

import com.l2jbr.commons.database.dao.DAO;

public class RandomSpawn implements DAO {

    private int groupId;
    private int npcId;
    private int count;
    private int initialDelay;
    private int respawnDelay;
    private int despawnDelay;
    private String broadcastSpawn;
    private String randomSpawn;
}
