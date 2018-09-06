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

import com.l2jbr.gameserver.GameTimeController;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;


/**
 * This class ...
 * @version $Revision: 1.4.2.5.2.6 $ $Date: 2005/03/27 15:29:39 $
 */
public class CharSelected extends L2GameServerPacket
{
	// SdSddddddddddffddddddddddddddddddddddddddddddddddddddddd d
	private static final String _S__21_CHARSELECTED = "[S] 15 CharSelected";
	private final L2PcInstance _activeChar;
	private final int _sessionId;
	
	/**
	 * @param cha
	 * @param sessionId
	 */
	public CharSelected(L2PcInstance cha, int sessionId)
	{
		_activeChar = cha;
		_sessionId = sessionId;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeByte(0x15);
		
		writeString(_activeChar.getName());
		writeInt(_activeChar.getCharId()); // ??
		writeString(_activeChar.getTitle());
		writeInt(_sessionId);
		writeInt(_activeChar.getClanId());
		writeInt(0x00); // ??
		writeInt(_activeChar.getAppearance().getSex() ? 1 : 0);
		writeInt(_activeChar.getRace().ordinal());
		writeInt(_activeChar.getPlayerClass().getId());
		writeInt(0x01); // active ??
		writeInt(_activeChar.getX());
		writeInt(_activeChar.getY());
		writeInt(_activeChar.getZ());
		
		writeDouble(_activeChar.getCurrentHp());
		writeDouble(_activeChar.getCurrentMp());
		writeInt(_activeChar.getSp());
		writeLong(_activeChar.getExp());
		writeInt(_activeChar.getLevel());
		writeInt(_activeChar.getKarma()); // thx evill33t
		writeInt(0x0); // ?
		writeInt(_activeChar.getINT());
		writeInt(_activeChar.getSTR());
		writeInt(_activeChar.getCON());
		writeInt(_activeChar.getMEN());
		writeInt(_activeChar.getDEX());
		writeInt(_activeChar.getWIT());
		for (int i = 0; i < 30; i++)
		{
			writeInt(0x00);
		}
		// writeInt(0); //c3
		// writeInt(0); //c3
		// writeInt(0); //c3
		
		writeInt(0x00); // c3 work
		writeInt(0x00); // c3 work
		
		// extra info
		writeInt(GameTimeController.getInstance().getGameTime()); // in-game time
		
		writeInt(0x00); //
		
		writeInt(0x00); // c3
		
		writeInt(0x00); // c3 InspectorBin
		writeInt(0x00); // c3
		writeInt(0x00); // c3
		writeInt(0x00); // c3
		
		writeInt(0x00); // c3 InspectorBin for 528 client
		writeInt(0x00); // c3
		writeInt(0x00); // c3
		writeInt(0x00); // c3
		writeInt(0x00); // c3
		writeInt(0x00); // c3
		writeInt(0x00); // c3
		writeInt(0x00); // c3
	}
	
	@Override
	public String getType()
	{
		return _S__21_CHARSELECTED;
	}
}
