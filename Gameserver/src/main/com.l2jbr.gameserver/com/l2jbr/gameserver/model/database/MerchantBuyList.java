package com.l2jbr.gameserver.model.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.database.model.Entity;
import org.springframework.data.annotation.Id;

@Table("merchant_buylists")
public class MerchantBuyList extends Entity<Integer> {

    @Id
    @Column("shop_id")
    private int shopId;
    @Column("item_id")
    private int itemId;
    private int price;
    private int order;
    private int count;
    private int currentCount;
    private int time;
    private long savetimer;

    public MerchantBuyList() {}

    public MerchantBuyList(int itemId, int price, int shopId, int order) {
        this.itemId =itemId;
        this.price = price;
        this.shopId = shopId;
        this.order = order;
    }

    @Override
    public Integer getId() {
        return shopId;
    }

    public int getItemId() {
        return itemId;
    }

    public int getPrice() {
        return price;
    }

    public int getOrder() {
        return order;
    }

    public int getCount() {
        return count;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public int getTime() {
        return time;
    }

    public long getSavetimer() {
        return savetimer;
    }
}
