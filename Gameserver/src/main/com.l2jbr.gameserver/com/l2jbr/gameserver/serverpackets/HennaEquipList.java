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

import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.model.entity.database.Henna;

import java.util.List;


public class HennaEquipList extends L2GameServerPacket
{
	private static final String _S__E2_HennaEquipList = "[S] E2 HennaEquipList";
	
	private final L2PcInstance _player;
	private final List<Henna> _hennaEquipList;
	
	public HennaEquipList(L2PcInstance player, List<Henna> hennaEquipList)
	{
		_player = player;
		_hennaEquipList = hennaEquipList;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeByte(0xe2);
		writeInt(_player.getAdena()); // activeChar current amount of aden
		writeInt(3); // available equip slot
		// writeInt(10); // total amount of symbol available which depends on difference classes
		writeInt(_hennaEquipList.size());
		
		for (Henna element : _hennaEquipList) {
			/*
			 * Player must have at least one dye in inventory to be able to see the henna that can be applied with it.
			 */
			if ((_player.getInventory().getItemByItemId(element.getDyeId())) != null)
			{
				writeInt(element.getSymbolId()); // symbolid
				writeInt(element.getDyeId()); // itemid of dye
				writeInt(element.getDyeAmount()); // amount of dye require
				writeInt(element.getPrice()); // amount of aden require
				writeInt(1); // meet the requirement or not
			}
			else
			{
				writeInt(0x00);
				writeInt(0x00);
				writeInt(0x00);
				writeInt(0x00);
				writeInt(0x00);
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
		return _S__E2_HennaEquipList;
	}
	
}
