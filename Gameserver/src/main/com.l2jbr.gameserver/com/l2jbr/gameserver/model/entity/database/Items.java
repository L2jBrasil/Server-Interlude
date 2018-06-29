package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.database.model.Entity;
import org.springframework.data.annotation.Id;

@Table("items")
public class Items extends Entity<Integer> {

    @Id
    @Column("object_id")
    private int objectId;
    @Column("owner_id")
    private int ownerId;
    @Column("item_id")
    private int itemId;
    private int count;
    @Column("enchant_level")
    private int enchantLevel;
    private String loc;
    @Column("loc_data")
    private int locData;
    @Column("price_sell")
    private int priceSell;
    @Column("price_buy")
    private int priceBuy;
    @Column("time_of_use")
    private Integer timeOfUse;
    @Column("custom_type1")
    private int customType1;
    @Column("custom_type2")
    private int customType2;
    @Column("mana_left")
    private int manaLeft;

    public Items() { }

    public Items(int objectId, int ownerId, int itemId, int count, String loc, int locData, int enchantLevel, int priceSell, int priceBuy, int type1, int type2, int mana) {
        this.objectId = objectId;
        this.ownerId = ownerId;
        this.itemId = itemId;
        this.count = count;
        this.loc = loc;
        this.locData = locData;
        this.enchantLevel = enchantLevel;
        this.priceSell = priceSell;
        this.priceBuy = priceBuy;
        this.customType1 = type1;
        this.customType2 = type2;
        this.manaLeft = mana;
    }

    @Override
    public Integer getId() {  return objectId; }

    public int getOwnerId() {
        return ownerId;
    }

    public int getItemId() {
        return itemId;
    }

    public int getCount() {
        return count;
    }

    public int getEnchantLevel() {
        return enchantLevel;
    }

    public String getLoc() {
        return loc;
    }

    public int getLocData() {
        return locData;
    }

    public int getPriceSell() {
        return priceSell;
    }

    public int getPriceBuy() {
        return priceBuy;
    }

    public int getTimeOfUse() {
        return timeOfUse;
    }

    public int getCustomType1() {
        return customType1;
    }

    public int getCustomType2() {
        return customType2;
    }

    public int getManaLeft() {
        return manaLeft;
    }
}
