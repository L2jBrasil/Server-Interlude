package com.l2jbr.gameserver.model.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.database.model.Entity;
import org.springframework.data.annotation.Id;

@Table("armor")
public class Armor extends Entity<Integer> {

    @Id
    @Column("item_id")
    private int itemId;
    private String name;
    private String bodyPart;
    private String crystallizable;
    @Column("armor_type")
    private String armorType;
    private int weight;
    private String material;
    @Column("crystal_type")
    private String crystalType;
    @Column("avoid_modify")
    private int avoidModify;
    private int duration;
    @Column("p_def")
    private int pDef;
    @Column("m_def")
    private int mDef;
    @Column("mp_bonus")
    private int mpBonus;
    private int price;
    @Column("crystal_count")
    private int crystalCount;
    private String sellable;
    private String dropable;
    private String destroyable;
    private String tradeable;
    @Column("item_skill_id")
    private int itemSkillId;
    @Column("item_skill_lvl")
    private int itemSkillLvl;

    public String getArmorType() {
        return armorType;
    }

    @Override
    public Integer getId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public String getBodyPart() {
        return bodyPart;
    }

    public String getCrystallizable() {
        return crystallizable;
    }

    public int getCrystalCount() {
        return crystalCount;
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

    public int getWeight() {
        return weight;
    }

    public String getMaterial() {
        return material;
    }

    public String getCrystalType() {
        return crystalType;
    }

    public int getAvoidModify() {
        return avoidModify;
    }

    public int getDuration() {
        return duration;
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

    public int getPrice() {
        return price;
    }
}
