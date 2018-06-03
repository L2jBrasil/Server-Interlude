package com.l2jbr.gameserver.templates;

import com.l2jbr.commons.util.Util;

public enum ItemType {

    //Weapon related
    SHIELD,
    SWORD,
    BLUNT,
    DAGGER,
    BOW,
    POLE,
    ETC,
    FIST,
    DUAL,
    DUAL_FIST,
    BIG_SWORD,
    PET_WEAPON,
    ROD,
    BIG_BLUNT,

    //armor related
    NONE,
    LIGHT,
    HEAVY,
    MAGIC,
    PET_ARMOR,

    //etc item related
    ARROW,
    MATERIAL,
    PET_COLLAR,
    POTION,
    RECIPE,
    SCROLL,
    QUEST,
    MONEY,
    OTHER,
    SPELL_BOOK,
    SEED,
    SHOT,
    HERB,
    CASTLE_GUARD,
    LOTTO,
    RACE_TICKET,
    DYE,
    HARVEST,
    TICKET_OF_LORD,
    LURE;

    public int mask()
    {
        return 1 << (ordinal() +1);
    }

    @Override
    public String toString()
    {
        return Util.capitalize(super.toString().replace('_', ' '));
    }
}
