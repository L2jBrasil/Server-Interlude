package com.l2jbr.gameserver.model.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;

@Table("weapon")
public class Weapon extends ItemTemplate {

    private String bodypart;
    private Integer soulshots;
    private Integer spiritshots;
    @Column("p_dam")
    private Integer pDam;
    @Column("rnd_dam")
    private Integer rndDam;
    @Column("weaponType")
    private String type;
    private Integer critical;
    @Column("hit_modify")
    private Double hitModify;
    @Column("avoid_modify")
    private Integer avoidModify;
    @Column("shield_def")
    private Integer shieldDef;
    @Column("shield_def_rate")
    private Integer shieldDefRate;
    @Column("atk_speed")
    private Integer atkSpeed;
    @Column("mp_consume")
    private Integer mpConsume;
    @Column("m_dam")
    private Integer mDam;
    @Column("item_skill_id")
    private Integer itemSkillId;
    @Column("item_skill_lvl")
    private Integer itemSkillLvl;
    @Column("enchant4_skill_id")
    private Integer enchant4SkillId;
    @Column("enchant4_skill_lvl")
    private Integer enchant4SkillLvl;
    @Column("onCast_skill_id")
    private Integer onCastSkillId;
    @Column("onCast_skill_lvl")
    private Integer onCastSkillLvl;
    @Column("onCast_skill_chance")
    private Integer onCastSkillChance;
    @Column("onCrit_skill_id")
    private Integer onCritSkillId;
    @Column("onCrit_skill_lvl")
    private Integer onCritSkillLvl;
    @Column("onCrit_skill_chance")
    private Integer onCritSkillChance;

    public String getType() {
        return type;
    }

    public String getBodyPart() {
        return bodypart;
    }

    public int getSoulshots() {
        return soulshots;
    }

    public int getSpiritshots() {
        return spiritshots;
    }

    public int getPDam() {
        return pDam;
    }

    public int getRndDam() {
        return rndDam;
    }

    public int getCritical() {
        return critical;
    }

    public double getHitModify() {
        return hitModify;
    }

    public int getAvoidModify() {
        return avoidModify;
    }

    public int getShieldDef() {
        return shieldDef;
    }

    public int getShieldDefRate() {
        return shieldDefRate;
    }

    public int getAtkSpeed() {
        return atkSpeed;
    }

    public int getMpConsume() {
        return mpConsume;
    }

    public int getMDam() {
        return mDam;
    }

    public int getItemSkillId() {
        return itemSkillId;
    }

    public int getItemSkillLevel() {
        return itemSkillLvl;
    }

    public int getEnchant4SkillId() {
        return enchant4SkillId;
    }

    public int getEnchant4SkillLevel() {
        return enchant4SkillLvl;
    }

    public int getOnCastSkillId() {
        return onCastSkillId;
    }

    public int getOnCastSkillLevel() {
        return onCastSkillLvl;
    }

    public int getOnCastSkillChance() {
        return onCastSkillChance;
    }

    public int getOnCritSkillId() {
        return onCritSkillId;
    }

    public int getOnCritSkillLevel() {
        return onCritSkillLvl;
    }

    public int getOnCritSkillChance() {
        return onCritSkillChance;
    }
}
