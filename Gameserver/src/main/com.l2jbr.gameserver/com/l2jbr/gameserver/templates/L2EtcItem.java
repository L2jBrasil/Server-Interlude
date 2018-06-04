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
package com.l2jbr.gameserver.templates;


/**
 * This class is dedicated to the management of EtcItem.
 * @version $Revision: 1.2.2.1.2.3 $ $Date: 2005/03/27 15:30:10 $
 */
public final class L2EtcItem extends L2Item
{

	public L2EtcItem(ItemType type, StatsSet set)
	{
		super(type, set);
	}

	@Override
	public ItemType getItemType()
	{
		return (ItemType) super._type;
	}

	/**
	 * Returns if the item is consumable
	 * @return boolean
	 */
	@Override
	public final boolean isConsumable()
	{
		return ((getItemType() == ItemType.SHOT) || (getItemType() == ItemType.POTION));
	}
	
	/**
	 * Returns the ID of the Etc item after applying the mask.
	 * @return int : ID of the EtcItem
	 */
	@Override
	public int getItemMask()
	{
		return getItemType().mask();
	}
	
}
