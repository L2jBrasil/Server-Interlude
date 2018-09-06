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

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class WearList extends L2GameServerPacket {

	private static final String _S__EF_WEARLIST = "[S] EF WearList";
	private final int _money;
	private final MerchantShop shop;
	private int _expertise;
	
	public WearList(MerchantShop list, int currentMoney, int expertiseIndex) {
		this.shop = list;
		_money = currentMoney;
		_expertise = expertiseIndex;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeByte(0xef);
		writeByte(0xc0); // ?
		writeByte(0x13); // ?
		writeByte(0x00); // ?
		writeByte(0x00); // ?
		writeInt(_money); // current money
		writeInt(shop.getId());

        Set<MerchantItem> wearList = shop.getItems().stream().filter(this::showItem).collect(Collectors.toSet());

		writeShort(wearList.size());
		
		for (MerchantItem item : wearList) {
		    ItemTemplate template = ItemTable.getInstance().getTemplate(item.getItemId());
            writeInt(item.getItemId());
            writeShort(template.getType2().getId()); // item type2

            if (template.getType1() != ItemTypeGroup.TYPE1_ITEM_QUEST) {
                writeShort(template.getBodyPart().getId()); // rev 415 slot 0006-lr.ear 0008-neck 0030-lr.finger 0040-head 0080-?? 0100-l.hand 0200-gloves 0400-chest 0800-pants 1000-feet 2000-?? 4000-r.hand 8000-r.hand
            } else {
                writeShort(0x00); // rev 415 slot 0006-lr.ear 0008-neck 0030-lr.finger 0040-head 0080-?? 0100-l.hand 0200-gloves 0400-chest 0800-pants 1000-feet 2000-?? 4000-r.hand 8000-r.hand
            }
            writeInt(Config.WEAR_PRICE);
		}
	}

    private boolean showItem(MerchantItem merchantItem) {
        ItemTemplate template = ItemTable.getInstance().getTemplate(merchantItem.getItemId());
        return nonNull(template) && template.isEquipable() && template.getCrystalType().ordinal() <= _expertise;
    }

	@Override
	public String getType()
	{
		return _S__EF_WEARLIST;
	}
}
