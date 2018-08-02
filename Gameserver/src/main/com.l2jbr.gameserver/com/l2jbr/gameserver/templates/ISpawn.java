package com.l2jbr.gameserver.templates;

import com.l2jbr.gameserver.model.entity.database.NpcTemplate;

public interface ISpawn {
    Integer getId();

    void setId(int id);

    int getX();

    void setX(int locx);

    int getY();

    void setY(int locy);

    int getZ();

    void setZ(int locz);

    int getCount();

    void setCount(int amount);

    int getRespawnDelay();

    void setRespawnDelay(int delay);

    int getHeading();

    void setHeading(int heading);

    default int getLocId() {
        return 0;
    }

    default void setLocId(int location) {

    }

    NpcTemplate getNpcTemplate();

    default int getRespawnMinDelay() {
        return 0;
    }

    default int getRespawnMaxDelay() {
        return  0;
    }
}
