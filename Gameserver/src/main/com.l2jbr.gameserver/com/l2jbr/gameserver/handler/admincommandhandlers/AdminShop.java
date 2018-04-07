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
import com.l2jbr.gameserver.TradeController;
import com.l2jbr.gameserver.handler.IAdminCommandHandler;
import com.l2jbr.gameserver.model.GMAudit;
import com.l2jbr.gameserver.model.L2TradeList;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.serverpackets.ActionFailed;
import com.l2jbr.gameserver.serverpackets.BuyList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class handles following admin commands: - gmshop = shows menu - buy id = shows shop with respective id
 * @version $Revision: 1.2.4.4 $ $Date: 2005/04/11 10:06:06 $
 */
public class AdminShop implements IAdminCommandHandler
{
	private static Logger _log = LoggerFactory.getLogger(AdminShop.class.getName());
	
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_buy",
		"admin_gmshop"
	};
	private static final int REQUIRED_LEVEL = Config.GM_CREATE_ITEM;
	
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
		
		if (command.startsWith("admin_buy"))
		{
			try
			{
				handleBuyRequest(activeChar, command.substring(10));
			}
			catch (IndexOutOfBoundsException e)
			{
				activeChar.sendMessage("Please specify buylist.");
			}
		}
		else if (command.equals("admin_gmshop"))
		{
			AdminHelpPage.showHelpPage(activeChar, "gmshops.htm");
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
	
	private void handleBuyRequest(L2PcInstance activeChar, String command)
	{
		int val = -1;
		try
		{
			val = Integer.parseInt(command);
		}
		catch (Exception e)
		{
			_log.warn("admin buylist failed:" + command);
		}
		
		L2TradeList list = TradeController.getInstance().getBuyList(val);
		
		if (list != null)
		{
			activeChar.sendPacket(new BuyList(list, activeChar.getAdena()));
			if (Config.DEBUG)
			{
				_log.debug("GM: " + activeChar.getName() + "(" + activeChar.getObjectId() + ") opened GM shop id " + val);
			}
		}
		else
		{
			_log.warn("no buylist with id:" + val);
		}
		activeChar.sendPacket(new ActionFailed());
	}
}
