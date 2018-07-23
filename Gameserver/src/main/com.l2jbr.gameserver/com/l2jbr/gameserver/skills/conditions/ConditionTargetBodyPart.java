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
package com.l2jbr.gameserver.skills.conditions;

import com.l2jbr.gameserver.model.Inventory;
import com.l2jbr.gameserver.model.entity.database.Armor;
import com.l2jbr.gameserver.skills.Env;
import com.l2jbr.gameserver.templates.BodyPart;


/**
 * @author mkizub TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code Templates
 */
public class ConditionTargetBodyPart extends Condition
{
	
	private final Armor _armor;
	
	public ConditionTargetBodyPart(Armor armor)
	{
		_armor = armor;
	}
	
	@Override
	public boolean testImpl(Env env)
	{
		// target is attacker
		if (env.target == null)
		{
			return true;
		}
		int bodypart = env.target.getAttackingBodyPart();
		BodyPart armorPart = _armor.getBodyPart();
		switch (bodypart)
		{
			case Inventory.PAPERDOLL_CHEST:
				return (armorPart == BodyPart.CHEST || armorPart == BodyPart.FULL_ARMOR || armorPart == BodyPart.UNDERWEAR);
			case Inventory.PAPERDOLL_LEGS:
				return (armorPart == BodyPart.LEGS || armorPart == BodyPart.FULL_ARMOR);
			case Inventory.PAPERDOLL_HEAD:
				return (armorPart == BodyPart.HEAD);
			case Inventory.PAPERDOLL_FEET:
				return (armorPart == BodyPart.FEET);
			case Inventory.PAPERDOLL_GLOVES:
				return (armorPart == BodyPart.GLOVES);
			default:
				return true;
		}
	}
}
