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

import com.l2jbr.gameserver.datatables.NpcTable;
import com.l2jbr.gameserver.model.L2Character;
import com.l2jbr.gameserver.model.L2ItemInstance;
import com.l2jbr.gameserver.model.L2Object;
import com.l2jbr.gameserver.model.entity.database.NpcTemplate;


/**
 * This class ...
 * @version $Revision: 1.7.2.4.2.9 $ $Date: 2005/04/11 10:05:54 $
 */
public class NpcInfoPoly extends L2GameServerPacket
{
	// ddddddddddddddddddffffdddcccccSSddd dddddc
	private static final String _S__22_NPCINFO = "[S] 16 NpcInfo";
	private L2Character _activeChar;
	private final L2Object _obj;
	private int _x, _y, _z, _heading;
	private final int _npcId;
	private boolean _isAttackable;
	private final boolean _isSummoned;
	private boolean _isRunning;
	private boolean _isInCombat;
	private boolean _isAlikeDead;
	private int _mAtkSpd, _pAtkSpd;
	private int _runSpd, _walkSpd, _swimRunSpd, _swimWalkSpd, _flRunSpd, _flWalkSpd, _flyRunSpd, _flyWalkSpd;
	private int _rhand, _lhand;
	private String _name, _title;
	private int _abnormalEffect;
	NpcTemplate _template;
	private final float _collisionRadius;
	private final float _collisionHeight;
	
	/**
	 * @param obj
	 * @param attacker
	 */
	public NpcInfoPoly(L2Object obj, L2Character attacker)
	{
		_obj = obj;
		_npcId = obj.getPoly().getPolyId();
		_template = NpcTable.getInstance().getTemplate(_npcId);
		_isAttackable = true;
		_rhand = 0;
		_lhand = 0;
		_isSummoned = false;
		_collisionRadius = _template.getCollisionRadius();
		_collisionHeight = _template.getCollisionHeight();
		if (_obj instanceof L2Character)
		{
			_activeChar = (L2Character) obj;
			_isAttackable = obj.isAutoAttackable(attacker);
			_rhand = _template.getRhand();
			_lhand = _template.getLhand();
			
		}
		
		if (_obj instanceof L2ItemInstance)
		{
			_x = _obj.getX();
			_y = _obj.getY();
			_z = _obj.getZ();
			_heading = 0;
			_mAtkSpd = 100; // yes, an item can be dread as death
			_pAtkSpd = 100;
			_runSpd = 120;
			_walkSpd = 80;
			_swimRunSpd = _flRunSpd = _flyRunSpd = _runSpd;
			_swimWalkSpd = _flWalkSpd = _flyWalkSpd = _walkSpd;
			_isRunning = _isInCombat = _isAlikeDead = false;
			_name = "item";
			_title = "polymorphed";
			_abnormalEffect = 0;
		}
		else
		{
			_x = _activeChar.getX();
			_y = _activeChar.getY();
			_z = _activeChar.getZ();
			_heading = _activeChar.getHeading();
			_mAtkSpd = _activeChar.getMAtkSpd();
			_pAtkSpd = _activeChar.getPAtkSpd();
			_runSpd = _activeChar.getRunSpeed();
			_walkSpd = _activeChar.getWalkSpeed();
			_swimRunSpd = _flRunSpd = _flyRunSpd = _runSpd;
			_swimWalkSpd = _flWalkSpd = _flyWalkSpd = _walkSpd;
			_isRunning = _activeChar.isRunning();
			_isInCombat = _activeChar.isInCombat();
			_isAlikeDead = _activeChar.isAlikeDead();
			_name = _activeChar.getName();
			_title = _activeChar.getTitle();
			_abnormalEffect = _activeChar.getAbnormalEffect();
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		writeByte(0x16);
		writeInt(_obj.getObjectId());
		writeInt(_npcId + 1000000); // npctype id
		writeInt(_isAttackable ? 1 : 0);
		writeInt(_x);
		writeInt(_y);
		writeInt(_z);
		writeInt(_heading);
		writeInt(0x00);
		writeInt(_mAtkSpd);
		writeInt(_pAtkSpd);
		writeInt(_runSpd);
		writeInt(_walkSpd);
		writeInt(_swimRunSpd/* 0x32 */); // swimspeed
		writeInt(_swimWalkSpd/* 0x32 */); // swimspeed
		writeInt(_flRunSpd);
		writeInt(_flWalkSpd);
		writeInt(_flyRunSpd);
		writeInt(_flyWalkSpd);
		writeDouble(1/* _activeChar.getProperMultiplier() */);
		writeDouble(1/* _activeChar.getAttackSpeedMultiplier() */);
		writeDouble(_collisionRadius);
		writeDouble(_collisionHeight);
		writeInt(_rhand); // right hand weapon
		writeInt(0);
		writeInt(_lhand); // left hand weapon
		writeByte(1); // name above char 1=true ... ??
		writeByte(_isRunning ? 1 : 0);
		writeByte(_isInCombat ? 1 : 0);
		writeByte(_isAlikeDead ? 1 : 0);
		writeByte(_isSummoned ? 2 : 0); // invisible ?? 0=false 1=true 2=summoned (only works if model has a summon animation)
		writeString(_name);
		writeString(_title);
		writeInt(0);
		writeInt(0);
		writeInt(0000); // hmm karma ??
		
		writeShort(_abnormalEffect); // C2
		writeShort(0x00); // C2
		writeInt(0000); // C2
		writeInt(0000); // C2
		writeInt(0000); // C2
		writeInt(0000); // C2
		writeByte(0000); // C2
	}
	
	@Override
	public String getType()
	{
		return _S__22_NPCINFO;
	}
}
