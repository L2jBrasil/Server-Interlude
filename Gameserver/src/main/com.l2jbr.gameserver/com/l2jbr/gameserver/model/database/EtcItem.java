package com.l2jbr.gameserver.model.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.gameserver.templates.ItemType;
import com.l2jbr.gameserver.templates.ItemTypeGroup;
import com.l2jbr.gameserver.templates.Slot;
import org.springframework.data.annotation.Transient;

@Table("etcitem")
public class EtcItem extends ItemTemplate {

    @Column("item_type")
    private ItemType itemType;
    @Column("consume_type")
    private ConsumeType consumeType;

    @Transient
    private Slot bodyPart = Slot.NONE;
    @Transient
    private boolean stackable = false;

    public EtcItem() {
        type1 = ItemTypeGroup.TYPE1_ITEM_QUEST;
        type2 = ItemTypeGroup.TYPE2_OTHER;
    }

    @Override
    public void onLoad() {
        super.onLoad();

        switch (consumeType) {
            case ASSET:
                itemType = ItemType.MONEY;
                type2 = ItemTypeGroup.TYPE2_MONEY;
            case STACKABLE:
                stackable = true;
        }

        switch (itemType) {
            case ARROW:
            case LURE:
                bodyPart = Slot.LEFT_HAND;
                break;
            case QUEST:
                type2 = ItemTypeGroup.TYPE2_QUEST;
        }
    }

    @Override
    public ItemType getType() { return itemType; }

    @Override
    public Slot getBodyPart() {
        return bodyPart;
    }

    @Override
    public boolean isStackable() {
        return stackable;
    }

    enum ConsumeType {
        NORMAL,
        STACKABLE,
        ASSET;
    }
}
