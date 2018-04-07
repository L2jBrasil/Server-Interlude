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
import com.l2jbr.gameserver.cache.CrestCache;
import com.l2jbr.gameserver.serverpackets.AllyCrest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class ...
 * @version $Revision: 1.3.4.4 $ $Date: 2005/03/27 15:29:30 $
 */
public final class RequestAllyCrest extends L2GameClientPacket
{
	private static final String _C__88_REQUESTALLYCREST = "[C] 88 RequestAllyCrest";
	private static Logger _log = LoggerFactory.getLogger(RequestAllyCrest.class.getName());
	
	private int _crestId;
	
	/**
	 * packet type id 0x88 format: cd
	 */
	@Override
	protected void readImpl()
	{
		_crestId = readD();
	}
	
	@Override
	protected void runImpl()
	{
		if (Config.DEBUG)
		{
			_log.debug("allycrestid " + _crestId + " requested");
		}
		
		byte[] data = CrestCache.getInstance().getAllyCrest(_crestId);
		
		if (data != null)
		{
			AllyCrest ac = new AllyCrest(_crestId, data);
			sendPacket(ac);
		}
		else
		{
			if (Config.DEBUG)
			{
				_log.debug("allycrest is missing:" + _crestId);
			}
		}
	}
	
	@Override
	public String getType()
	{
		return _C__88_REQUESTALLYCREST;
	}
}
