package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import org.springframework.data.annotation.Id;

@Table("helper_buff_list")
public class HelperBuff {

    @Id
    private int id;
    @Column("skill_id")
    private int skillId;
    private String name;
    @Column("skill_level")
    private int skillLevel;
    @Column("lower_level")
    private int lowerLevel;
    @Column("upper_level")
    private int upperLevel;
    @Column("is_magic_class")
    private boolean isMagicClass;

    public int getId() {
        return id;
    }

    public int getSkillId() {
        return skillId;
    }

    public String getName() {
        return name;
    }

    public int getSkillLevel() {
        return skillLevel;
    }

    public int getLowerLevel() {
        return lowerLevel;
    }

    public int getUpperLevel() {
        return upperLevel;
    }

    public boolean isMagicClass() { return isMagicClass; }
}
