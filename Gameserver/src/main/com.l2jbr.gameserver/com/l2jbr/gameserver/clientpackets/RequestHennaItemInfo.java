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

import com.l2jbr.gameserver.datatables.HennaTable;
import com.l2jbr.gameserver.model.entity.database.Henna;
import com.l2jbr.gameserver.serverpackets.HennaItemInfo;

import static java.util.Objects.isNull;

// format cd
public final class RequestHennaItemInfo extends L2GameClientPacket {
	private static final String _C__BB_RequestHennaItemInfo = "[C] bb RequestHennaItemInfo";
	private int _symbolId;

	@Override
	protected void readImpl()
	{
		_symbolId = readInt();
	}
	
	@Override
	protected void runImpl() {
		var activeChar = getClient().getActiveChar();

		if (isNull(activeChar)) {
			return;
		}

		Henna template = HennaTable.getInstance().getTemplate(_symbolId);
		if (isNull(template)) {
			return;
		}
		
		HennaItemInfo hii = new HennaItemInfo(template, activeChar);
		activeChar.sendPacket(hii);
	}
	

	@Override
	public String getType()
	{
		return _C__BB_RequestHennaItemInfo;
	}
}
