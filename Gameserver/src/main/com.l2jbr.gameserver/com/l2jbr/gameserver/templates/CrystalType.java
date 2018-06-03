package com.l2jbr.gameserver.templates;

public enum CrystalType {
    NONE(0, 0, 0),
    D(1458, 11,  90),
    C(1459, 6, 45),
    B(1460, 11, 67),
    A(1461, 19, 144),
    S(1462, 25, 250);

    private final int enchantAddArmor;
    private final int itemId;
    private final int enchantAddWeapon;

    CrystalType(int id, int enchantAddArmor, int enchantAddWeapon) {
        this.itemId = id;
        this.enchantAddArmor = enchantAddArmor;
        this.enchantAddWeapon = enchantAddWeapon;
    }

    public int getEnchantAddArmor() {
        return enchantAddArmor;
    }

    public int getEnchantAddWeapon() {
        return enchantAddWeapon;
    }

    public int getItemId() {
        return itemId;
    }
}
