/* This program is free software; you can redistribute it and/or modify
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

/**
 * @author KenM
 */
public final class AccountKicked extends L2LoginServerPacket  {

	
	private final AccountKickedReason _reason;
	
	public AccountKicked(AccountKickedReason reason) {
		_reason = reason;
	}
	
	@Override
	protected void write()
	{
		writeByte(0x02);
		writeInt(_reason.getCode());
	}

    @Override
    protected int packetSize() {
        return super.packetSize() + 5;
    }

    public enum AccountKickedReason  {
        REASON_DATA_STEALER(0x01),
        REASON_GENERIC_VIOLATION(0x08),
        REASON_7_DAYS_SUSPENDED(0x10),
        REASON_PERMANENTLY_BANNED(0x20);

        private final int _code;

        AccountKickedReason(int code)
        {
            _code = code;
        }

        public final int getCode()
        {
            return _code;
        }
    }

}
