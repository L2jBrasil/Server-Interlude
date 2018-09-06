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
import com.l2jbr.gameserver.model.L2Character;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.model.entity.database.NpcTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 0000: 03 32 15 00 00 44 fe 00 00 80 f1 ff ff 00 00 00 .2...D..........
 * <p>
 * 0010: 00 6b b4 c0 4a 45 00 6c 00 6c 00 61 00 6d 00 69 .k..JE.l.l.a.m.i
 * <p>
 * 0020: 00 00 00 01 00 00 00 01 00 00 00 12 00 00 00 00 ................
 * <p>
 * 0030: 00 00 00 2a 00 00 00 42 00 00 00 71 02 00 00 31 ...*...B...q...1
 * <p>
 * 0040: 00 00 00 18 00 00 00 1f 00 00 00 25 00 00 00 00 ...........%....
 * <p>
 * 0050: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 f9 ................
 * <p>
 * 0060: 00 00 00 b3 01 00 00 00 00 00 00 00 00 00 00 7d ...............}
 * <p>
 * 0070: 00 00 00 5a 00 00 00 32 00 00 00 32 00 00 00 00 ...Z...2...2....
 * <p>
 * 0080: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 67 ...............g
 * <p>
 * 0090: 66 66 66 66 66 f2 3f 5f 63 97 a8 de 1a f9 3f 00 fffff.?_c.....?.
 * <p>
 * 00a0: 00 00 00 00 00 1e 40 00 00 00 00 00 00 37 40 01 .............7..
 * <p>
 * 00b0: 00 00 00 01 00 00 00 01 00 00 00 00 00 c1 0c 00 ................
 * <p>
 * 00c0: 00 00 00 00 00 00 00 00 00 01 01 00 00 00 00 00 ................
 * <p>
 * 00d0: 00 00
 * <p>
 * <p>
 * dddddSdddddddddddddddddddddddddddffffdddSdddccccccc (h)
 * <p>
 * dddddSdddddddddddddddddddddddddddffffdddSdddddccccccch dddddSddddddddddddddddddddddddddddffffdddSdddddccccccch (h) c (dchd) ddc dcc c cddd d dddddSdddddddddddddddhhhhhhhhhhhhhhhhhhhhhhhhddddddddddddddffffdddSdddddccccccch [h] c (ddhd) ddc c ddc cddd d d dd d d d @version $Revision: 1.7.2.6.2.11 $
 * $Date: 2005/04/11 10:05:54 $
 */
public class CharInfo extends L2GameServerPacket
{
	private static final Logger _log = LoggerFactory.getLogger(CharInfo.class.getName());
	
	private static final String _S__03_CHARINFO = "[S] 03 CharInfo";
	private final L2PcInstance _activeChar;
	private final Inventory _inv;
	private final int _x, _y, _z, _heading;
	private final int _mAtkSpd, _pAtkSpd;
	private final int _runSpd, _walkSpd, _swimRunSpd, _swimWalkSpd;
	
	private int _flRunSpd;
	
	private int _flWalkSpd;
	
	private int _flyRunSpd;
	
	private int _flyWalkSpd;
	private final float _moveMultiplier, _attackSpeedMultiplier;
	private final int _maxCp;
	
	/**
	 * @param cha
	 */
	public CharInfo(L2PcInstance cha)
	{
		_activeChar = cha;
		_inv = cha.getInventory();
		_x = _activeChar.getX();
		_y = _activeChar.getY();
		_z = _activeChar.getZ();
		_heading = _activeChar.getHeading();
		_mAtkSpd = _activeChar.getMAtkSpd();
		_pAtkSpd = _activeChar.getPAtkSpd();
		_moveMultiplier = _activeChar.getMovementSpeedMultiplier();
		_attackSpeedMultiplier = _activeChar.getAttackSpeedMultiplier();
		_runSpd = (int) (_activeChar.getRunSpeed() / _moveMultiplier);
		_walkSpd = (int) (_activeChar.getWalkSpeed() / _moveMultiplier);
		_swimRunSpd = _flRunSpd = _flyRunSpd = _runSpd;
		_swimWalkSpd = _flWalkSpd = _flyWalkSpd = _walkSpd;
		_maxCp = _activeChar.getMaxCp();
	}
	
	@Override
	protected final void writeImpl()
	{
		boolean gmSeeInvis = false;
		
		if (_activeChar.getAppearance().getInvisible())
		{
			L2PcInstance tmp = getClient().getActiveChar();
			if ((tmp != null) && tmp.isGM())
			{
				gmSeeInvis = true;
			}
			else
			{
				return;
			}
		}
		
		if (_activeChar.getPoly().isMorphed())
		{
			NpcTemplate template = NpcTable.getInstance().getTemplate(_activeChar.getPoly().getPolyId());
			
			if (template != null)
			{
				writeByte(0x16);
				writeInt(_activeChar.getObjectId());
				writeInt(_activeChar.getPoly().getPolyId() + 1000000); // npctype id
				writeInt(_activeChar.getKarma() > 0 ? 1 : 0);
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
				writeDouble(_moveMultiplier);
				writeDouble(_attackSpeedMultiplier);
				writeDouble(template.getCollisionRadius());
				writeDouble(template.getCollisionHeight());
				writeInt(_inv.getPaperdollItemId(Inventory.PAPERDOLL_RHAND)); // right hand weapon
				writeInt(0);
				writeInt(_inv.getPaperdollItemId(Inventory.PAPERDOLL_LHAND)); // left hand weapon
				writeByte(1); // name above char 1=true ... ??
				writeByte(_activeChar.isRunning() ? 1 : 0);
				writeByte(_activeChar.isInCombat() ? 1 : 0);
				writeByte(_activeChar.isAlikeDead() ? 1 : 0);
				
				if (gmSeeInvis)
				{
					writeByte(0);
				}
				else
				{
					writeByte(_activeChar.getAppearance().getInvisible() ? 1 : 0); // invisible ?? 0=false 1=true 2=summoned (only works if model has a summon animation)
				}
				
				writeString(_activeChar.getName());
				
				if (gmSeeInvis)
				{
					writeString("Invisible");
				}
				else
				{
					writeString(_activeChar.getTitle());
				}
				
				writeInt(0);
				writeInt(0);
				writeInt(0000); // hmm karma ??
				
				if (gmSeeInvis)
				{
					writeInt((_activeChar.getAbnormalEffect() | L2Character.ABNORMAL_EFFECT_STEALTH));
				}
				else
				{
					writeInt(_activeChar.getAbnormalEffect()); // C2
				}
				
				writeInt(0); // C2
				writeInt(0); // C2
				writeInt(0); // C2
				writeInt(0); // C2
				writeByte(0); // C2
			}
			else
			{
				_log.warn("Character " + _activeChar.getName() + " (" + _activeChar.getObjectId() + ") morphed in a NpcTemplate (" + _activeChar.getPoly().getPolyId() + ") w/o template.");
			}
		}
		else
		{
			writeByte(0x03);
			writeInt(_x);
			writeInt(_y);
			writeInt(_z);
			writeInt(_heading);
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
			
			writeInt(_inv.getPaperdollItemId(Inventory.PAPERDOLL_DHAIR));
			writeInt(_inv.getPaperdollItemId(Inventory.PAPERDOLL_HEAD));
			writeInt(_inv.getPaperdollItemId(Inventory.PAPERDOLL_RHAND));
			writeInt(_inv.getPaperdollItemId(Inventory.PAPERDOLL_LHAND));
			writeInt(_inv.getPaperdollItemId(Inventory.PAPERDOLL_GLOVES));
			writeInt(_inv.getPaperdollItemId(Inventory.PAPERDOLL_CHEST));
			writeInt(_inv.getPaperdollItemId(Inventory.PAPERDOLL_LEGS));
			writeInt(_inv.getPaperdollItemId(Inventory.PAPERDOLL_FEET));
			writeInt(_inv.getPaperdollItemId(Inventory.PAPERDOLL_BACK));
			writeInt(_inv.getPaperdollItemId(Inventory.PAPERDOLL_LRHAND));
			writeInt(_inv.getPaperdollItemId(Inventory.PAPERDOLL_HAIR));
			writeInt(_inv.getPaperdollItemId(Inventory.PAPERDOLL_FACE));
			
			// c6 new h's
			writeShort(0x00);
			writeShort(0x00);
			writeShort(0x00);
			writeShort(0x00);
			writeInt(_inv.getPaperdollAugmentationId(Inventory.PAPERDOLL_RHAND));
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
			writeInt(_inv.getPaperdollAugmentationId(Inventory.PAPERDOLL_LRHAND));
			writeShort(0x00);
			writeShort(0x00);
			writeShort(0x00);
			writeShort(0x00);
			
			writeInt(_activeChar.getPvpFlag());
			writeInt(_activeChar.getKarma());
			
			writeInt(_mAtkSpd);
			writeInt(_pAtkSpd);
			
			writeInt(_activeChar.getPvpFlag());
			writeInt(_activeChar.getKarma());
			
			writeInt(_runSpd);
			writeInt(_walkSpd);
			writeInt(_swimRunSpd/* 0x32 */); // swimspeed
			writeInt(_swimWalkSpd/* 0x32 */); // swimspeed
			writeInt(_flRunSpd);
			writeInt(_flWalkSpd);
			writeInt(_flyRunSpd);
			writeInt(_flyWalkSpd);
			writeDouble(_activeChar.getMovementSpeedMultiplier()); // _activeChar.getProperMultiplier()
			writeDouble(_activeChar.getAttackSpeedMultiplier()); // _activeChar.getAttackSpeedMultiplier()
			writeDouble(_activeChar.getBaseTemplate().getCollisionRadius());
			writeDouble(_activeChar.getBaseTemplate().getCollisionHeight());
			
			writeInt(_activeChar.getAppearance().getHairStyle());
			writeInt(_activeChar.getAppearance().getHairColor());
			writeInt(_activeChar.getAppearance().getFace());
			
			if (gmSeeInvis)
			{
				writeString("Invisible");
			}
			else
			{
				writeString(_activeChar.getTitle());
			}
			
			writeInt(_activeChar.getClanId());
			writeInt(_activeChar.getClanCrestId());
			writeInt(_activeChar.getAllyId());
			writeInt(_activeChar.getAllyCrestId());
			// In UserInfo leader rights and siege flags, but here found nothing??
			// Therefore RelationChanged packet with that info is required
			writeInt(0);
			
			writeByte(_activeChar.isSitting() ? 0 : 1); // standing = 1 sitting = 0
			writeByte(_activeChar.isRunning() ? 1 : 0); // running = 1 walking = 0
			writeByte(_activeChar.isInCombat() ? 1 : 0);
			writeByte(_activeChar.isAlikeDead() ? 1 : 0);
			
			if (gmSeeInvis)
			{
				writeByte(0);
			}
			else
			{
				writeByte(_activeChar.getAppearance().getInvisible() ? 1 : 0); // invisible = 1 visible =0
			}
			
			writeByte(_activeChar.getMountType()); // 1 on strider 2 on wyvern 0 no mount
			writeByte(_activeChar.getPrivateStoreType()); // 1 - sellshop
			
			writeShort(_activeChar.getCubics().size());
			for (int id : _activeChar.getCubics().keySet())
			{
				writeShort(id);
			}
			
			writeByte(0x00); // find party members
			
			if (gmSeeInvis)
			{
				writeInt((_activeChar.getAbnormalEffect() | L2Character.ABNORMAL_EFFECT_STEALTH));
			}
			else
			{
				writeInt(_activeChar.getAbnormalEffect());
			}
			
			writeByte(_activeChar.getRecomLeft()); // Changed by Thorgrim
			writeShort(_activeChar.getRecomHave()); // Blue value for name (0 = white, 255 = pure blue)
			writeInt(_activeChar.getPlayerClass().getId());
			
			writeInt(_maxCp);
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
			writeByte(_activeChar.isNoble() ? 1 : 0); // Symbol on char menu ctrl+I
			writeByte((_activeChar.isHero() || (_activeChar.isGM() && Config.GM_HERO_AURA)) ? 1 : 0); // Hero Aura
			
			writeByte(_activeChar.isFishing() ? 1 : 0); // 0x01: Fishing Mode (Cant be undone by setting back to 0)
			writeInt(_activeChar.GetFishx());
			writeInt(_activeChar.GetFishy());
			writeInt(_activeChar.GetFishz());
			
			writeInt(_activeChar.getAppearance().getNameColor());
			
			writeInt(0x00); // isRunning() as in UserInfo?
			
			writeInt(_activeChar.getPledgeClass());
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
	}
	
	@Override
	public String getType()
	{
		return _S__03_CHARINFO;
	}
}
