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

import com.l2jbr.commons.Config;
import com.l2jbr.gameserver.serverpackets.KeyPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class ...
 * @version $Revision: 1.5.2.8.2.8 $ $Date: 2005/04/02 10:43:04 $
 */
public final class ProtocolVersion extends L2GameClientPacket
{
	private static final String _C__00_PROTOCOLVERSION = "[C] 00 ProtocolVersion";
	static Logger _log = LoggerFactory.getLogger(ProtocolVersion.class.getName());
	
	private int _version;
	
	@Override
	protected void readImpl()
	{
		_version = readD();
	}
	
	@Override
	protected void runImpl()
	{
		// this packet is never encrypted
		if (_version == -2)
		{
			if (Config.DEBUG)
			{
				_log.info("Ping received");
			}
			// this is just a ping attempt from the new C2 client
			getClient().closeNow();
		}
		else if ((_version < Config.MIN_PROTOCOL_REVISION) || (_version > Config.MAX_PROTOCOL_REVISION))
		{
			_log.info("Client: " + getClient().toString() + " -> Protocol Revision: " + _version + " is invalid. Minimum is " + Config.MIN_PROTOCOL_REVISION + " and Maximum is " + Config.MAX_PROTOCOL_REVISION + " are supported. Closing connection.");
			_log.warn("Wrong Protocol Version " + _version);
			getClient().closeNow();
		}
		else
		{
			if (Config.DEBUG)
			{
				_log.debug("Client Protocol Revision is ok: " + _version);
			}
			
			KeyPacket pk = new KeyPacket(getClient().enableCrypt());
			getClient().sendPacket(pk);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jbr.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__00_PROTOCOLVERSION;
	}
}
