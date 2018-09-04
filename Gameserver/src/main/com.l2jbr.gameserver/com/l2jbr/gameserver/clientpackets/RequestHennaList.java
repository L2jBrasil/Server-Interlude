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
package com.l2jbr.gameserver.clientpackets;

import com.l2jbr.gameserver.datatables.HennaTreeTable;
import com.l2jbr.gameserver.serverpackets.HennaEquipList;

import static java.util.Objects.isNull;

/**
 * RequestHennaList - 0xba
 * @author Tempy
 */
public final class RequestHennaList extends L2GameClientPacket {
	private static final String _C__BA_RequestHennaList = "[C] ba RequestHennaList";

	@SuppressWarnings("unused")
	private int _unknown;
	
	@Override
	protected void readImpl()
	{
		_unknown = readInt(); // ??
	}
	
	@Override
	protected void runImpl() {
		var activeChar = getClient().getActiveChar();
		if (isNull(activeChar)) {
			return;
		}
		
		var hennas = HennaTreeTable.getInstance().getAvailableHenna(activeChar.getPlayerClass());
		activeChar.sendPacket(new HennaEquipList(activeChar, hennas));
	}

	@Override
	public String getType()
	{
		return _C__BA_RequestHennaList;
	}
}