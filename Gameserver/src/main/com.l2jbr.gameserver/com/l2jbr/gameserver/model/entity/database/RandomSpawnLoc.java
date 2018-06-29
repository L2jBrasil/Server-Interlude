package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Table;
import org.springframework.data.annotation.Id;

@Table("random_spawn_loc")
public class RandomSpawnLoc  {

    @Id
    private int groupId;
    private int x;
    private int y;
    private int z;
    private int heading;

    public int getGroupId() {
        return groupId;
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
}
