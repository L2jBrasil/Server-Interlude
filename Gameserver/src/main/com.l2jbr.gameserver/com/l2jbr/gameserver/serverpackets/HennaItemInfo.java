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


public class HennaItemInfo extends L2GameServerPacket
{
	private static final String _S__E3_HennaItemInfo = "[S] E3 HennaItemInfo";
	
	private final L2PcInstance _activeChar;
	private final Henna _henna;
	
	public HennaItemInfo(Henna henna, L2PcInstance player)
	{
		_henna = henna;
		_activeChar = player;
	}
	
	@Override
	protected final void writeImpl()
	{
		
		writeByte(0xe3);
		writeInt(_henna.getSymbolId()); // symbol Id
		writeInt(_henna.getDyeId()); // item id of dye
		writeInt(_henna.getDyeAmount()); // total amount of dye require
		writeInt(_henna.getPrice()); // total amount of aden require to draw symbol
		writeInt(1); // able to draw or not 0 is false and 1 is true
		writeInt(_activeChar.getAdena());
		
		writeInt(_activeChar.getINT()); // current INT
		writeByte(_activeChar.getINT() + _henna.getStatINT()); // equip INT
		writeInt(_activeChar.getSTR()); // current STR
		writeByte(_activeChar.getSTR() + _henna.getStatSTR()); // equip STR
		writeInt(_activeChar.getCON()); // current CON
		writeByte(_activeChar.getCON() + _henna.getStatCON()); // equip CON
		writeInt(_activeChar.getMEN()); // current MEM
		writeByte(_activeChar.getMEN() + _henna.getStatMEM()); // equip MEM
		writeInt(_activeChar.getDEX()); // current DEX
		writeByte(_activeChar.getDEX() + _henna.getStatDEX()); // equip DEX
		writeInt(_activeChar.getWIT()); // current WIT
		writeByte(_activeChar.getWIT() + _henna.getStatWIT()); // equip WIT
	}

	@Override
	public String getType()
	{
		return _S__E3_HennaItemInfo;
	}
}
