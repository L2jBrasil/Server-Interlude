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
package com.l2jbr.gameserver.clientpackets;

import com.l2jbr.commons.Config;
import com.l2jbr.gameserver.model.L2World;
import com.l2jbr.gameserver.model.TradeList;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.network.SystemMessageId;
import com.l2jbr.gameserver.serverpackets.SystemMessage;
import com.l2jbr.gameserver.serverpackets.TradeOtherAdd;
import com.l2jbr.gameserver.serverpackets.TradeOwnAdd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class ...
 * @version $Revision: 1.5.2.2.2.5 $ $Date: 2005/03/27 15:29:29 $
 */
public final class AddTradeItem extends L2GameClientPacket
{
	private static final String _C__16_ADDTRADEITEM = "[C] 16 AddTradeItem";
	private static Logger _log = LoggerFactory.getLogger(AddTradeItem.class.getName());
	
	private int _tradeId;
	private int _objectId;
	private int _count;
	
	public AddTradeItem()
	{
	}
	
	@Override
	protected void readImpl()
	{
		_tradeId = readD();
		_objectId = readD();
		_count = readD();
	}
	
	@Override
	protected void runImpl()
	{
		L2PcInstance player = getClient().getActiveChar();
		if (player == null)
		{
			return;
		}
		
		TradeList trade = player.getActiveTradeList();
		if (trade == null)
		{
			_log.warn("Character: " + player.getName() + " requested item:" + _objectId + " add without active tradelist:" + _tradeId);
			return;
		}
		
		if ((trade.getPartner() == null) || (L2World.getInstance().findObject(trade.getPartner().getObjectId()) == null))
		{
			// Trade partner not found, cancel trade
			if (trade.getPartner() != null)
			{
				_log.warn("Character:" + player.getName() + " requested invalid trade object: " + _objectId);
			}
			SystemMessage msg = new SystemMessage(SystemMessageId.TARGET_IS_NOT_FOUND_IN_THE_GAME);
			player.sendPacket(msg);
			player.cancelActiveTrade();
			return;
		}
		
		if (Config.GM_DISABLE_TRANSACTION && (player.getAccessLevel() >= Config.GM_TRANSACTION_MIN) && (player.getAccessLevel() <= Config.GM_TRANSACTION_MAX))
		{
			player.sendMessage("Transactions are disable for your Access Level");
			player.cancelActiveTrade();
			return;
		}
		
		if (!player.validateItemManipulation(_objectId, "trade"))
		{
			player.sendPacket(new SystemMessage(SystemMessageId.NOTHING_HAPPENED));
			return;
		}
		
		TradeList.TradeItem item = trade.addItem(_objectId, _count);
		if (item != null)
		{
			player.sendPacket(new TradeOwnAdd(item));
			trade.getPartner().sendPacket(new TradeOtherAdd(item));
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jbr.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__16_ADDTRADEITEM;
	}
}
