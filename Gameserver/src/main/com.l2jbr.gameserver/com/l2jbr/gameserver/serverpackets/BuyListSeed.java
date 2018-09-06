/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package com.l2jbr.gameserver.serverpackets;

import com.l2jbr.gameserver.model.entity.database.MerchantItem;
import com.l2jbr.gameserver.model.entity.database.MerchantShop;


/**
 * Format: c ddh[hdddhhd] c - id (0xE8) d - money d - manor id h - size [ h - item type 1 d - object id d - item id d - count h - item type 2 h d - price ]
 *
 * @author l3x
 */

public final class BuyListSeed extends L2GameServerPacket {
    private static final String _S__E8_BUYLISTSEED = "[S] E8 BuyListSeed";

    private final int _manorId;
    private final int _money;
    private final MerchantShop shop;

    public BuyListSeed(MerchantShop shop, int manorId, int currentMoney) {
        this.shop = shop;
        _money = currentMoney;
        _manorId = manorId;
    }

    @Override
    protected final void writeImpl() {
        writeByte(0xE8);

        writeInt(_money); // current money
        writeInt(_manorId); // manor id

        writeShort(shop.getItems().size()); // list length

        for (MerchantItem item : shop.getItems()) {
            writeShort(0x04); // item->type1
            writeInt(0x00); // objectId
            writeInt(item.getItemId()); // item id
            writeInt(item.getCount()); // item count
            writeShort(0x04); // item->type2
            writeShort(0x00); // unknown :)
            writeInt(item.getPrice()); // price
        }
    }

    @Override
    public String getType() {
        return _S__E8_BUYLISTSEED;
    }
}