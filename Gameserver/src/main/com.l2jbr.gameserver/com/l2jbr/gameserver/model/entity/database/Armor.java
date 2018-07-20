package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.gameserver.model.L2Skill;
import com.l2jbr.gameserver.templates.BodyPart;
import com.l2jbr.gameserver.templates.ItemType;
import com.l2jbr.gameserver.templates.ItemTypeGroup;

@Table("armor")
public class Armor extends ItemTemplate {

    private BodyPart bodyPart;
    @Column("armor_type")
    private ItemType armorType;
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

    @Override
    public void onLoad() {
        super.onLoad();

        switch (bodyPart) {
            case NECK:
            case HAIR:
            case FACE:
            case DHAIR:
            case EAR:
            case FINGER:
                type1 = ItemTypeGroup.TYPE1_WEAPON_ACCESSORY;
                type2 = ItemTypeGroup.TYPE2_ACCESSORY;
                break;
            default:
                type1 = ItemTypeGroup.TYPE1_ARMOR_SHIELD;
                type2 = ItemTypeGroup.TYPE2_SHIELD_ARMOR;
        }

        if(ItemType.PET_ARMOR == armorType) {
            switch (bodyPart) {
                case WOLF:
                    type2 = ItemTypeGroup.TYPE2_PET_WOLF;
                    break;
                case HATCHLING:
                    type2 = ItemTypeGroup.TYPE2_PET_HATCHLING;
                    break;
                case BABYPET:
                    type2 = ItemTypeGroup.TYPE2_PET_BABY;
                    break;
                default:
                    type2 = ItemTypeGroup.TYPE2_PET_STRIDER;
            }
            type1 = ItemTypeGroup.TYPE1_ARMOR_SHIELD;
            bodyPart = BodyPart.CHEST;
        }
    }

    @Override
    public BodyPart getBodyPart() {
        return bodyPart;
    }

    @Override
    public ItemType getType() {
        return armorType;
    }

    @Override
    public boolean isStackable() {
        return false;
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



    public L2Skill getSkill() {
        // todo implement
        return null;
    }

}
