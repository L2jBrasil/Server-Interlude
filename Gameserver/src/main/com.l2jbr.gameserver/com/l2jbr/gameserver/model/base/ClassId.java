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
package com.l2jbr.gameserver.model.base;


/**
 * This class defines all classes (ex : HUMAN fighter, darkFighter...) that a player can chose.<BR>
 * <BR>
 * Data :<BR>
 * <BR>
 * <li>id : The Identifier of the class</li> <li>isMage : True if the class is a mage class</li> <li>race : The race of this class</li> <li>parent : The parent ClassId or null if this class is the root</li><BR>
 * <BR>
 * @version $Revision: 1.4.4.4 $ $Date: 2005/03/27 15:29:33 $
 */
public enum ClassId
{
	fighter(0x00, false, Race.HUMAN, null),
	
	warrior(0x01, false, Race.HUMAN, fighter),
	gladiator(0x02, false, Race.HUMAN, warrior),
	warlord(0x03, false, Race.HUMAN, warrior),
	knight(0x04, false, Race.HUMAN, fighter),
	paladin(0x05, false, Race.HUMAN, knight),
	darkAvenger(0x06, false, Race.HUMAN, knight),
	rogue(0x07, false, Race.HUMAN, fighter),
	treasureHunter(0x08, false, Race.HUMAN, rogue),
	hawkeye(0x09, false, Race.HUMAN, rogue),
	
	mage(0x0a, true, Race.HUMAN, null),
	wizard(0x0b, true, Race.HUMAN, mage),
	sorceror(0x0c, true, Race.HUMAN, wizard),
	necromancer(0x0d, true, Race.HUMAN, wizard),
	warlock(0x0e, true, Race.HUMAN, wizard),
	cleric(0x0f, true, Race.HUMAN, mage),
	bishop(0x10, true, Race.HUMAN, cleric),
	prophet(0x11, true, Race.HUMAN, cleric),
	
	elvenFighter(0x12, false, Race.ELF, null),
	elvenKnight(0x13, false, Race.ELF, elvenFighter),
	templeKnight(0x14, false, Race.ELF, elvenKnight),
	swordSinger(0x15, false, Race.ELF, elvenKnight),
	elvenScout(0x16, false, Race.ELF, elvenFighter),
	plainsWalker(0x17, false, Race.ELF, elvenScout),
	silverRanger(0x18, false, Race.ELF, elvenScout),
	
	elvenMage(0x19, true, Race.ELF, null),
	elvenWizard(0x1a, true, Race.ELF, elvenMage),
	spellsinger(0x1b, true, Race.ELF, elvenWizard),
	elementalSummoner(0x1c, true, Race.ELF, elvenWizard),
	oracle(0x1d, true, Race.ELF, elvenMage),
	elder(0x1e, true, Race.ELF, oracle),
	
	darkFighter(0x1f, false, Race.DARK_ELF, null),
	palusKnight(0x20, false, Race.DARK_ELF, darkFighter),
	shillienKnight(0x21, false, Race.DARK_ELF, palusKnight),
	bladedancer(0x22, false, Race.DARK_ELF, palusKnight),
	assassin(0x23, false, Race.DARK_ELF, darkFighter),
	abyssWalker(0x24, false, Race.DARK_ELF, assassin),
	phantomRanger(0x25, false, Race.DARK_ELF, assassin),
	
	darkMage(0x26, true, Race.DARK_ELF, null),
	darkWizard(0x27, true, Race.DARK_ELF, darkMage),
	spellhowler(0x28, true, Race.DARK_ELF, darkWizard),
	phantomSummoner(0x29, true, Race.DARK_ELF, darkWizard),
	shillienOracle(0x2a, true, Race.DARK_ELF, darkMage),
	shillenElder(0x2b, true, Race.DARK_ELF, shillienOracle),
	
	orcFighter(0x2c, false, Race.ORC, null),
	orcRaider(0x2d, false, Race.ORC, orcFighter),
	destroyer(0x2e, false, Race.ORC, orcRaider),
	orcMonk(0x2f, false, Race.ORC, orcFighter),
	tyrant(0x30, false, Race.ORC, orcMonk),
	
	orcMage(0x31, false, Race.ORC, null),
	orcShaman(0x32, false, Race.ORC, orcMage),
	overlord(0x33, false, Race.ORC, orcShaman),
	warcryer(0x34, false, Race.ORC, orcShaman),
	
	dwarvenFighter(0x35, false, Race.DWARF, null),
	scavenger(0x36, false, Race.DWARF, dwarvenFighter),
	bountyHunter(0x37, false, Race.DWARF, scavenger),
	artisan(0x38, false, Race.DWARF, dwarvenFighter),
	warsmith(0x39, false, Race.DWARF, artisan),
	
	/*
	 * Dummy Entries (id's already in decimal format) btw FU NCSoft for the amount of work you put me through to do this!! <START>
	 */
	dummyEntry1(58, false, null, null),
	dummyEntry2(59, false, null, null),
	dummyEntry3(60, false, null, null),
	dummyEntry4(61, false, null, null),
	dummyEntry5(62, false, null, null),
	dummyEntry6(63, false, null, null),
	dummyEntry7(64, false, null, null),
	dummyEntry8(65, false, null, null),
	dummyEntry9(66, false, null, null),
	dummyEntry10(67, false, null, null),
	dummyEntry11(68, false, null, null),
	dummyEntry12(69, false, null, null),
	dummyEntry13(70, false, null, null),
	dummyEntry14(71, false, null, null),
	dummyEntry15(72, false, null, null),
	dummyEntry16(73, false, null, null),
	dummyEntry17(74, false, null, null),
	dummyEntry18(75, false, null, null),
	dummyEntry19(76, false, null, null),
	dummyEntry20(77, false, null, null),
	dummyEntry21(78, false, null, null),
	dummyEntry22(79, false, null, null),
	dummyEntry23(80, false, null, null),
	dummyEntry24(81, false, null, null),
	dummyEntry25(82, false, null, null),
	dummyEntry26(83, false, null, null),
	dummyEntry27(84, false, null, null),
	dummyEntry28(85, false, null, null),
	dummyEntry29(86, false, null, null),
	dummyEntry30(87, false, null, null),
	/*
	 * <END> Of Dummy entries
	 */
	
	/*
	 * Now the bad boys! new class ids :)) (3rd classes)
	 */
	duelist(0x58, false, Race.HUMAN, gladiator),
	dreadnought(0x59, false, Race.HUMAN, warlord),
	phoenixKnight(0x5a, false, Race.HUMAN, paladin),
	hellKnight(0x5b, false, Race.HUMAN, darkAvenger),
	sagittarius(0x5c, false, Race.HUMAN, hawkeye),
	adventurer(0x5d, false, Race.HUMAN, treasureHunter),
	archmage(0x5e, true, Race.HUMAN, sorceror),
	soultaker(0x5f, true, Race.HUMAN, necromancer),
	arcanaLord(0x60, true, Race.HUMAN, warlock),
	cardinal(0x61, true, Race.HUMAN, bishop),
	hierophant(0x62, true, Race.HUMAN, prophet),
	
	evaTemplar(0x63, false, Race.ELF, templeKnight),
	swordMuse(0x64, false, Race.ELF, swordSinger),
	windRider(0x65, false, Race.ELF, plainsWalker),
	moonlightSentinel(0x66, false, Race.ELF, silverRanger),
	mysticMuse(0x67, true, Race.ELF, spellsinger),
	elementalMaster(0x68, true, Race.ELF, elementalSummoner),
	evaSaint(0x69, true, Race.ELF, elder),
	
	shillienTemplar(0x6a, false, Race.DARK_ELF, shillienKnight),
	spectralDancer(0x6b, false, Race.DARK_ELF, bladedancer),
	ghostHunter(0x6c, false, Race.DARK_ELF, abyssWalker),
	ghostSentinel(0x6d, false, Race.DARK_ELF, phantomRanger),
	stormScreamer(0x6e, true, Race.DARK_ELF, spellhowler),
	spectralMaster(0x6f, true, Race.DARK_ELF, phantomSummoner),
	shillienSaint(0x70, true, Race.DARK_ELF, shillenElder),
	
	titan(0x71, false, Race.ORC, destroyer),
	grandKhauatari(0x72, false, Race.ORC, tyrant),
	dominator(0x73, false, Race.ORC, overlord),
	doomcryer(0x74, false, Race.ORC, warcryer),
	
	fortuneSeeker(0x75, false, Race.DWARF, bountyHunter),
	maestro(0x76, false, Race.DWARF, warsmith);
	
	/** The Identifier of the Class */
	private final int _id;
	
	/** True if the class is a mage class */
	private final boolean _isMage;
	
	/** The Race object of the class */
	private final Race _race;
	
	/** The parent ClassId or null if this class is a root */
	private final ClassId _parent;
	
	/**
	 * Constructor of ClassId.
	 * @param pId
	 * @param pIsMage
	 * @param pRace
	 * @param pParent
	 */
	private ClassId(int pId, boolean pIsMage, Race pRace, ClassId pParent)
	{
		_id = pId;
		_isMage = pIsMage;
		_race = pRace;
		_parent = pParent;
	}
	
	/**
	 * Return the Identifier of the Class.<BR>
	 * <BR>
	 * @return
	 */
	public final int getId()
	{
		return _id;
	}
	
	/**
	 * @return {@code true} if the class is a mage class
	 */
	public final boolean isMage()
	{
		return _isMage;
	}
	
	/**
	 * @return the Race object of the class
	 */
	public final Race getRace()
	{
		return _race;
	}
	
	/**
	 * @param cid The parent ClassId to check
	 * @return {@code true} if this Class is a child of the selected ClassId
	 */
	public final boolean childOf(ClassId cid)
	{
		if (_parent == null)
		{
			return false;
		}
		
		if (_parent == cid)
		{
			return true;
		}
		
		return _parent.childOf(cid);
		
	}
	
	/**
	 * @param cid The parent ClassId to check
	 * @return {@code true} if this Class is equal to the selected ClassId or a child of the selected ClassId
	 */
	public final boolean equalsOrChildOf(ClassId cid)
	{
		return (this == cid) || childOf(cid);
	}
	
	/**
	 * @return {@code true} if the child level of this Class (0=root, 1=child leve 1...)
	 */
	public final int level()
	{
		if (_parent == null)
		{
			return 0;
		}
		
		return 1 + _parent.level();
	}
	
	/**
	 * @return its parent ClassId
	 */
	public final ClassId getParent()
	{
		return _parent;
	}
}
