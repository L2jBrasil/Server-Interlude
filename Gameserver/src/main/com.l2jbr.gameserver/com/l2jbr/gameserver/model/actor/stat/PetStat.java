/* This program is free software; you can redistribute it and/or modify
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
package com.l2jbr.gameserver.model.actor.stat;

import com.l2jbr.gameserver.model.L2Character;
import com.l2jbr.gameserver.model.L2PetDataTable;
import com.l2jbr.gameserver.model.L2Skill;
import com.l2jbr.gameserver.model.actor.instance.L2PetInstance;
import com.l2jbr.gameserver.model.base.Experience;
import com.l2jbr.gameserver.network.SystemMessageId;
import com.l2jbr.gameserver.serverpackets.PetInfo;
import com.l2jbr.gameserver.serverpackets.StatusUpdate;
import com.l2jbr.gameserver.serverpackets.SystemMessage;
import com.l2jbr.gameserver.skills.Stats;


public class PetStat extends SummonStat
{
	// =========================================================
	// Data Field
	
	// =========================================================
	// Constructor
	public PetStat(L2PetInstance activeChar)
	{
		super(activeChar);
	}
	
	// =========================================================
	// Method - Public
	public boolean addExp(int value)
	{
		if (!super.addExp(value))
		{
			return false;
		}
		
		/*
		 * Micht : Use of PetInfo for C5 StatusUpdate su = new StatusUpdate(getActiveChar().getObjectId()); su.addAttribute(StatusUpdate.EXP, getExp()); getActiveChar().broadcastPacket(su);
		 */
		getActiveChar().broadcastPacket(new PetInfo(getActiveChar()));
		// The PetInfo packet wipes the PartySpelled (list of active spells' icons). Re-add them
		getActiveChar().updateEffectIcons(true);
		
		return true;
	}
	
	@Override
	public boolean addExpAndSp(long addToExp, int addToSp)
	{
		if (!super.addExpAndSp(addToExp, addToSp))
		{
			return false;
		}
		
		SystemMessage sm = new SystemMessage(SystemMessageId.PET_EARNED_S1_EXP);
		sm.addNumber((int) addToExp);
		
		getActiveChar().getOwner().sendPacket(sm);
		
		return true;
	}
	
	@Override
	public final boolean addLevel(byte value)
	{
		if ((getLevel() + value) > (Experience.MAX_LEVEL - 1))
		{
			return false;
		}
		
		boolean levelIncreased = super.addLevel(value);
		
		// Sync up exp with current level
		if ((getExp() > getExpForLevel(getLevel() + 1)) || (getExp() < getExpForLevel(getLevel())))
		{
			setExp(Experience.LEVEL[getLevel()]);
		}
		
		if (levelIncreased)
		{
			getActiveChar().getOwner().sendMessage("Your pet has increased it's level.");
		}
		
		StatusUpdate su = new StatusUpdate(getActiveChar().getObjectId());
		su.addAttribute(StatusUpdate.LEVEL, getLevel());
		su.addAttribute(StatusUpdate.MAX_HP, getMaxHp());
		su.addAttribute(StatusUpdate.MAX_MP, getMaxMp());
		getActiveChar().broadcastPacket(su);
		
		// Send a Server->Client packet PetInfo to the L2PcInstance
		getActiveChar().getOwner().sendPacket(new PetInfo(getActiveChar()));
		// The PetInfo packet wipes the PartySpelled (list of active spells' icons). Re-add them
		getActiveChar().updateEffectIcons(true);
		
		if (getActiveChar().getControlItem() != null)
		{
			getActiveChar().getControlItem().setEnchantLevel(getLevel());
		}
		
		return levelIncreased;
	}
	
	@Override
	public final long getExpForLevel(int level)
	{
		return L2PetDataTable.getInstance().getPetData(getActiveChar().getNpcId(), level).getExpMax();
	}
	
	// =========================================================
	// Method - Private
	
	// =========================================================
	// Property - Public
	@Override
	public L2PetInstance getActiveChar()
	{
		return (L2PetInstance) super.getActiveChar();
	}
	
	public final int getFeedBattle()
	{
		return getActiveChar().getPetData().getFeedbattle();
	}
	
	public final int getFeedNormal()
	{
		return getActiveChar().getPetData().getFeednormal();
	}
	
	@Override
	public void setLevel(byte value)
	{
		getActiveChar().stopFeed();
		super.setLevel(value);
		
		getActiveChar().setPetData(L2PetDataTable.getInstance().getPetData(getActiveChar().getTemplate().getId(), getLevel()));
		getActiveChar().startFeed(false);
		
		if (getActiveChar().getControlItem() != null)
		{
			getActiveChar().getControlItem().setEnchantLevel(getLevel());
		}
	}
	
	public final int getMaxFeed()
	{
		return getActiveChar().getPetData().getFeedMax();
	}
	
	@Override
	public int getMaxHp()
	{
		return (int) calcStat(Stats.MAX_HP, getActiveChar().getPetData().getHpMax(), null, null);
	}
	
	@Override
	public int getMaxMp()
	{
		return (int) calcStat(Stats.MAX_MP, getActiveChar().getPetData().getMpMax(), null, null);
	}
	
	@Override
	public int getMAtk(L2Character target, L2Skill skill)
	{
		double attack = getActiveChar().getPetData().getMatk();
		Stats stat = skill == null ? null : skill.getStat();
		if (stat != null)
		{
			switch (stat)
			{
				case AGGRESSION:
					attack += getActiveChar().getTemplate().getAggression();
					break;
				case BLEED:
					attack += getActiveChar().getTemplate().getBleed();
					break;
				case POISON:
					attack += getActiveChar().getTemplate().getPoison();
					break;
				case STUN:
					attack += getActiveChar().getTemplate().getStun();
					break;
				case ROOT:
					attack += getActiveChar().getTemplate().getRoot();
					break;
				case MOVEMENT:
					attack += getActiveChar().getTemplate().getMovement();
					break;
				case CONFUSION:
					attack += getActiveChar().getTemplate().getConfusion();
					break;
				case SLEEP:
					attack += getActiveChar().getTemplate().getSleep();
					break;
				case FIRE:
					attack += getActiveChar().getTemplate().getFire();
					break;
				case WIND:
					attack += getActiveChar().getTemplate().getWind();
					break;
				case WATER:
					attack += getActiveChar().getTemplate().getWater();
					break;
				case EARTH:
					attack += getActiveChar().getTemplate().getEarth();
					break;
				case HOLY:
					attack += getActiveChar().getTemplate().getHoly();
					break;
				case DARK:
					attack += getActiveChar().getTemplate().getDark();
					break;
			}
		}
		if (skill != null)
		{
			attack += skill.getPower();
		}
		return (int) calcStat(Stats.MAGIC_ATTACK, attack, target, skill);
	}
	
	@Override
	public int getMDef(L2Character target, L2Skill skill)
	{
		double defence = getActiveChar().getPetData().getMdef();
		return (int) calcStat(Stats.MAGIC_DEFENCE, defence, target, skill);
	}
	
	@Override
	public int getPAtk(L2Character target)
	{
		return (int) calcStat(Stats.PHYSIC_ATTACK, getActiveChar().getPetData().getPatk(), target, null);
	}
	
	@Override
	public int getPDef(L2Character target)
	{
		return (int) calcStat(Stats.PHYSIC_DEFENCE, getActiveChar().getPetData().getPdef(), target, null);
	}
	
	@Override
	public int getAccuracy()
	{
		return (int) calcStat(Stats.ACCURACY, getActiveChar().getPetData().getAcc(), null, null);
	}
	
	@Override
	public int getCriticalHit(L2Character target, L2Skill skill)
	{
		return (int) calcStat(Stats.CRITICAL_RATE, getActiveChar().getPetData().getCrit(), target, null);
	}
	
	@Override
	public int getEvasionRate(L2Character target)
	{
		return (int) calcStat(Stats.EVASION_RATE, getActiveChar().getPetData().getEvasion(), target, null);
	}
	
	@Override
	public int getRunSpeed()
	{
		return (int) calcStat(Stats.RUN_SPEED, getActiveChar().getPetData().getSpeed(), null, null);
	}
	
	@Override
	public int getPAtkSpd()
	{
		return (int) calcStat(Stats.PHYSIC_ATTACK_SPEED, getActiveChar().getPetData().getAtkSpeed(), null, null);
	}
	
	@Override
	public int getMAtkSpd()
	{
		return (int) calcStat(Stats.MAGIC_ATTACK_SPEED, getActiveChar().getPetData().getCastSpeed(), null, null);
	}
}
