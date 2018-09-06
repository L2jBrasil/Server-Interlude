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

import com.l2jbr.commons.Config;
import com.l2jbr.gameserver.datatables.ItemTable;
import com.l2jbr.gameserver.model.entity.database.ItemTemplate;
import com.l2jbr.gameserver.model.entity.database.MerchantItem;
import com.l2jbr.gameserver.model.entity.database.MerchantShop;
import com.l2jbr.gameserver.templates.ItemTypeGroup;

import static java.util.Objects.isNull;


/**
 * sample 1d 1e 00 00 00 // ?? 5c 4a a0 7c // buy list id 02 00 // item count 04 00 // itemType1 0-weapon/ring/earring/necklace 1-armor/shield 4-item/questitem/adena 00 00 00 00 // objectid 32 04 00 00 // itemid 00 00 00 00 // count 05 00 // itemType2 0-weapon 1-shield/armor 2-ring/earring/necklace
 * 3-questitem 4-adena 5-item 00 00 60 09 00 00 // price 00 00 00 00 00 00 b6 00 00 00 00 00 00 00 00 00 00 00 80 00 // body slot these 4 values are only used if itemtype1 = 0 or 1 00 00 // 00 00 // 00 00 // 50 c6 0c 00 format dd h (h dddhh hhhh d) revision 377 format dd h (h dddhh dhhh d) revision
 * 377
 */
public final class BuyList extends L2GameServerPacket
{
	private static final String _S__1D_BUYLIST = "[S] 11 BuyList";
	private final MerchantShop shop;
	private final int _money;
	private double _taxRate;
	
	public BuyList(MerchantShop shop, int currentMoney) {
        this(shop, currentMoney, 0);
    }
	
	public BuyList(MerchantShop shop, int currentMoney, double taxRate) {
		this.shop = shop;
		_money = currentMoney;
		_taxRate = taxRate;
	}

	@Override
	protected final void writeImpl()
	{
		writeByte(0x11);
		writeInt(_money); // current money
		writeInt(shop.getId());
		
		writeShort(shop.getItems().size());
		
		for (MerchantItem item : shop.getItems()) {

			if ((item.getCount() > 0) || (item.getCount() == -1)) {
                ItemTemplate template = ItemTable.getInstance().getTemplate(item.getItemId());
                if(isNull(template)) {
                    continue;
                }

				writeShort(template.getType1().getId()); // item type1
				writeInt(0);
				writeInt(item.getItemId());
				if (item.getCount() < 0)
				{
					writeInt(0x00); // max amount of items that a player can buy at a time (with this itemid)
				}
				else
				{
					writeInt(item.getCount());
				}
				writeShort(template.getType2().getId()); // item type2
				writeShort(0x00); // ?
				
				if (template.getType1() != ItemTypeGroup.TYPE1_ITEM_QUEST)
				{
					writeInt(template.getBodyPart().getId()); // rev 415 slot 0006-lr.ear 0008-neck 0030-lr.finger 0040-head 0080-?? 0100-l.hand 0200-gloves 0400-chest 0800-pants 1000-feet 2000-?? 4000-r.hand 8000-r.hand
					writeShort(0); // enchant level
					writeShort(0x00); // ?
					writeShort(0x00);
				}
				else
				{
					writeInt(0x00); // rev 415 slot 0006-lr.ear 0008-neck 0030-lr.finger 0040-head 0080-?? 0100-l.hand 0200-gloves 0400-chest 0800-pants 1000-feet 2000-?? 4000-r.hand 8000-r.hand
					writeShort(0x00); // enchant level
					writeShort(0x00); // ?
					writeShort(0x00);
				}
				
				if ((item.getItemId() >= 3960) && (item.getItemId() <= 4026))
				{
					writeInt((int) (item.getPrice() * Config.RATE_SIEGE_GUARDS_PRICE * (1 + _taxRate)));
				}
				else
				{
					writeInt((int) (item.getPrice() * (1 + _taxRate)));
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.l2jbr.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__1D_BUYLIST;
	}
}
