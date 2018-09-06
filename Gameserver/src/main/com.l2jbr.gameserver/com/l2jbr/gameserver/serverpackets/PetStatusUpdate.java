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

import com.l2jbr.gameserver.model.L2Summon;
import com.l2jbr.gameserver.model.actor.instance.L2PetInstance;


/**
 * This class ...
 * @version $Revision: 1.5.2.3.2.5 $ $Date: 2005/03/29 23:15:10 $
 */
public class PetStatusUpdate extends L2GameServerPacket
{
	private static final String _S__CE_PETSTATUSSHOW = "[S] B5 PetStatusUpdate";
	
	private final L2Summon _summon;
	private final int _maxHp, _maxMp;
	private int _maxFed, _curFed;
	
	public PetStatusUpdate(L2Summon summon)
	{
		_summon = summon;
		_maxHp = _summon.getMaxHp();
		_maxMp = _summon.getMaxMp();
		if (_summon instanceof L2PetInstance)
		{
			L2PetInstance pet = (L2PetInstance) _summon;
			_curFed = pet.getCurrentFed(); // how fed it is
			_maxFed = pet.getMaxFed(); // max fed it can be
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		writeByte(0xb5);
		writeInt(_summon.getSummonType());
		writeInt(_summon.getObjectId());
		writeInt(_summon.getX());
		writeInt(_summon.getY());
		writeInt(_summon.getZ());
		writeString(_summon.getTitle());
		writeInt(_curFed);
		writeInt(_maxFed);
		writeInt((int) _summon.getCurrentHp());
		writeInt(_maxHp);
		writeInt((int) _summon.getCurrentMp());
		writeInt(_maxMp);
		writeInt(_summon.getLevel());
		writeLong(_summon.getStat().getExp());
		writeLong(_summon.getExpForThisLevel());// 0% absolute value
		writeLong(_summon.getExpForNextLevel());// 100% absolute value
	}
	
	@Override
	public String getType()
	{
		return _S__CE_PETSTATUSSHOW;
	}
}
