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

import com.l2jbr.commons.database.DatabaseAccess;
import com.l2jbr.commons.util.Util;
import com.l2jbr.gameserver.model.L2World;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.model.entity.database.CharacterFriends;
import com.l2jbr.gameserver.model.entity.database.repository.CharacterFriendRepository;

import java.util.List;

/**
 * Support for "Chat with Friends" dialog.
 *
 * Format: ch (hdSdh) h: Total Friend Count h: Unknown d: Player Object ID S: Friend Name d: Online/Offline h: Unknown
 * @author Tempy
 */
public class FriendList extends L2GameServerPacket {
	private static final String _S__FA_FRIENDLIST = "[S] FA FriendList";
	
	private final L2PcInstance _activeChar;
	
	public FriendList(L2PcInstance character)  {
		_activeChar = character;
	}
	
	@Override
	protected final void writeImpl() {
		if (_activeChar == null) {
			return;
		}
		
        CharacterFriendRepository repository = DatabaseAccess.getRepository(CharacterFriendRepository.class);
        List<CharacterFriends> friendList = repository.findAllByCharacterId(_activeChar.getObjectId());

        if(!Util.isNullOrEmpty(friendList)) {
            writeByte(0xfa);
            writeShort(friendList.size());
            friendList.forEach(characterFriends -> {
                L2PcInstance friend = L2World.getInstance().getPlayer(characterFriends.getFriendName());

                writeShort(0); // ??
                writeInt(characterFriends.getFriendId());
                writeString(characterFriends.getFriendName());
                writeInt(friend == null ? 0 : 1);  // offline : online
                writeShort(0); // ??

            });
        }
	}

	@Override
	public String getType()
	{
		return _S__FA_FRIENDLIST;
	}
}
