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

import com.l2jbr.gameserver.handler.ISkillHandler;
import com.l2jbr.gameserver.instancemanager.CastleManager;
import com.l2jbr.gameserver.model.L2Character;
import com.l2jbr.gameserver.model.L2ItemInstance;
import com.l2jbr.gameserver.model.L2Object;
import com.l2jbr.gameserver.model.L2Skill;
import com.l2jbr.gameserver.model.L2Skill.SkillType;
import com.l2jbr.gameserver.model.actor.instance.L2DoorInstance;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.model.entity.Castle;
import com.l2jbr.gameserver.network.SystemMessageId;
import com.l2jbr.gameserver.serverpackets.SystemMessage;
import com.l2jbr.gameserver.skills.Formulas;
import com.l2jbr.gameserver.templates.L2WeaponType;


/**
 * @author _tomciaaa_
 */
public class StrSiegeAssault implements ISkillHandler
{
	// private static Logger _log = LoggerFactory.getLogger(StrSiegeAssault.class.getName());
	private static final SkillType[] SKILL_IDS =
	{
		SkillType.STRSIEGEASSAULT
	};
	
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets)
	{
		
		if ((activeChar == null) || !(activeChar instanceof L2PcInstance))
		{
			return;
		}
		
		L2PcInstance player = (L2PcInstance) activeChar;
		
		if (!activeChar.isRiding())
		{
			return;
		}
		if (!(player.getTarget() instanceof L2DoorInstance))
		{
			return;
		}
		
		Castle castle = CastleManager.getInstance().getCastle(player);
		if ((castle == null) || !checkIfOkToUseStriderSiegeAssault(player, castle, true))
		{
			return;
		}
		
		try
		{
			L2ItemInstance itemToTake = player.getInventory().getItemByItemId(skill.getItemConsumeId());
			if (!player.destroyItem("Consume", itemToTake.getObjectId(), skill.getItemConsume(), null, true))
			{
				return;
			}
			
			// damage calculation
			int damage = 0;
			
			for (L2Object target2 : targets)
			{
				L2Character target = (L2Character) target2;
				L2ItemInstance weapon = activeChar.getActiveWeaponInstance();
				if ((activeChar instanceof L2PcInstance) && (target instanceof L2PcInstance) && target.isAlikeDead() && target.isFakeDeath())
				{
					target.stopFakeDeath(null);
				}
				else if (target.isAlikeDead())
				{
					continue;
				}
				
				boolean dual = activeChar.isUsingDualWeapon();
				boolean shld = Formulas.getInstance().calcShldUse(activeChar, target);
				boolean crit = Formulas.getInstance().calcCrit(activeChar.getCriticalHit(target, skill));
				boolean soul = ((weapon != null) && (weapon.getChargedSoulshot() == L2ItemInstance.CHARGED_SOULSHOT) && (weapon.getItemType() != L2WeaponType.DAGGER));
				
				if (!crit && ((skill.getCondition() & L2Skill.COND_CRIT) != 0))
				{
					damage = 0;
				}
				else
				{
					damage = (int) Formulas.getInstance().calcPhysDam(activeChar, target, skill, shld, crit, dual, soul);
				}
				
				if (damage > 0)
				{
					target.reduceCurrentHp(damage, activeChar);
					if (soul && (weapon != null))
					{
						weapon.setChargedSoulshot(L2ItemInstance.CHARGED_NONE);
					}
					
					activeChar.sendDamageMessage(target, damage, false, false, false);
					
				}
				else
				{
					activeChar.sendPacket(SystemMessage.sendString(skill.getName() + " failed."));
				}
			}
		}
		catch (Exception e)
		{
			player.sendMessage("Error using siege assault:" + e);
		}
	}
	
	@Override
	public SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
	
	/**
	 * Return true if character clan place a flag<BR>
	 * <BR>
	 * @param activeChar The L2Character of the character placing the flag
	 * @param isCheckOnly if false, it will send a notification to the player telling him why it failed
	 * @return
	 */
	public static boolean checkIfOkToUseStriderSiegeAssault(L2Character activeChar, boolean isCheckOnly)
	{
		return checkIfOkToUseStriderSiegeAssault(activeChar, CastleManager.getInstance().getCastle(activeChar), isCheckOnly);
	}
	
	public static boolean checkIfOkToUseStriderSiegeAssault(L2Character activeChar, Castle castle, boolean isCheckOnly)
	{
		if ((activeChar == null) || !(activeChar instanceof L2PcInstance))
		{
			return false;
		}
		
		SystemMessage sm = new SystemMessage(SystemMessageId.S1_S2);
		L2PcInstance player = (L2PcInstance) activeChar;
		
		if ((castle == null) || (castle.getCastleId() <= 0))
		{
			sm.addString("You must be on castle ground to use strider siege assault");
		}
		else if (!castle.getSiege().getIsInProgress())
		{
			sm.addString("You can only use strider siege assault during a siege.");
		}
		else if (!(player.getTarget() instanceof L2DoorInstance))
		{
			sm.addString("You can only use strider siege assault on doors and walls.");
		}
		else if (!activeChar.isRiding())
		{
			sm.addString("You can only use strider siege assault when on strider.");
		}
		else
		{
			return true;
		}
		
		if (!isCheckOnly)
		{
			player.sendPacket(sm);
		}
		return false;
	}
}
