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

import com.l2jbr.commons.database.DatabaseAccess;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.model.entity.database.repository.CharacterFriendRepository;
import com.l2jbr.gameserver.model.entity.database.repository.CharacterRepository;
import com.l2jbr.gameserver.network.SystemMessageId;
import com.l2jbr.gameserver.serverpackets.SystemMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class ...
 * @version $Revision: 1.3.4.2 $ $Date: 2005/03/27 15:29:30 $
 */
public final class RequestFriendDel extends L2GameClientPacket
{
	
	private static final String _C__61_REQUESTFRIENDDEL = "[C] 61 RequestFriendDel";
	private static Logger _log = LoggerFactory.getLogger(RequestFriendDel.class.getName());
	
	private String _name;
	
	@Override
	protected void readImpl()
	{
		_name = readString();
	}
	
	@Override
	protected void runImpl()  {
		SystemMessage sm;
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}

        CharacterRepository characterRepository = DatabaseAccess.getRepository(CharacterRepository.class);
		int friendId = characterRepository.findIdByName(_name);

        CharacterFriendRepository repository = DatabaseAccess.getRepository(CharacterFriendRepository.class);
		if(repository.deleteFriendship(activeChar.getObjectId(), friendId) > 0) {
            sm = new SystemMessage(SystemMessageId.S1_HAS_BEEN_DELETED_FROM_YOUR_FRIENDS_LIST);
            sm.addString(_name);
            activeChar.sendPacket(sm);
        } else {
            sm = new SystemMessage(SystemMessageId.S1_NOT_ON_YOUR_FRIENDS_LIST);
            sm.addString(_name);
            activeChar.sendPacket(sm);
        }
	}
	
	@Override
	public String getType()
	{
		return _C__61_REQUESTFRIENDDEL;
	}
}
