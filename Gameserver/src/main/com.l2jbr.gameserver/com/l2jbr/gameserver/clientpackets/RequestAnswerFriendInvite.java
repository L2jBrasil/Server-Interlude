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
import com.l2jbr.gameserver.model.entity.database.CharacterFriends;
import com.l2jbr.gameserver.model.entity.database.repository.CharacterFriendRepository;
import com.l2jbr.gameserver.network.SystemMessageId;
import com.l2jbr.gameserver.serverpackets.SystemMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * sample 5F 01 00 00 00 format cdd
 * @version $Revision: 1.7.4.2 $ $Date: 2005/03/27 15:29:30 $
 */
public final class RequestAnswerFriendInvite extends L2GameClientPacket
{
	private static final String _C__5F_REQUESTANSWERFRIENDINVITE = "[C] 5F RequestAnswerFriendInvite";
	private static Logger _log = LoggerFactory.getLogger(RequestAnswerFriendInvite.class.getName());
	
	private int _response;
	
	@Override
	protected void readImpl()
	{
		_response = readInt();
	}
	
	@Override
	protected void runImpl()
	{
		L2PcInstance player = getClient().getActiveChar();
		if (player != null)
		{
			L2PcInstance requestor = player.getActiveRequester();
			if (requestor == null)
			{
				return;
			}
			
			if (_response == 1)
			{
                CharacterFriendRepository repository = DatabaseAccess.getRepository(CharacterFriendRepository.class);
                CharacterFriends characterFriends = new CharacterFriends(requestor.getObjectId(), player.getObjectId(), player.getName());
                repository.save(characterFriends);
                characterFriends = new CharacterFriends(player.getObjectId(), requestor.getObjectId(), requestor.getName());
                repository.save(characterFriends);

                SystemMessage msg = new SystemMessage(SystemMessageId.YOU_HAVE_SUCCEEDED_INVITING_FRIEND);
                requestor.sendPacket(msg);

                // Player added to your friendlist
                msg = new SystemMessage(SystemMessageId.S1_ADDED_TO_FRIENDS);
                msg.addString(player.getName());
                requestor.sendPacket(msg);

                // has joined as friend.
                msg = new SystemMessage(SystemMessageId.S1_JOINED_AS_FRIEND);
                msg.addString(requestor.getName());
                player.sendPacket(msg);
			}
			else
			{
				requestor.sendPacket(new SystemMessage(SystemMessageId.FAILED_TO_INVITE_A_FRIEND));
			}
			
			player.setActiveRequester(null);
			requestor.onTransactionResponse();
		}
	}
	
	@Override
	public String getType()
	{
		return _C__5F_REQUESTANSWERFRIENDINVITE;
	}
}
