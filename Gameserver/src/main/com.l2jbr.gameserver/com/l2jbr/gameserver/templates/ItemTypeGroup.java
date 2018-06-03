package com.l2jbr.gameserver.templates;

public enum ItemTypeGroup {
    TYPE1_WEAPON_ACCESSORY(0),
    TYPE1_ARMOR_SHIELD(1),
    TYPE1_ITEM_QUEST(4),

    TYPE2_WEAPON(0),
    TYPE2_SHIELD_ARMOR(1),
    TYPE2_ACCESSORY(2),
    TYPE2_QUEST(3),
    TYPE2_MONEY(4),
    TYPE2_OTHER (5),
    TYPE2_PET_WOLF(6),
    TYPE2_PET_HATCHLING(7),
    TYPE2_PET_STRIDER(8),
    TYPE2_PET_BABY(9);

    private final int id;

    ItemTypeGroup(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
