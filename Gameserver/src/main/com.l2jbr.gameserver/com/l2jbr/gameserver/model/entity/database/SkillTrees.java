package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import org.springframework.data.annotation.Id;

@Table("skill_trees")
public class SkillTrees  {

    @Id
    @Column("class_id")
    private int classId;
    @Column("skill_id")
    private int skillId;
    private int level;
    private String name;
    private int sp;
    @Column("min_level")
    private int minLevel;

    public int getClassId() {
        return classId;
    }

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
}
