package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import org.springframework.data.annotation.Id;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

    public boolean containsItemId(int itemId) {
        return items.parallelStream().anyMatch(item -> item.getItemId() == itemId);
    }

    public int getPriceForItemId(int itemId) {
        return getItem(itemId).map(MerchantItem::getPrice).orElse(-1);
    }

    public boolean isLimited(int itemId) {
        return getItem(itemId).map(MerchantItem::isLimited).orElse(false);
    }

    private Optional<MerchantItem> getItem(int itemId) {
        for (MerchantItem item : items) {
            if(item.getItemId() == itemId) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    public void decreaseCount(int itemId, int count) {
        getItem(itemId).ifPresent(item -> item.decreaseCount(count));

    }

    public void changePrice(int itemId, int price) {
        getItem(itemId).ifPresent(item -> item.setPrice(price));
    }

    public void removeItem(int itemId) {
        getItem(itemId).ifPresent(item -> items.remove(item));
    }

    public void addItem(MerchantItem item) {
        items.add(item);
    }

    public Set<MerchantItem> getItems(int start, int end) {
        return items.stream().skip(start).limit(end).collect(Collectors.toSet());
    }
}
