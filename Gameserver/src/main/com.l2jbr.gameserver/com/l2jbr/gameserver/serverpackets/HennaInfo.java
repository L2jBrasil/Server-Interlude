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


public final class HennaInfo extends L2GameServerPacket
{
	private static final String _S__E4_HennaInfo = "[S] E4 HennaInfo";
	
	private final L2PcInstance _activeChar;
	private final Henna[] _hennas = new Henna[3];
	private final int _count;
	
	public HennaInfo(L2PcInstance player)
	{
		_activeChar = player;
		
		int j = 0;
		for (int i = 0; i < 3; i++)
		{
			Henna h = _activeChar.getHenna(i + 1);
			if (h != null)
			{
				_hennas[j++] = h;
			}
		}
		_count = j;
	}
	
	@Override
	protected final void writeImpl()
	{
		
		writeByte(0xe4);
		
		writeByte(_activeChar.getHennaStatINT()); // equip INT
		writeByte(_activeChar.getHennaStatSTR()); // equip STR
		writeByte(_activeChar.getHennaStatCON()); // equip CON
		writeByte(_activeChar.getHennaStatMEN()); // equip MEM
		writeByte(_activeChar.getHennaStatDEX()); // equip DEX
		writeByte(_activeChar.getHennaStatWIT()); // equip WIT
		
		writeInt(3); // slots?
		
		writeInt(_count); // size
		for (int i = 0; i < _count; i++)
		{
			writeInt(_hennas[i].getSymbolId());
			writeInt(_hennas[i].getSymbolId());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jbr.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__E4_HennaInfo;
	}
}
