package com.l2jbr.gameserver.model.database;

import com.l2jbr.commons.database.annotation.Column;
import org.springframework.data.annotation.Id;

public class ItemTemplate {

    @Id
    @Column("item_id")
    private int id;
    private String name;
    private String crystallizable;
    private int weight;
    private String material;
    private String crystal_type;
    private int duration;
    private int price;
    private int crystal_count;
    private String sellable;
    private String dropable;
    private String destroyable;
    private String tradeable;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCrystallizable() {
        return crystallizable;
    }

    public int getWeight() {
        return weight;
    }

    public String getMaterial() {
        return material;
    }

    public String getCrystalType() {
        return crystal_type;
    }

    public int getDuration() {
        return duration;
    }

    public int getPrice() {
        return price;
    }

    public int getCrystalCount() {
        return crystal_count;
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
}
