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

import com.l2jbr.commons.Config;
import com.l2jbr.gameserver.datatables.NpcTable;
import com.l2jbr.gameserver.instancemanager.CursedWeaponsManager;
import com.l2jbr.gameserver.model.Inventory;
import com.l2jbr.gameserver.model.L2Summon;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.model.entity.database.NpcTemplate;


/**
 * 0000: 04 03 15 00 00 77 ff 00 00 80 f1 ff ff 00 00 00 .....w.......... 0010: 00 2a 89 00 4c 43 00 61 00 6c 00 61 00 64 00 6f .*..LC.a.l.a.d.o 0020: 00 6e 00 00 00 01 00 00 00 00 00 00 00 19 00 00 .n.............. 0030: 00 0d 00 00 00 ee 81 02 00 15 00 00 00 18 00 00 ................ 0040: 00 19
 * 00 00 00 25 00 00 00 17 00 00 00 28 00 00 .....%.......(.. 0050: 00 14 01 00 00 14 01 00 00 02 01 00 00 02 01 00 ................ 0060: 00 fa 09 00 00 81 06 00 00 26 34 00 00 2e 00 00 .........&4..... 0070: 00 00 00 00 00 db 9f a1 41 93 26 64 41 de c8 31 ........A.&dA..1 0080: 41 ca 73 c0 41 d5
 * 22 d0 41 83 bd 41 41 81 56 10 A.s.A.".A..AA.V. 0090: 41 00 00 00 00 27 7d 30 41 69 aa e0 40 b4 fb d3 A....'}0Ai..@... 00a0: 41 91 f9 63 41 00 00 00 00 81 56 10 41 00 00 00 A..cA.....V.A... 00b0: 00 71 00 00 00 71 00 00 00 76 00 00 00 74 00 00 .q...q...v...t.. 00c0: 00 74 00 00 00 2a 00 00 00 e8
 * 02 00 00 00 00 00 .t...*.......... 00d0: 00 5f 04 00 00 ac 01 00 00 cf 01 00 00 62 04 00 ._...........b.. 00e0: 00 00 00 00 00 e8 02 00 00 0b 00 00 00 52 01 00 .............R.. 00f0: 00 4d 00 00 00 2a 00 00 00 2f 00 00 00 29 00 00 .M...*.../...).. 0100: 00 12 00 00 00 82 01 00 00 52 01 00 00 53
 * 00 00 .........R...S.. 0110: 00 00 00 00 00 00 00 00 00 7a 00 00 00 55 00 00 .........z...U.. 0120: 00 32 00 00 00 32 00 00 00 00 00 00 00 00 00 00 .2...2.......... 0130: 00 00 00 00 00 00 00 00 00 a4 70 3d 0a d7 a3 f0 ..........p=.... 0140: 3f 64 5d dc 46 03 78 f3 3f 00 00 00 00 00 00 1e
 * ?d].F.x.?....... 0150: 40 00 00 00 00 00 00 38 40 02 00 00 00 01 00 00 @......8@....... 0160: 00 00 00 00 00 00 00 00 00 00 00 c1 0c 00 00 01 ................ 0170: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ................ 0180: 00 00 00 00 ....
 * dddddSdddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddffffddddSdddcccdd (h) dddddSddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd ffffddddSdddddcccddh (h) c dc hhdh
 * dddddSdddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddffffddddSdddddcccddh (h) c dc hhdh ddddc c dcc cddd d (from 654) but it actually reads dddddSdddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddffffddddSdddddcccddh (h) c dc *dddddddd* hhdh ddddc dcc
 * cddd d *...*: here i am not sure at least it looks like it reads that much data (32 bytes), not sure about the format inside because it is not read thanks to the ususal parsing function
 * dddddSddddQddddddddddddddddddddddddddddddddddddddddddddddddhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhddddddddddddddddddddffffddddSdddddcccddh [h] c dc d hhdh ddddc c dcc cddd d c dd d d @version $Revision: 1.14.2.4.2.12 $ $Date: 2005/04/11 10:05:55 $
 */
public class UserInfo extends L2GameServerPacket
{
	private static final String _S__04_USERINFO = "[S] 04 UserInfo";
	private final L2PcInstance _activeChar;
	private final int _runSpd, _walkSpd, _swimRunSpd, _swimWalkSpd;
	private int _flRunSpd;
	private int _flWalkSpd;
	private int _flyRunSpd;
	private int _flyWalkSpd;
	private int _relation;
	private final float _moveMultiplier;
	
	/**
	 * @param character
	 */
	public UserInfo(L2PcInstance character)
	{
		_activeChar = character;
		
		_moveMultiplier = _activeChar.getMovementSpeedMultiplier();
		_runSpd = (int) (_activeChar.getRunSpeed() / _moveMultiplier);
		_walkSpd = (int) (_activeChar.getWalkSpeed() / _moveMultiplier);
		_swimRunSpd = _flRunSpd = _flyRunSpd = _runSpd;
		_swimWalkSpd = _flWalkSpd = _flyWalkSpd = _walkSpd;
		_relation = _activeChar.isClanLeader() ? 0x40 : 0;
		if (_activeChar.getSiegeState() == 1)
		{
			_relation |= 0x180;
		}
		if (_activeChar.getSiegeState() == 2)
		{
			_relation |= 0x80;
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		writeByte(0x04);
		
		writeInt(_activeChar.getX());
		writeInt(_activeChar.getY());
		writeInt(_activeChar.getZ());
		writeInt(_activeChar.getHeading());
		writeInt(_activeChar.getObjectId());
		writeString(_activeChar.getName());
		writeInt(_activeChar.getRace().ordinal());
		writeInt(_activeChar.getAppearance().getSex() ? 1 : 0);
		
		if (_activeChar.getClassIndex() == 0)
		{
			writeInt(_activeChar.getPlayerClass().getId());
		}
		else
		{
			writeInt(_activeChar.getBaseClass());
		}
		
		writeInt(_activeChar.getLevel());
		writeLong(_activeChar.getExp());
		writeInt(_activeChar.getSTR());
		writeInt(_activeChar.getDEX());
		writeInt(_activeChar.getCON());
		writeInt(_activeChar.getINT());
		writeInt(_activeChar.getWIT());
		writeInt(_activeChar.getMEN());
		writeInt(_activeChar.getMaxHp());
		writeInt((int) _activeChar.getCurrentHp());
		writeInt(_activeChar.getMaxMp());
		writeInt((int) _activeChar.getCurrentMp());
		writeInt(_activeChar.getSp());
		writeInt(_activeChar.getCurrentLoad());
		writeInt(_activeChar.getMaxLoad());
		
		writeInt(0x28); // unknown
		
		writeInt(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_DHAIR));
		writeInt(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_REAR));
		writeInt(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LEAR));
		writeInt(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_NECK));
		writeInt(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_RFINGER));
		writeInt(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LFINGER));
		writeInt(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_HEAD));
		writeInt(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_RHAND));
		writeInt(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LHAND));
		writeInt(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_GLOVES));
		writeInt(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_CHEST));
		writeInt(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LEGS));
		writeInt(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_FEET));
		writeInt(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_BACK));
		writeInt(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LRHAND));
		writeInt(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_HAIR));
		writeInt(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_FACE));
		
		writeInt(_activeChar.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_DHAIR));
		writeInt(_activeChar.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_REAR));
		writeInt(_activeChar.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LEAR));
		writeInt(_activeChar.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_NECK));
		writeInt(_activeChar.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_RFINGER));
		writeInt(_activeChar.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LFINGER));
		writeInt(_activeChar.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_HEAD));
		writeInt(_activeChar.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_RHAND));
		writeInt(_activeChar.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LHAND));
		writeInt(_activeChar.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_GLOVES));
		writeInt(_activeChar.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_CHEST));
		writeInt(_activeChar.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LEGS));
		writeInt(_activeChar.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_FEET));
		writeInt(_activeChar.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_BACK));
		writeInt(_activeChar.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LRHAND));
		writeInt(_activeChar.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_HAIR));
		writeInt(_activeChar.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_FACE));
		
		// c6 new h's
		writeShort(0x00);
		writeShort(0x00);
		writeShort(0x00);
		writeShort(0x00);
		writeShort(0x00);
		writeShort(0x00);
		writeShort(0x00);
		writeShort(0x00);
		writeShort(0x00);
		writeShort(0x00);
		writeShort(0x00);
		writeShort(0x00);
		writeShort(0x00);
		writeShort(0x00);
		writeInt(_activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_RHAND));
		writeShort(0x00);
		writeShort(0x00);
		writeShort(0x00);
		writeShort(0x00);
		writeShort(0x00);
		writeShort(0x00);
		writeShort(0x00);
		writeShort(0x00);
		writeShort(0x00);
		writeShort(0x00);
		writeShort(0x00);
		writeShort(0x00);
		writeInt(_activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_LRHAND));
		writeShort(0x00);
		writeShort(0x00);
		writeShort(0x00);
		writeShort(0x00);
		// end of c6 new h's
		
		writeInt(_activeChar.getPAtk(null));
		writeInt(_activeChar.getPAtkSpd());
		writeInt(_activeChar.getPDef(null));
		writeInt(_activeChar.getEvasionRate(null));
		writeInt(_activeChar.getAccuracy());
		writeInt(_activeChar.getCriticalHit(null, null));
		writeInt(_activeChar.getMAtk(null, null));
		
		writeInt(_activeChar.getMAtkSpd());
		writeInt(_activeChar.getPAtkSpd());
		
		writeInt(_activeChar.getMDef(null, null));
		
		writeInt(_activeChar.getPvpFlag()); // 0-non-pvp 1-pvp = violett name
		writeInt(_activeChar.getKarma());
		
		writeInt(_runSpd);
		writeInt(_walkSpd);
		writeInt(_swimRunSpd); // swimspeed
		writeInt(_swimWalkSpd); // swimspeed
		writeInt(_flRunSpd);
		writeInt(_flWalkSpd);
		writeInt(_flyRunSpd);
		writeInt(_flyWalkSpd);
		writeDouble(_moveMultiplier);
		writeDouble(_activeChar.getAttackSpeedMultiplier());
		
		L2Summon pet = _activeChar.getPet();
		if ((_activeChar.getMountType() != 0) && (pet != null))
		{
			writeDouble(pet.getTemplate().getCollisionRadius());
			writeDouble(pet.getTemplate().getCollisionHeight());
		}
		else
		{
			writeDouble(_activeChar.getBaseTemplate().getCollisionRadius());
			writeDouble(_activeChar.getBaseTemplate().getCollisionHeight());
		}
		
		writeInt(_activeChar.getAppearance().getHairStyle());
		writeInt(_activeChar.getAppearance().getHairColor());
		writeInt(_activeChar.getAppearance().getFace());
		writeInt((_activeChar.getAccessLevel() >= Config.GM_ALTG_MIN_LEVEL) ? 1 : 0); // builder level
		
		String title = _activeChar.getTitle();
		if (_activeChar.getAppearance().getInvisible() && _activeChar.isGM())
		{
			title = "Invisible";
		}
		if (_activeChar.getPoly().isMorphed())
		{
			NpcTemplate polyObj = NpcTable.getInstance().getTemplate(_activeChar.getPoly().getPolyId());
			if (polyObj != null)
			{
				title += " - " + polyObj.getName();
			}
		}
		writeString(title);
		
		writeInt(_activeChar.getClanId());
		writeInt(_activeChar.getClanCrestId());
		writeInt(_activeChar.getAllyId());
		writeInt(_activeChar.getAllyCrestId()); // ally crest id
		// 0x40 leader rights
		// siege flags: attacker - 0x180 sword over name, defender - 0x80 shield, 0xC0 crown (|leader), 0x1C0 flag (|leader)
		writeInt(_relation);
		writeByte(_activeChar.getMountType()); // mount type
		writeByte(_activeChar.getPrivateStoreType());
		writeByte(_activeChar.hasDwarvenCraft() ? 1 : 0);
		writeInt(_activeChar.getPkKills());
		writeInt(_activeChar.getPvpKills());
		
		writeShort(_activeChar.getCubics().size());
		for (int id : _activeChar.getCubics().keySet())
		{
			writeShort(id);
		}
		
		writeByte(0x00); // 1-find party members
		
		writeInt(_activeChar.getAbnormalEffect());
		writeByte(0x00);
		
		writeInt(_activeChar.getClanPrivileges());
		
		writeShort(_activeChar.getRecomLeft()); // c2 recommendations remaining
		writeShort(_activeChar.getRecomHave()); // c2 recommendations received
		writeInt(0x00);
		writeShort(_activeChar.GetInventoryLimit());
		
		writeInt(_activeChar.getPlayerClass().getId());
		writeInt(0x00); // special effects? circles around player...
		writeInt(_activeChar.getMaxCp());
		writeInt((int) _activeChar.getCurrentCp());
		writeByte(_activeChar.isMounted() ? 0 : _activeChar.getEnchantEffect());
		
		if (_activeChar.getTeam() == 1)
		{
			writeByte(0x01); // team circle around feet 1= Blue, 2 = red
		}
		else if (_activeChar.getTeam() == 2)
		{
			writeByte(0x02); // team circle around feet 1= Blue, 2 = red
		}
		else
		{
			writeByte(0x00); // team circle around feet 1= Blue, 2 = red
		}
		
		writeInt(_activeChar.getClanCrestLargeId());
		writeByte(_activeChar.isNoble() ? 1 : 0); // 0x01: symbol on char menu ctrl+I
		writeByte((_activeChar.isHero() || (_activeChar.isGM() && Config.GM_HERO_AURA)) ? 1 : 0); // 0x01: Hero Aura
		
		writeByte(_activeChar.isFishing() ? 1 : 0); // Fishing Mode
		writeInt(_activeChar.GetFishx()); // fishing x
		writeInt(_activeChar.GetFishy()); // fishing y
		writeInt(_activeChar.GetFishz()); // fishing z
		writeInt(_activeChar.getAppearance().getNameColor());
		
		// new c5
		writeByte(_activeChar.isRunning() ? 0x01 : 0x00); // changes the Speed display on Status Window
		
		writeInt(_activeChar.getPledgeClass()); // changes the text above CP on Status Window
		writeInt(0x00); // ??
		
		writeInt(_activeChar.getAppearance().getTitleColor());
		
		// writeInt(0x00); // ??
		
		if (_activeChar.isCursedWeaponEquiped())
		{
			writeInt(CursedWeaponsManager.getInstance().getLevel(_activeChar.getCursedWeaponEquipedId()));
		}
		else
		{
			writeInt(0x00);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jbr.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__04_USERINFO;
	}
}
