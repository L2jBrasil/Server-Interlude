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
package com.l2jbr.gameserver.skills.effects;

import com.l2jbr.gameserver.model.L2Attackable;
import com.l2jbr.gameserver.model.L2Effect;
import com.l2jbr.gameserver.model.L2Skill.SkillTargetType;
import com.l2jbr.gameserver.network.SystemMessageId;
import com.l2jbr.gameserver.serverpackets.SystemMessage;
import com.l2jbr.gameserver.skills.Env;


class EffectDamOverTime extends L2Effect
{
	public EffectDamOverTime(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.DMG_OVER_TIME;
	}
	
	@Override
	public boolean onActionTime()
	{
		if (getEffected().isDead())
		{
			return false;
		}
		
		double damage = calc();
		
		if (damage >= getEffected().getCurrentHp())
		{
			if (getSkill().isToggle())
			{
				SystemMessage sm = new SystemMessage(SystemMessageId.SKILL_REMOVED_DUE_LACK_HP);
				getEffected().sendPacket(sm);
				return false;
			}
			
			// ** This is just hotfix, needs better solution **
			// 1947: "DOT skills shouldn't kill"
			// Well, some of them should ;-)
			if (getSkill().getId() != 4082)
			{
				damage = getEffected().getCurrentHp() - 1;
			}
		}
		
		boolean awake = !(getEffected() instanceof L2Attackable) && !((getSkill().getTargetType() == SkillTargetType.TARGET_SELF) && getSkill().isToggle());
		
		getEffected().reduceCurrentHp(damage, getEffector(), awake);
		
		return true;
	}
}
