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
package com.l2jbr.loginserver.serverpackets;

import com.l2jbr.loginserver.SessionKey;

/**
 * Format: dddddddd f: the session key d: ? d: ? d: ? d: ? d: ? d: ? b: 16 bytes - unknown
 */
public final class LoginOk extends L2LoginServerPacket
{
	private final int _loginOk1, _loginOk2;
	
	public LoginOk(SessionKey sessionKey)
	{
		_loginOk1 = sessionKey.loginOkID1;
		_loginOk2 = sessionKey.loginOkID2;
	}
	
	@Override
	protected void write()
	{
		writeByte(0x03);
		writeInt(_loginOk1);
		writeInt(_loginOk2);
		writeInt(0x00);
		writeInt(0x00);
		writeInt(0x000003ea);
		writeInt(0x00);
		writeInt(0x00);
		writeInt(0x00);
		writeBytes(new byte[16]);
	}

	@Override
	protected int packetSize() {
		return super.packetSize() + 49;
	}
}
