package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.gameserver.model.Inventory;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import org.springframework.data.annotation.Id;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

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

    public boolean containItem(int slot, int itemId) {
        switch (slot) {
            case Inventory.PAPERDOLL_CHEST:
                return chest == itemId;
            case Inventory.PAPERDOLL_LEGS:
                return legs == itemId;
            case Inventory.PAPERDOLL_HEAD:
                return head == itemId;
            case Inventory.PAPERDOLL_GLOVES:
                return gloves == itemId;
            case Inventory.PAPERDOLL_FEET:
                return feet == itemId;
            default:
                return false;
        }
    }

    public boolean containAll(L2PcInstance player)  {
        var inventory = player.getInventory();

        var legsItem = inventory.getPaperdollItem(Inventory.PAPERDOLL_LEGS);
        var headItem = inventory.getPaperdollItem(Inventory.PAPERDOLL_HEAD);
        var glovesItem = inventory.getPaperdollItem(Inventory.PAPERDOLL_GLOVES);
        var feetItem = inventory.getPaperdollItem(Inventory.PAPERDOLL_FEET);

        var legs = isNull(legsItem) ?  0 : legsItem.getItemId();
        var head = isNull(headItem) ?  0 : headItem.getItemId();
        var gloves = isNull(glovesItem) ?  0 : glovesItem.getItemId();
        var feet = isNull(feetItem) ?  0 : feetItem.getItemId();

        return containAll(chest, legs, head, gloves, feet);
    }

    public boolean containAll(int chest, int legs, int head, int gloves, int feet) {
        var result = (this.chest == chest);
        result &= (this.legs == legs);
        result &= (this.feet == feet);
        result &= (this.head == head);
        result &= (this.gloves == gloves);
        return result;
    }

    public boolean containShield(L2PcInstance player) {
        var shieldItem = player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_LHAND);
        return nonNull(shieldItem) && shieldItem.getItemId() == shield;
    }

    public boolean containShield(int shieldId) {
        return shieldId == shield;
    }

    public boolean isEnchanted6(L2PcInstance player) {
        if (!containAll(player)) {
            return false;
        }

        var inventory = player.getInventory();

        var chestItem = inventory.getPaperdollItem(Inventory.PAPERDOLL_CHEST);
        var legsItem = inventory.getPaperdollItem(Inventory.PAPERDOLL_LEGS);
        var headItem = inventory.getPaperdollItem(Inventory.PAPERDOLL_HEAD);
        var glovesItem = inventory.getPaperdollItem(Inventory.PAPERDOLL_GLOVES);
        var feetItem = inventory.getPaperdollItem(Inventory.PAPERDOLL_FEET);

        var result = chestItem.getEnchantLevel() >= 6;
        result &= (legs == 0 || legsItem.getEnchantLevel() >= 6);
        result &= (head == 0 || headItem.getEnchantLevel() >= 6);
        result &= (gloves == 0 || glovesItem.getEnchantLevel() >= 6);
        result &= (feet == 0 || feetItem.getEnchantLevel() >= 6);
        return result;
    }
}
