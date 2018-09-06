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
package com.l2jbr.gameserver.serverpackets;

import com.l2jbr.gameserver.model.L2ItemInstance;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.model.entity.database.Weapon;


/**
 * Sdh(h dddhh [dhhh] d) Sdh ddddd ddddd ddddd ddddd
 * @version $Revision: 1.1.2.1.2.5 $ $Date: 2007/11/26 16:10:05 $
 */
public class GMViewWarehouseWithdrawList extends L2GameServerPacket
{
	private static final String _S__95_GMViewWarehouseWithdrawList = "[S] 95 GMViewWarehouseWithdrawList";
	private final L2ItemInstance[] _items;
	private final String _playerName;
	private final L2PcInstance _activeChar;
	private final int _money;
	
	public GMViewWarehouseWithdrawList(L2PcInstance cha)
	{
		_activeChar = cha;
		_items = _activeChar.getWarehouse().getItems();
		_playerName = _activeChar.getName();
		_money = _activeChar.getAdena();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeByte(0x95);
		writeString(_playerName);
		writeInt(_money);
		writeShort(_items.length);
		
		for (L2ItemInstance item : _items)
		{
			writeShort(item.getItem().getType1().getId());
			
			writeInt(item.getObjectId());
			writeInt(item.getItemId());
			writeInt(item.getCount());
			writeShort(item.getItem().getType2().getId());
			writeShort(item.getCustomType1());
			
			switch (item.getItem().getType2())
			{
				case TYPE2_WEAPON:
				{
					writeInt(item.getItem().getBodyPart().getId());
					writeShort(item.getEnchantLevel());
					writeShort(((Weapon) item.getItem()).getSoulshots());
					writeShort(((Weapon) item.getItem()).getSpiritshots());
					break;
				}
				
				case TYPE2_SHIELD_ARMOR:
				case TYPE2_ACCESSORY:
				case TYPE2_PET_WOLF:
				case TYPE2_PET_HATCHLING:
				case TYPE2_PET_STRIDER:
				case TYPE2_PET_BABY:
				{
					writeInt(item.getItem().getBodyPart().getId());
					writeShort(item.getEnchantLevel());
					writeShort(0x00);
					writeShort(0x00);
					break;
				}
			}
			
			writeInt(item.getObjectId());
			
			switch (item.getItem().getType2())
			{
				case TYPE2_WEAPON:
				{
					if (item.isAugmented())
					{
						writeInt(0x0000FFFF & item.getAugmentation().getAugmentationId());
						writeInt(item.getAugmentation().getAugmentationId() >> 16);
					}
					else
					{
						writeInt(0);
						writeInt(0);
					}
					
					break;
				}
				
				case TYPE2_SHIELD_ARMOR:
				case TYPE2_ACCESSORY:
				case TYPE2_PET_WOLF:
				case TYPE2_PET_HATCHLING:
				case TYPE2_PET_STRIDER:
				case TYPE2_PET_BABY:
				{
					writeInt(0);
					writeInt(0);
				}
			}
		}
	}
	
	@Override
	public String getType()
	{
		return _S__95_GMViewWarehouseWithdrawList;
	}
}
