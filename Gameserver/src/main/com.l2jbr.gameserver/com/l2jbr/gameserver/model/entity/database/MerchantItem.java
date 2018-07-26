package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.database.model.Entity;
import org.springframework.data.annotation.Id;

@Table("merchant_buylists")
public class MerchantItem extends Entity<Integer> {

    @Id
    @Column("shop_id")
    private int shopId;
    @Column("item_id")
    private int itemId;
    private int price;
    private int ordering;
    private int count;
    private int currentCount;
    private int time;
    private long savetimer;

    public MerchantItem() {}

    public MerchantItem(int itemId, int price, int shopId, int ordering) {
        this.itemId =itemId;
        this.price = price;
        this.shopId = shopId;
        this.ordering = ordering;
    }

    @Override
    public void onLoad() {
        super.onLoad();

    }

    @Override
    public Integer getId() {
        return shopId;
    }

    public int getItemId() { return itemId; }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price=price;
    }

    public int getOrdering() {
        return ordering;
    }

    public int getCount() { return count; }

    public void setCount(int count) {
        this.count = count;
    }
    public int getCurrentCount() {
        return currentCount;
    }

    public int getTime() {
        return time;
    }

    public long getSavetimer() {  return savetimer; }

    public boolean isLimited() { return count > -1; }

    public void restoreCount() {
        currentCount = count;
    }

    public void decreaseCount(int count) {
       this.currentCount -= count > currentCount  ? currentCount : count;

    }




}
