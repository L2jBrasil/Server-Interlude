package com.l2jbr.gameserver.model.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import org.springframework.data.annotation.Id;

@Table("armorsets")
public class ArmorSet {

    @Id
    private Integer id;
    private int chest;
    private int legs;
    private int head;
    private int gloves;
    private int feet;
    @Column("skill_id")
    private int skillId;
    private int shield;
    @Column("shield_skill_id")
    private int shieldSkillId;
    private int enchant6skill;

    public int getChest() {
        return chest;
    }

    public int getLegs() {
        return legs;
    }

    public int getHead() {
        return head;
    }

    public int getGloves() {
        return gloves;
    }

    public int getFeet() {
        return feet;
    }

    public int getSkillId() {
        return skillId;
    }

    public int getShield() {
        return shield;
    }

    public int getShieldSkillId() {
        return shieldSkillId;
    }

    public int getEnchant6Skill() {
        return enchant6skill;
    }
}
