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
import com.l2jbr.commons.database.DatabaseAccess;
import com.l2jbr.gameserver.handler.IAdminCommandHandler;
import com.l2jbr.gameserver.model.GMAudit;
import com.l2jbr.gameserver.model.L2World;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.model.entity.database.repository.CharacterRepository;
import com.l2jbr.gameserver.network.SystemMessageId;
import com.l2jbr.gameserver.serverpackets.SystemMessage;


/**
 * This class handles following admin commands: - changelvl = change a character's access level Can be used for character ban (as opposed to regular //ban that affects accounts) or to grant mod/GM privileges ingame
 * @version $Revision: 1.1.2.2.2.3 $ $Date: 2005/04/11 10:06:00 $
 */
public class AdminChangeAccessLevel implements IAdminCommandHandler
{
	
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_changelvl"
	};
	
	private static final int REQUIRED_LEVEL = Config.GM_ACCESSLEVEL;
	
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
		
		handleChangeLevel(command, activeChar);
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
	
	/**
	 * If no character name is specified, tries to change GM's target access level. Else if a character name is provided, will try to reach it either from L2World or from a database connection.
	 * @param command
	 * @param activeChar
	 */
	private void handleChangeLevel(String command, L2PcInstance activeChar)
	{
		String[] parts = command.split(" ");
		if (parts.length == 2)
		{
			try
			{
				int lvl = Integer.parseInt(parts[1]);
				if (activeChar.getTarget() instanceof L2PcInstance)
				{
					onLineChange(activeChar, (L2PcInstance) activeChar.getTarget(), lvl);
				}
				else
				{
					activeChar.sendPacket(new SystemMessage(SystemMessageId.INCORRECT_TARGET));
				}
			}
			catch (Exception e)
			{
				activeChar.sendMessage("Usage: //changelvl <target_new_level> | <player_name> <new_level>");
			}
		}
		else if (parts.length == 3)
		{
			String name = parts[1];
			int lvl = Integer.parseInt(parts[2]);
			L2PcInstance player = L2World.getInstance().getPlayer(name);
			if (player != null)
			{
				onLineChange(activeChar, player, lvl);
			}
			else {
                CharacterRepository repository = DatabaseAccess.getRepository(CharacterRepository.class);
                if(repository.updateAccessLevelByCharName(name, lvl) == 0) {
                    activeChar.sendMessage("Character not found or access level unaltered.");
                } else {
                    activeChar.sendMessage("Character's access level is now set to " + lvl);
                }
			}
		}
	}
	
	/**
	 * @param activeChar
	 * @param player
	 * @param lvl
	 */
	private void onLineChange(L2PcInstance activeChar, L2PcInstance player, int lvl)
	{
		player.setAccessLevel(lvl);
		if (lvl > 0)
		{
			player.sendMessage("Your access level has been changed to " + lvl);
		}
		else
		{
			player.sendMessage("Your character has been banned. Bye.");
			player.logout();
		}
		activeChar.sendMessage("Character's access level is now set to " + lvl + ". Effects won't be noticeable until next session.");
	}
}
