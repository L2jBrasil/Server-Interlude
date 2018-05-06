package com.l2jbr.gameserver.model.database;

import com.l2jbr.commons.database.annotation.Table;
import org.springframework.data.annotation.Id;

@Table("etcitem")
public class EtcItem  {
    @Id
    private int item_id;
    private String name;
    private String crystallizable;
    private String item_type;
    private int weight;
    private String consume_type;
    private String material;
    private String crystal_type;
    private int duration;
    private int price;
    private int crystal_count;
    private String sellable;
    private String dropable;
    private String destroyable;
    private String tradeable;
    private String oldname;
    private String oldtype;

    public int getItemId() {
        return item_id;
    }

    public String getName() {
        return name;
    }

    public String getCrystallizable() {
        return crystallizable;
    }

    public String getItemType() {
        return item_type;
    }

    public int getWeight() {
        return weight;
    }

    public String getConsumeType() {
        return consume_type;
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

    public String getOldname() {
        return oldname;
    }

    public String getOldtype() {
        return oldtype;
    }
}
