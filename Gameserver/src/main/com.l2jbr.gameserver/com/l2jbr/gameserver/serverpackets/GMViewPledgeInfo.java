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

import com.l2jbr.gameserver.model.L2Clan;
import com.l2jbr.gameserver.model.L2ClanMember;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;


/**
 * format SdSS dddddddd d (Sddddd)
 * @version $Revision: 1.1.2.1.2.3 $ $Date: 2005/03/27 15:29:57 $
 */
public class GMViewPledgeInfo extends L2GameServerPacket
{
	private static final String _S__A9_GMVIEWPLEDGEINFO = "[S] 90 GMViewPledgeInfo";
	private final L2Clan _clan;
	private final L2PcInstance _activeChar;
	
	public GMViewPledgeInfo(L2Clan clan, L2PcInstance activeChar)
	{
		_clan = clan;
		_activeChar = activeChar;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeByte(0x90);
		writeString(_activeChar.getName());
		writeInt(_clan.getClanId());
		writeInt(0x00);
		writeString(_clan.getName());
		writeString(_clan.getLeaderName());
		writeInt(_clan.getCrestId()); // -> no, it's no longer used (nuocnam) fix by game
		writeInt(_clan.getLevel());
		writeInt(_clan.getCastle());
		writeInt(_clan.getHasHideout());
		writeInt(_clan.getRank());
		writeInt(_clan.getReputationScore());
		writeInt(0);
		writeInt(0);
		
		writeInt(_clan.getAllyId()); // c2
		writeString(_clan.getAllyName()); // c2
		writeInt(_clan.getAllyCrestId()); // c2
		writeInt(_clan.isAtWar()); // c3
		
		L2ClanMember[] members = _clan.getMembers();
		writeInt(members.length);
		
		for (L2ClanMember member : members)
		{
			writeString(member.getName());
			writeInt(member.getLevel());
			writeInt(member.getClassId());
			writeInt(0);
			writeInt(1);
			writeInt(member.isOnline() ? member.getObjectId() : 0);
			writeInt(0);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jbr.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__A9_GMVIEWPLEDGEINFO;
	}
	
}
