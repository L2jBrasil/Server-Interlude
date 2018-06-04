package com.l2jbr.gameserver.templates;

import com.l2jbr.commons.util.Util;

import java.util.EnumSet;
import java.util.Set;

public enum ItemType {

    //Weapon related
    SHIELD(1),
    SWORD(2),
    BLUNT(3),
    DAGGER(4),
    BOW(5),
    POLE(6),
    ETC(7),
    FIST(8),
    DUAL(9),
    DUAL_FIST(10),
    BIG_SWORD(11),
    PET_WEAPON(12),
    ROD(13),
    BIG_BLUNT(14),

    //armor related
    NONE(17),
    LIGHT(18),
    HEAVY(19),
    MAGIC(20),
    PET_ARMOR(21),

    //etc item related
    ARROW(22),
    MATERIAL(23),
    PET_COLLAR(24),
    POTION(25),
    RECIPE(26),
    SCROLL(27),
    QUEST(28),
    MONEY(29),
    OTHER(30),
    SPELL_BOOK(31),
    SEED(32),
    SHOT(33),
    HERB(34),
    CASTLE_GUARD(35),
    LOTTO(36),
    RACE_TICKET(37),
    DYE(38),
    HARVEST(39),
    TICKET_OF_LORD(40),
    LURE(41);

    private static Set<ItemType> weapons = EnumSet.range(SHIELD, BIG_BLUNT);
    private static Set<ItemType> armors = EnumSet.range(NONE, PET_ARMOR);
    private static Set<ItemType> items = EnumSet.range(ARROW, LURE);
    private final int id;

    ItemType(int id) {
        this.id = id;
    }


    public int mask()
    {
        return 1 << id;
    }

    public boolean isWeapon() {
        return weapons.contains(this);
    }

    public static Set<ItemType> weapons() {
        return weapons;
    }
    public static Set<ItemType> items() {
        return items;
    }
    public static Set<ItemType> armors() { return armors; }


    @Override
    public String toString()
    {
        return Util.capitalize(super.toString().replace('_', ' '));
    }

    public String getName() {
        return name().replaceAll("_", "").toLowerCase();
    }
}

