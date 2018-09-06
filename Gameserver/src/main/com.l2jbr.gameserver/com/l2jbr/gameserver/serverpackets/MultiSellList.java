/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jbr.gameserver.serverpackets;

import com.l2jbr.gameserver.datatables.ItemTable;
import com.l2jbr.gameserver.model.L2Multisell.MultiSellEntry;
import com.l2jbr.gameserver.model.L2Multisell.MultiSellIngredient;
import com.l2jbr.gameserver.model.L2Multisell.MultiSellListContainer;


/**
 * This class ...
 * @version $Revision: 1.2 $ $Date: 2004/06/27 08:12:59 $
 */
public class MultiSellList extends L2GameServerPacket
{
	private static final String _S__D0_MULTISELLLIST = "[S] D0 MultiSellList";
	
	protected int _listId, _page, _finished;
	protected MultiSellListContainer _list;
	
	public MultiSellList(MultiSellListContainer list, int page, int finished)
	{
		_list = list;
		_listId = list.getListId();
		_page = page;
		_finished = finished;
	}
	
	@Override
	protected void writeImpl()
	{
		// [ddddd] [dchh] [hdhdh] [hhdh]
		
		writeByte(0xd0);
		writeInt(_listId); // list id
		writeInt(_page); // page
		writeInt(_finished); // finished
		writeInt(0x28); // size of pages
		writeInt(_list == null ? 0 : _list.getEntries().size()); // list lenght
		
		if (_list != null)
		{
			for (MultiSellEntry ent : _list.getEntries())
			{
				writeInt(ent.getEntryId());
				writeInt(0x00); // C6
				writeInt(0x00); // C6
				writeByte(1);
				writeShort(ent.getProducts().size());
				writeShort(ent.getIngredients().size());
				
				for (MultiSellIngredient i : ent.getProducts())
				{
					writeShort(i.getItemId());
					writeInt(0);
					writeShort(ItemTable.getInstance().getTemplate(i.getItemId()).getType2().getId());
					writeInt(i.getItemCount());
					writeShort(i.getEnchantmentLevel()); // enchtant lvl
					writeInt(0x00); // C6
					writeInt(0x00); // C6
				}
				
				for (MultiSellIngredient i : ent.getIngredients())
				{
					int items = i.getItemId();
					int typeE = 65535;
					if (items != 65336)
					{
						typeE = ItemTable.getInstance().getTemplate(i.getItemId()).getType2().getId();
					}
					writeShort(items); // ID
					writeShort(typeE);
					writeInt(i.getItemCount()); // Count
					writeShort(i.getEnchantmentLevel()); // Enchant Level
					writeInt(0x00); // C6
					writeInt(0x00); // C6
				}
			}
		}
	}
	
	@Override
	public String getType()
	{
		return _S__D0_MULTISELLLIST;
	}
	
}
