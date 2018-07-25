package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import org.springframework.data.annotation.Id;

import java.util.Set;

@Table("merchant_shopids")
public class MerchantShop {

    @Id
    @Column("shop_id")
    private int id;
    @Column("npc_id")
    private String npcId;
    @Column("shop_id")
    private Set<MerchantItem> items;

    public int getId() {
        return id;
    }

    public String getNpcId() {
        return npcId;
    }

    public Set<MerchantItem> getItems() {
        return items;
    }
}
