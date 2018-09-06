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

import com.l2jbr.gameserver.model.entity.Heroes;
import com.l2jbr.gameserver.model.entity.database.Hero;

import java.util.Map;


/**
 * Format: (ch) d [SdSdSdd] d: size [ S: hero name d: hero class ID S: hero clan name d: hero clan crest id S: hero ally name d: hero Ally id d: count ]
 * @author -Wooden- Format from KenM Re-written by godson
 */
public class ExHeroList extends L2GameServerPacket
{
	private static final String _S__FE_23_EXHEROLIST = "[S] FE:23 ExHeroList";
	private final Map<Integer, Hero> _heroList;
	
	public ExHeroList()
	{
		_heroList = Heroes.getInstance().getHeroes();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jbr.gameserver.serverpackets.ServerBasePacket#writeImpl()
	 */
	@Override
	protected void writeImpl()
	{
		writeByte(0xfe);
		writeShort(0x23);
		writeInt(_heroList.size());

        for (Hero hero: _heroList.values()) {
            writeString(hero.getCharName());
            writeInt(hero.getClassId());
            writeString(hero.getClanName());
            writeInt(hero.getclanCrest());
            writeString(hero.getAllyName());
            writeInt(hero.getAllyCrest());
            writeInt(hero.getCount());
        }
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jbr.gameserver.BasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__FE_23_EXHEROLIST;
	}
	
}