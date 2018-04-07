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
package com.l2jbr.gameserver.handler.skillhandlers;

import com.l2jbr.commons.Config;
import com.l2jbr.gameserver.datatables.MapRegionTable;
import com.l2jbr.gameserver.handler.ISkillHandler;
import com.l2jbr.gameserver.model.L2Character;
import com.l2jbr.gameserver.model.L2Object;
import com.l2jbr.gameserver.model.L2Skill;
import com.l2jbr.gameserver.model.L2Skill.SkillType;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.model.entity.TvTEvent;
import com.l2jbr.gameserver.network.SystemMessageId;
import com.l2jbr.gameserver.serverpackets.ActionFailed;
import com.l2jbr.gameserver.serverpackets.SystemMessage;


public class Recall implements ISkillHandler
{
	// private static Logger _log = LoggerFactory.getLogger(Recall.class.getName());
	private static final SkillType[] SKILL_IDS =
	{
		SkillType.RECALL
	};
	
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets)
	{
		if (activeChar instanceof L2PcInstance)
		{
			// Thanks nbd
			if (!TvTEvent.onEscapeUse(((L2PcInstance) activeChar).getName()))
			{
				((L2PcInstance) activeChar).sendPacket(new ActionFailed());
				return;
			}
			
			if (((L2PcInstance) activeChar).isInOlympiadMode())
			{
				((L2PcInstance) activeChar).sendPacket(new SystemMessage(SystemMessageId.THIS_ITEM_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT));
				return;
			}
		}
		
		try
		{
			for (int index = 0; index < targets.length; index++)
			{
				if (!(targets[index] instanceof L2Character))
				{
					continue;
				}
				
				L2Character target = (L2Character) targets[index];
				
				if (target instanceof L2PcInstance)
				{
					L2PcInstance targetChar = (L2PcInstance) target;
					
					// Check to see if the current player target is in a festival.
					if (targetChar.isFestivalParticipant())
					{
						targetChar.sendPacket(SystemMessage.sendString("You may not use an escape skill in a festival."));
						continue;
					}
					
					// Check to see if player is in jail
					if (targetChar.isInJail())
					{
						targetChar.sendPacket(SystemMessage.sendString("You can not escape from jail."));
						continue;
					}
					
					// Check to see if player is in a duel
					if (targetChar.isInDuel())
					{
						targetChar.sendPacket(SystemMessage.sendString("You cannot use escape skills during a duel."));
						continue;
					}
				}
				
				target.teleToLocation(MapRegionTable.TeleportWhereType.Town);
			}
		}
		catch (Throwable e)
		{
			if (Config.DEBUG)
			{
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}