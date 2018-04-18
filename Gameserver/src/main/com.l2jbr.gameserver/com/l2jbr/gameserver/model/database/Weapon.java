package com.l2jbr.gameserver.model.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.model.Entity;

public class Weapon extends Entity<Integer> {

    @Column("item_id")
    private Integer itemId;
    private String name;
    private String bodypart;
    private String crystallizable;
    private Integer weight;
    private Integer soulshots;
    private Integer spiritshots;
    private String material;
    @Column("crystal_type")
    private String crystalType;
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
    private Integer duration;
    private Integer price;
    @Column("crystal_count")
    private Integer crystal_count;
    private String sellable;
    private String dropable;
    private String destroyable;
    private String tradeable;
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

    @Override
    public Integer getId() {
        return itemId;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getBodyPart() {
        return bodypart;
    }

    public String getMaterial() {
        return material;
    }

    public String getCrystalType() {
        return crystalType;
    }

    public String getCrystallizable() {
        return crystallizable;
    }

    public int getWeight() {
        return weight;
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

    public int getDuration() {
        return duration;
    }

    public int getPrice() {
        return price;
    }

    public int getCrystalCount() {
        return crystal_count;
    }

    public String getSellable() {
        return sellable;
    }

    public String getDropable() {
        return dropable;
    }

    public String getDestroyable() {
        return destroyable;
    }

    public String getTradeable() {
        return tradeable;
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
