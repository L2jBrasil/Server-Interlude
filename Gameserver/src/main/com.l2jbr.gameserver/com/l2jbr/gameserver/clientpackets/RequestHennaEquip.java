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

import com.l2jbr.gameserver.datatables.HennaTable;
import com.l2jbr.gameserver.model.L2ItemInstance;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.model.entity.database.Henna;
import com.l2jbr.gameserver.network.SystemMessageId;
import com.l2jbr.gameserver.serverpackets.InventoryUpdate;
import com.l2jbr.gameserver.serverpackets.SystemMessage;


/**
 * This class ...
 * @version $Revision$ $Date$
 */
public final class RequestHennaEquip extends L2GameClientPacket
{
	private static final String _C__BC_RequestHennaEquip = "[C] bc RequestHennaEquip";
	private int _symbolId;
	
	// format cd
	
	/**
	 * packet type id 0xbb format: cd
	 */
	@Override
	protected void readImpl()
	{
		_symbolId = readInt();
	}
	
	@Override
	protected void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		
		if (activeChar == null)
		{
			return;
		}
		
		Henna template = HennaTable.getInstance().getTemplate(_symbolId);
		
		if (template == null)
		{
			return;
		}
		int _count = 0;
		
		try
		{
			_count = activeChar.getInventory().getItemByItemId(template.getDyeId()).getCount();
		}
		catch (Exception e)
		{
		}
		
		if ((_count >= template.getDyeAmount()) && (activeChar.getAdena() >= template.getPrice()) && activeChar.addHenna(template))
		{
			SystemMessage sm = new SystemMessage(SystemMessageId.S1_DISAPPEARED);
			sm.addNumber(template.getDyeId());
			activeChar.sendPacket(sm);
			sm = null;
			activeChar.sendPacket(new SystemMessage(SystemMessageId.SYMBOL_ADDED));
			
			// HennaInfo hi = new HennaInfo(temp,activeChar);
			// activeChar.sendPacket(hi);
			
			activeChar.getInventory().reduceAdena("Henna", template.getPrice(), activeChar, activeChar.getLastFolkNPC());
			L2ItemInstance dyeToUpdate = activeChar.getInventory().destroyItemByItemId("Henna", template.getDyeId(), template.getDyeAmount(), activeChar, activeChar.getLastFolkNPC());
			
			// update inventory
			InventoryUpdate iu = new InventoryUpdate();
			iu.addModifiedItem(activeChar.getInventory().getAdenaInstance());
			iu.addModifiedItem(dyeToUpdate);
			activeChar.sendPacket(iu);
		}
		else
		{
			activeChar.sendPacket(new SystemMessage(SystemMessageId.CANT_DRAW_SYMBOL));
		}
	}
	
	@Override
	public String getType()
	{
		return _C__BC_RequestHennaEquip;
	}
}
