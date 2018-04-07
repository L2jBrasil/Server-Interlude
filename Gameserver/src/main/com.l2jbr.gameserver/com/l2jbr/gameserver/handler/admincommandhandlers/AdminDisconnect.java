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
package com.l2jbr.gameserver.handler.admincommandhandlers;

import com.l2jbr.commons.Config;
import com.l2jbr.gameserver.communitybbs.Manager.RegionBBSManager;
import com.l2jbr.gameserver.handler.IAdminCommandHandler;
import com.l2jbr.gameserver.model.GMAudit;
import com.l2jbr.gameserver.model.L2Object;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.network.SystemMessageId;
import com.l2jbr.gameserver.serverpackets.LeaveWorld;
import com.l2jbr.gameserver.serverpackets.SystemMessage;


/**
 * This class handles following admin commands: - character_disconnect = disconnects target player
 * @version $Revision: 1.2.4.4 $ $Date: 2005/04/11 10:06:00 $
 */
public class AdminDisconnect implements IAdminCommandHandler
{
	
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_character_disconnect"
	};
	private static final int REQUIRED_LEVEL = Config.GM_KICK;
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		if (!Config.ALT_PRIVILEGES_ADMIN)
		{
			if (!(checkLevel(activeChar.getAccessLevel()) && activeChar.isGM()))
			{
				return false;
			}
		}
		
		if (command.equals("admin_character_disconnect"))
		{
			disconnectCharacter(activeChar);
		}
		
		String target = (activeChar.getTarget() != null ? activeChar.getTarget().getName() : "no-target");
		GMAudit.auditGMAction(activeChar.getName(), command, target, "");
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	private boolean checkLevel(int level)
	{
		return (level >= REQUIRED_LEVEL);
	}
	
	private void disconnectCharacter(L2PcInstance activeChar)
	{
		L2Object target = activeChar.getTarget();
		L2PcInstance player = null;
		if (target instanceof L2PcInstance)
		{
			player = (L2PcInstance) target;
		}
		else
		{
			return;
		}
		
		if (player.getObjectId() == activeChar.getObjectId())
		{
			SystemMessage sm = new SystemMessage(SystemMessageId.S1_S2);
			sm.addString("You cannot logout your character.");
			activeChar.sendPacket(sm);
		}
		else
		{
			SystemMessage sm = new SystemMessage(SystemMessageId.S1_S2);
			sm.addString("Character " + player.getName() + " disconnected from server.");
			activeChar.sendPacket(sm);
			
			// Logout Character
			LeaveWorld ql = new LeaveWorld();
			player.sendPacket(ql);
			
			RegionBBSManager.getInstance().changeCommunityBoard();
			
			player.closeNetConnection();
		}
	}
}
