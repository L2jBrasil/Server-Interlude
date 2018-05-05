package com.l2jbr.gameserver.model.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import org.springframework.data.annotation.Id;

import java.util.Set;

@Table("random_spawn")
public class RandomSpawn  {

    @Id
    private int groupId;
    private int npcId;
    private int count;
    private int initialDelay;
    private int respawnDelay;
    private int despawnDelay;
    private String broadcastSpawn;
    private String randomSpawn;
    @Column("groupId")
    private Set<RandomSpawnLoc> locs;

    public int getGroupId() {
        return groupId;
    }

    public int getNpcId() {
        return npcId;
    }

    public int getCount() {
        return count;
    }

    public int getInitialDelay() {
        return initialDelay;
    }

    public int getRespawnDelay() {
        return respawnDelay;
    }

    public int getDespawnDelay() {
        return despawnDelay;
    }

    public String getBroadcastSpawn() {
        return broadcastSpawn;
    }

    public String getRandomSpawn() {
        return randomSpawn;
    }

    public Set<RandomSpawnLoc> getLocs() {
        return locs;
    }
}
