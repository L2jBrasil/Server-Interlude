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

package com.l2jbr.gameserver.handler.usercommandhandlers;

import com.l2jbr.commons.database.DatabaseAccess;
import com.l2jbr.commons.database.L2DatabaseFactory;
import com.l2jbr.gameserver.handler.IUserCommandHandler;
import com.l2jbr.gameserver.model.L2Clan;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.model.entity.database.ClanData;
import com.l2jbr.gameserver.model.entity.database.repository.ClanRepository;
import com.l2jbr.gameserver.network.SystemMessageId;
import com.l2jbr.gameserver.serverpackets.SystemMessage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;


/**
 * Support for /clanwarlist command
 * @author Tempy
 */
public class ClanWarsList implements IUserCommandHandler
{
	private static final int[] COMMAND_IDS =
	{
		88,
		89,
		90
	};
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jbr.gameserver.handler.IUserCommandHandler#useUserCommand(int, com.l2jbr.gameserver.model.L2PcInstance)
	 */
	@Override
	public boolean useUserCommand(int id, L2PcInstance activeChar)
	{
		if ((id != COMMAND_IDS[0]) && (id != COMMAND_IDS[1]) && (id != COMMAND_IDS[2]))
		{
			return false;
		}
		
		L2Clan clan = activeChar.getClan();
		
		if (clan == null)
		{
			activeChar.sendMessage("You are not in a clan.");
			return false;
		}

        Iterable<ClanData> clans;
        ClanRepository repository = DatabaseAccess.getRepository(ClanRepository.class);
        if (id == 88) {
            // Attack List
            activeChar.sendPacket(new SystemMessage(SystemMessageId.CLANS_YOU_DECLARED_WAR_ON));
            clans = repository.findAllByOnlyAttacker(clan.getClanId());
        }
        else if (id == 89) {
            // Under Attack List
            activeChar.sendPacket(new SystemMessage(SystemMessageId.CLANS_THAT_HAVE_DECLARED_WAR_ON_YOU));
            clans = repository.findAllByOnlyUnderAttack(clan.getClanId());
        }
        else { // ID = 90
            // War List
            activeChar.sendPacket(new SystemMessage(SystemMessageId.WAR_LIST));
            clans = repository.findAllInWar(clan.getClanId());
        }

        clans.forEach(clanData -> {
            String clanName = clanData.getClanName();
            int ally_id = clanData.getAllyId();

            SystemMessage sm;
            if (ally_id > 0) {
                // Target With Ally
                sm = new SystemMessage(SystemMessageId.S1_S2_ALLIANCE);
                sm.addString(clanName);
                sm.addString(clanData.getAllyName());
            }
            else  {
                // Target Without Ally
                sm = new SystemMessage(SystemMessageId.S1_NO_ALLI_EXISTS);
                sm.addString(clanName);
            }
            activeChar.sendPacket(sm);
        });

        activeChar.sendPacket(new SystemMessage(SystemMessageId.FRIEND_LIST_FOOT));
		
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jbr.gameserver.handler.IUserCommandHandler#getUserCommandList()
	 */
	@Override
	public int[] getUserCommandList()
	{
		return COMMAND_IDS;
	}
}
