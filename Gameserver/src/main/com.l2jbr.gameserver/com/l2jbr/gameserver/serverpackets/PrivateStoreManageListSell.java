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

import com.l2jbr.gameserver.model.TradeList;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;


/**
 * 3 section to this packet 1)playerinfo which is always sent dd 2)list of items which can be added to sell d(hhddddhhhd) 3)list of items which have already been setup for sell in previous sell private store sell manageent d(hhddddhhhdd) *
 * @version $Revision: 1.3.2.1.2.3 $ $Date: 2005/03/27 15:29:39 $
 */
public class PrivateStoreManageListSell extends L2GameServerPacket
{
	private static final String _S__B3_PRIVATESELLLISTSELL = "[S] 9a PrivateSellListSell";
	private final L2PcInstance _activeChar;
	private final int _playerAdena;
	private final boolean _packageSale;
	private final TradeList.TradeItem[] _itemList;
	private final TradeList.TradeItem[] _sellList;
	
	public PrivateStoreManageListSell(L2PcInstance player)
	{
		_activeChar = player;
		_playerAdena = _activeChar.getAdena();
		_activeChar.getSellList().updateItems();
		_packageSale = _activeChar.getSellList().isPackaged();
		_itemList = _activeChar.getInventory().getAvailableItems(_activeChar.getSellList());
		_sellList = _activeChar.getSellList().getItems();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeByte(0x9a);
		// section 1
		writeInt(_activeChar.getObjectId());
		writeInt(_packageSale ? 1 : 0); // Package sell
		writeInt(_playerAdena);
		
		// section2
		writeInt(_itemList.length); // for potential sells
		for (TradeList.TradeItem item : _itemList)
		{
			writeInt(item.getItem().getType2().getId());
			writeInt(item.getObjectId());
			writeInt(item.getItem().getId());
			writeInt(item.getCount());
			writeShort(0);
			writeShort(item.getEnchant());// enchant lvl
			writeShort(0);
			writeInt(item.getItem().getBodyPart().getId());
			writeInt(item.getPrice()); // store price
		}
		// section 3
		writeInt(_sellList.length); // count for any items already added for sell
		for (TradeList.TradeItem item : _sellList)
		{
			writeInt(item.getItem().getType2().getId());
			writeInt(item.getObjectId());
			writeInt(item.getItem().getId());
			writeInt(item.getCount());
			writeShort(0);
			writeShort(item.getEnchant());// enchant lvl
			writeShort(0x00);
			writeInt(item.getItem().getBodyPart().getId());
			writeInt(item.getPrice());// your price
			writeInt(item.getItem().getPrice()); // store price
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jbr.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__B3_PRIVATESELLLISTSELL;
	}
}
