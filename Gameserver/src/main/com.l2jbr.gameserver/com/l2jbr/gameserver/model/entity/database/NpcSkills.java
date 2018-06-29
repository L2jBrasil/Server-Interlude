package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Table;
import org.springframework.data.annotation.Id;

@Table("npcskills")
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

