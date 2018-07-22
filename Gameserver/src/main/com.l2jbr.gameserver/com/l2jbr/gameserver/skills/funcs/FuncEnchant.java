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
package com.l2jbr.gameserver.skills.funcs;

import com.l2jbr.gameserver.model.L2ItemInstance;
import com.l2jbr.gameserver.skills.Env;
import com.l2jbr.gameserver.skills.Stats;
import com.l2jbr.gameserver.templates.BodyPart;
import com.l2jbr.gameserver.templates.CrystalType;
import com.l2jbr.gameserver.templates.ItemType;


public class FuncEnchant extends Func
{
	
	public FuncEnchant(Stats pStat, int pOrder, Object owner, Lambda lambda)
	{
		super(pStat, pOrder, owner);
	}
	
	@Override
	public void calc(Env env)
	{
		if ((cond != null) && !cond.test(env))
		{
			return;
		}
		L2ItemInstance item = (L2ItemInstance) funcOwner;
		CrystalType cristall = item.getItem().getCrystalType();
		Enum<?> itemType = item.getItemType();
		
		if (cristall == CrystalType.NONE)
		{
			return;
		}
		int enchant = item.getEnchantLevel();
		
		int overenchant = 0;
		if (enchant > 3)
		{
			overenchant = enchant - 3;
			enchant = 3;
		}
		
		if ((stat == Stats.MAGIC_DEFENCE) || (stat == Stats.PHYSIC_DEFENCE))
		{
			env.value += enchant + (3 * overenchant);
			return;
		}
		
		if (stat == Stats.MAGIC_ATTACK)
		{
			switch (item.getItem().getCrystalType())
			{
				case S:
					env.value += (4 * enchant) + (8 * overenchant);
					break;
				case A:
					env.value += (3 * enchant) + (6 * overenchant);
					break;
				case B:
					env.value += (3 * enchant) + (6 * overenchant);
					break;
				case C:
					env.value += (3 * enchant) + (6 * overenchant);
					break;
				case D:
					env.value += (2 * enchant) + (4 * overenchant);
					break;
			}
			return;
		}
		
		switch (item.getItem().getCrystalType())
		{
			case A:
				if (itemType == ItemType.BOW)
				{
					env.value += (8 * enchant) + (16 * overenchant);
				}
				else if ((itemType == ItemType.DUAL_FIST) || (itemType == ItemType.DUAL) || ((itemType == ItemType.SWORD) && (item.getItem().getBodyPart() == BodyPart.TWO_HAND)))
				{
					env.value += (5 * enchant) + (10 * overenchant);
				}
				else
				{
					env.value += (4 * enchant) + (8 * overenchant);
				}
				break;
			case B:
				if (itemType == ItemType.BOW)
				{
					env.value += (6 * enchant) + (12 * overenchant);
				}
				else if ((itemType == ItemType.DUAL_FIST) || (itemType == ItemType.DUAL) || ((itemType == ItemType.SWORD) && (item.getItem().getBodyPart() == BodyPart.TWO_HAND)))
				{
					env.value += (4 * enchant) + (8 * overenchant);
				}
				else
				{
					env.value += (3 * enchant) + (6 * overenchant);
				}
				break;
			case C:
				if (itemType == ItemType.BOW)
				{
					env.value += (6 * enchant) + (12 * overenchant);
				}
				else if ((itemType == ItemType.DUAL_FIST) || (itemType == ItemType.DUAL) || ((itemType == ItemType.SWORD) && (item.getItem().getBodyPart() == BodyPart.TWO_HAND)))
				{
					env.value += (4 * enchant) + (8 * overenchant);
				}
				else
				{
					env.value += (3 * enchant) + (6 * overenchant);
				}
				
				break;
			case D:
				if (itemType == ItemType.BOW)
				{
					env.value += (4 * enchant) + (8 * overenchant);
				}
				else
				{
					env.value += (2 * enchant) + (4 * overenchant);
				}
				break;
			case S:
				if (itemType == ItemType.BOW)
				{
					env.value += (10 * enchant) + (20 * overenchant);
				}
				else if ((itemType == ItemType.DUAL_FIST) || (itemType == ItemType.DUAL) || ((itemType == ItemType.SWORD) && (item.getItem().getBodyPart() == BodyPart.TWO_HAND)))
				{
					env.value += (4 * enchant) + (12 * overenchant);
				}
				else
				{
					env.value += (4 * enchant) + (10 * overenchant);
				}
				break;
		}
		return;
	}
}
