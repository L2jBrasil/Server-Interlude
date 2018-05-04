package com.l2jbr.gameserver.model.database;

import org.springframework.data.annotation.Id;

public class NpcSkills {

    @Id
    private int npcid;
    private int skillid;
    private int level;

    public int getNpcid() {
        return npcid;
    }

    public int getSkillid() {
        return skillid;
    }

    public int getLevel() {
        return level;
    }
}

