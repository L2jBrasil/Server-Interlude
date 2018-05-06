package com.l2jbr.gameserver.model.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import org.springframework.data.annotation.Id;

@Table("fish")
public class Fish  {
    @Id
    private int id;
    private int level;
    private String name;
    private int hp;
    private int hpregen;
    @Column("fish_type")
    private int fishType;
    @Column("fish_group")
    private int fishGroup;
    @Column("fish_guts")
    private int fishGuts;
    @Column("guts_check_time")
    private int gutsCheckTime;
    @Column("wait_time")
    private int waitTime;
    @Column("combat_time")
    private int combatTime;

    public int getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getHpregen() {
        return hpregen;
    }

    public int getFishType() {
        return fishType;
    }

    public int getFishGroup() {
        return fishGroup;
    }

    public int getFishGuts() {
        return fishGuts;
    }

    public int getGutsCheckTime() {
        return gutsCheckTime;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public int getCombatTime() {
        return combatTime;
    }
}
