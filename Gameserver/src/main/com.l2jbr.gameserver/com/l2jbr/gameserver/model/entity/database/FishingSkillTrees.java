package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import org.springframework.data.annotation.Id;

@Table("fishing_skill_trees")
public class FishingSkillTrees  {

    @Id
    @Column("skill_id")
    private int skillId;
    private int level;
    private String name;
    private int sp;
    @Column("min_level")
    private int minLevel;
    private int costId;
    private int cost;
    private int isForDwarf;

    public int getSkillId() {
        return skillId;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public int getSp() {
        return sp;
    }

    public int getMinLevel() {
        return minLevel;
    }

    public int getCostId() {
        return costId;
    }

    public int getCost() {
        return cost;
    }

    public int getIsForDwarf() {
        return isForDwarf;
    }
}
