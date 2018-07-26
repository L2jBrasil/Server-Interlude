package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.gameserver.datatables.SkillTable;
import com.l2jbr.gameserver.model.L2Skill;
import com.l2jbr.gameserver.templates.BodyPart;
import com.l2jbr.gameserver.templates.ItemType;
import com.l2jbr.gameserver.templates.ItemTypeGroup;
import org.springframework.data.annotation.Transient;

import static java.util.Objects.isNull;

@Table("armor")
public class Armor extends ItemTemplate {

    private BodyPart bodyPart;
    @Column("armor_type")
    private ItemType armorType;
    @Column("item_skill_id")
    private int itemSkillId;
    @Column("item_skill_lvl")
    private int itemSkillLvl;

    @Transient private L2Skill skill;

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

        if(! isNull(itemSkillId)) {
            skill = SkillTable.getInstance().getInfo(itemSkillId, itemSkillLvl);
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

    @Override
    public boolean isEquipable() {
        return BodyPart.NONE != bodyPart;
    }

    public L2Skill getSkill() {
        return skill;
    }

}
