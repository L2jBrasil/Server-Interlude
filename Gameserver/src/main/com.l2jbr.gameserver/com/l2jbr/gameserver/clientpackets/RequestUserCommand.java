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

import com.l2jbr.gameserver.handler.IUserCommandHandler;
import com.l2jbr.gameserver.handler.UserCommandHandler;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.network.SystemMessageId;
import com.l2jbr.gameserver.serverpackets.SystemMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class ...
 * @version $Revision: 1.1.2.1.2.2 $ $Date: 2005/03/27 15:29:30 $
 */
public class RequestUserCommand extends L2GameClientPacket
{
	private static final String _C__AA_REQUESTUSERCOMMAND = "[C] aa RequestUserCommand";
	static Logger _log = LoggerFactory.getLogger(RequestUserCommand.class.getName());
	
	private int _command;
	
	@Override
	protected void readImpl()
	{
		_command = readD();
	}
	
	@Override
	protected void runImpl()
	{
		L2PcInstance player = getClient().getActiveChar();
		if (player == null)
		{
			return;
		}
		
		IUserCommandHandler handler = UserCommandHandler.getInstance().getUserCommandHandler(_command);
		
		if (handler == null)
		{
			SystemMessage sm = new SystemMessage(SystemMessageId.S1_S2);
			sm.addString("user commandID " + _command + " not implemented yet");
			player.sendPacket(sm);
			sm = null;
		}
		else
		{
			handler.useUserCommand(_command, getClient().getActiveChar());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jbr.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__AA_REQUESTUSERCOMMAND;
	}
}
