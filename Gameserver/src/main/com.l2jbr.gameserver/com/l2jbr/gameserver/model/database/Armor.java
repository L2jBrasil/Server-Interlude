package com.l2jbr.gameserver.model.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;

@Table("armor")
public class Armor extends ItemTemplate {

    private String bodyPart;
    @Column("armor_type")
    private String armorType;
    @Column("avoid_modify")
    private int avoidModify;
    @Column("p_def")
    private int pDef;
    @Column("m_def")
    private int mDef;
    @Column("mp_bonus")
    private int mpBonus;
    @Column("item_skill_id")
    private int itemSkillId;
    @Column("item_skill_lvl")
    private int itemSkillLvl;

    public String getArmorType() {
        return armorType;
    }

    public String getBodyPart() {
        return bodyPart;
    }

    public int getItemSkillId() {
        return itemSkillId;
    }

    public int getItemSkillLevel() {
        return itemSkillLvl;
    }

    public int getAvoidModify() {
        return avoidModify;
    }

    public int getPdef() {
        return pDef;
    }

    public int getMdef() {
        return mDef;
    }

    public int getMpBonus() {
        return mpBonus;
    }

}
