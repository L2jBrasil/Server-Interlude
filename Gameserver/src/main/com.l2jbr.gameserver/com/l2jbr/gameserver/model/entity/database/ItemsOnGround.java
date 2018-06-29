package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.database.model.Entity;
import com.l2jbr.gameserver.model.L2ItemInstance;
import org.springframework.data.annotation.Id;

@Table("itemsonground")
public class ItemsOnGround extends Entity<Integer> {

    @Id
    @Column("object_id")
    private int objectId;
    @Column("item_id")
    private int itemId;
    private int count;
    @Column("enchant_level")
    private int enchantLevel;
    private int x;
    private int y;
    private int z;
    @Column("drop_time")
    private long dropTime;
    private int equipable;

    public ItemsOnGround() {}

    public ItemsOnGround(L2ItemInstance item) {
        this.objectId = item.getObjectId();
        this.itemId = item.getItemId();
        this.count = item.getCount();
        this.enchantLevel = item.getEnchantLevel();
        this.x = item.getX();
        this.y = item.getY();
        this.z = item.getZ();
        this.dropTime = item.isProtected() ? -1 : item.getDropTime();
        this.equipable = item.isEquipable() ? 1 : 0;
    }

    @Override
    public Integer getId() {
        return objectId;
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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public long getDropTime() {
        return dropTime;
    }

    public int getEquipable() {
        return equipable;
    }
}
