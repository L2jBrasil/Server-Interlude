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

import com.l2jbr.commons.util.Util;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;

import static com.l2jbr.gameserver.model.base.ClassType.Fighter;
import static com.l2jbr.gameserver.model.base.ClassType.Mystic;
import static com.l2jbr.gameserver.model.base.ClassType.Priest;

public enum PlayerClass {
	FIGHTER(0x00, Fighter, Race.HUMAN, null),
	
	WARRIOR(0x01, Fighter, Race.HUMAN, FIGHTER),
	GLADIATOR(0x02, Fighter, Race.HUMAN, WARRIOR),
	WARLORD(0x03, Fighter, Race.HUMAN, WARRIOR),
	KNIGHT(0x04, Fighter, Race.HUMAN, FIGHTER),
	PALADIN(0x05, Fighter, Race.HUMAN, KNIGHT),
	DARK_AVENGER(0x06, Fighter, Race.HUMAN, KNIGHT),
	ROGUE(0x07, Fighter, Race.HUMAN, FIGHTER),
	TREASURE_HUNTER(0x08, Fighter, Race.HUMAN, ROGUE),
	HAWKEYE(0x09, Fighter, Race.HUMAN, ROGUE),
	
	MAGE(0x0a, Mystic, Race.HUMAN, null),
	WIZARD(0x0b, Mystic, Race.HUMAN, MAGE),
	SORCEROR(0x0c, Mystic, Race.HUMAN, WIZARD),
	NECROMANCER(0x0d, Mystic, Race.HUMAN, WIZARD),
	WARLOCK(0x0e, Mystic, Race.HUMAN, WIZARD),
	CLERIC(0x0f, Priest, Race.HUMAN, MAGE),
	BISHOP(0x10, Priest, Race.HUMAN, CLERIC),
	PROPHET(0x11, Priest, Race.HUMAN, CLERIC),
	
	ELVEN_FIGHTER(0x12, Fighter, Race.ELF, null),
	ELVEN_KNIGHT(0x13, Fighter, Race.ELF, ELVEN_FIGHTER),
	TEMPLE_KNIGHT(0x14, Fighter, Race.ELF, ELVEN_KNIGHT),
	SWORD_SINGER(0x15, Fighter, Race.ELF, ELVEN_KNIGHT),
	ELVEN_SCOUT(0x16, Fighter, Race.ELF, ELVEN_FIGHTER),
	PLAINS_WALKER(0x17, Fighter, Race.ELF, ELVEN_SCOUT),
	SILVER_RANGER(0x18, Fighter, Race.ELF, ELVEN_SCOUT),
	
	ELVEN_MAGE(0x19, Mystic, Race.ELF, null),
	ELVEN_WIZARD(0x1a, Mystic, Race.ELF, ELVEN_MAGE),
	SPELLSINGER(0x1b, Mystic, Race.ELF, ELVEN_WIZARD),
	ELEMENTAL_SUMMONER(0x1c, Mystic, Race.ELF, ELVEN_WIZARD),
	ORACLE(0x1d, Priest, Race.ELF, ELVEN_MAGE),
	ELDER(0x1e, Priest, Race.ELF, ORACLE),
	
	DARK_FIGHTER(0x1f, Fighter, Race.DARK_ELF, null),
	PALUS_KNIGHT(0x20, Fighter, Race.DARK_ELF, DARK_FIGHTER),
	SHILLIEN_KNIGHT(0x21, Fighter, Race.DARK_ELF, PALUS_KNIGHT),
	BLADEDANCER(0x22, Fighter, Race.DARK_ELF, PALUS_KNIGHT),
	ASSASSIN(0x23, Fighter, Race.DARK_ELF, DARK_FIGHTER),
	ABYSS_WALKER(0x24, Fighter, Race.DARK_ELF, ASSASSIN),
	PHANTOM_RANGER(0x25, Fighter, Race.DARK_ELF, ASSASSIN),
	
	DARK_MAGE(0x26, Mystic, Race.DARK_ELF, null),
	DARK_WIZARD(0x27, Mystic, Race.DARK_ELF, DARK_MAGE),
	SPELLHOWLER(0x28, Mystic, Race.DARK_ELF, DARK_WIZARD),
	PHANTOM_SUMMONER(0x29, Mystic, Race.DARK_ELF, DARK_WIZARD),
	SHILLIEN_ORACLE(0x2a, Priest, Race.DARK_ELF, DARK_MAGE),
	SHILLEN_ELDER(0x2b, Priest, Race.DARK_ELF, SHILLIEN_ORACLE),
	
	ORC_FIGHTER(0x2c, Fighter, Race.ORC, null),
	ORC_RAIDER(0x2d, Fighter, Race.ORC, ORC_FIGHTER),
	DESTROYER(0x2e, Fighter, Race.ORC, ORC_RAIDER),
	ORC_MONK(0x2f, Fighter, Race.ORC, ORC_FIGHTER),
	TYRANT(0x30, Fighter, Race.ORC, ORC_MONK),
	
	ORC_MAGE(0x31, Mystic, Race.ORC, null),
	ORC_SHAMAN(0x32, Mystic, Race.ORC, ORC_MAGE),
	OVERLORD(0x33, Mystic, Race.ORC, ORC_SHAMAN),
	WARCRYER(0x34, Mystic, Race.ORC, ORC_SHAMAN),
	
	DWARVEN_FIGHTER(0x35, Fighter, Race.DWARF, null),
	SCAVENGER(0x36, Fighter, Race.DWARF, DWARVEN_FIGHTER),
	BOUNTY_HUNTER(0x37, Fighter, Race.DWARF, SCAVENGER),
	ARTISAN(0x38, Fighter, Race.DWARF, DWARVEN_FIGHTER),
	WARSMITH(0x39, Fighter, Race.DWARF, ARTISAN),
	
	/*
	 * Dummy Entries (id's already in decimal format) btw FU NCSoft for the amount of work you put me through to do this!! <START>
	 */
	dummyEntry1(58, null, null, null),
	dummyEntry2(59, null, null, null),
	dummyEntry3(60, null, null, null),
	dummyEntry4(61, null, null, null),
	dummyEntry5(62, null, null, null),
	dummyEntry6(63, null, null, null),
	dummyEntry7(64, null, null, null),
	dummyEntry8(65, null, null, null),
	dummyEntry9(66, null, null, null),
	dummyEntry10(67, null, null, null),
	dummyEntry11(68, null, null, null),
	dummyEntry12(69, null, null, null),
	dummyEntry13(70, null, null, null),
	dummyEntry14(71, null, null, null),
	dummyEntry15(72, null, null, null),
	dummyEntry16(73, null, null, null),
	dummyEntry17(74, null, null, null),
	dummyEntry18(75, null, null, null),
	dummyEntry19(76, null, null, null),
	dummyEntry20(77, null, null, null),
	dummyEntry21(78, null, null, null),
	dummyEntry22(79, null, null, null),
	dummyEntry23(80, null, null, null),
	dummyEntry24(81, null, null, null),
	dummyEntry25(82, null, null, null),
	dummyEntry26(83, null, null, null),
	dummyEntry27(84, null, null, null),
	dummyEntry28(85, null, null, null),
	dummyEntry29(86, null, null, null),
	dummyEntry30(87, null, null, null),
	/*
	 * <END> Of Dummy entries
	 */
	
	/*
	 * Now the bad boys! new class ids :)) (3rd classes)
	 */
	DUELIST(0x58, Fighter, Race.HUMAN, GLADIATOR),
	DREADNOUGHT(0x59, Fighter, Race.HUMAN, WARLORD),
	PHOENIX_KNIGHT(0x5a, Fighter, Race.HUMAN, PALADIN),
	HELL_KNIGHT(0x5b, Fighter, Race.HUMAN, DARK_AVENGER),
	SAGITTARIUS(0x5c, Fighter, Race.HUMAN, HAWKEYE),
	ADVENTURER(0x5d, Fighter, Race.HUMAN, TREASURE_HUNTER),
	ARCHMAGE(0x5e, Mystic, Race.HUMAN, SORCEROR),
	SOULTAKER(0x5f, Mystic, Race.HUMAN, NECROMANCER),
	ARCANA_LORD(0x60, Mystic, Race.HUMAN, WARLOCK),
	CARDINAL(0x61, Priest, Race.HUMAN, BISHOP),
	HIEROPHANT(0x62, Priest, Race.HUMAN, PROPHET),
	
	EVA_TEMPLAR(0x63, Fighter, Race.ELF, TEMPLE_KNIGHT),
	SWORD_MUSE(0x64, Fighter, Race.ELF, SWORD_SINGER),
	WIND_RIDER(0x65, Fighter, Race.ELF, PLAINS_WALKER),
	MOONLIGHT_SENTINEL(0x66, Fighter, Race.ELF, SILVER_RANGER),
	MYSTIC_MUSE(0x67, Mystic, Race.ELF, SPELLSINGER),
	ELEMENTAL_MASTER(0x68, Mystic, Race.ELF, ELEMENTAL_SUMMONER),
	EVA_SAINT(0x69, Priest, Race.ELF, ELDER),
	
	SHILLIEN_TEMPLAR(0x6a, Fighter, Race.DARK_ELF, SHILLIEN_KNIGHT),
	SPECTRAL_DANCER(0x6b, Fighter, Race.DARK_ELF, BLADEDANCER),
	GHOST_HUNTER(0x6c, Fighter, Race.DARK_ELF, ABYSS_WALKER),
	GHOST_SENTINEL(0x6d, Fighter, Race.DARK_ELF, PHANTOM_RANGER),
	STORM_SCREAMER(0x6e, Mystic, Race.DARK_ELF, SPELLHOWLER),
	SPECTRAL_MASTER(0x6f, Mystic, Race.DARK_ELF, PHANTOM_SUMMONER),
	SHILLIEN_SAINT(0x70, Priest, Race.DARK_ELF, SHILLEN_ELDER),
	
	TITAN(0x71, Fighter, Race.ORC, DESTROYER),
	GRAND_KHAUATARI(0x72, Fighter, Race.ORC, TYRANT),
	DOMINATOR(0x73, Mystic, Race.ORC, OVERLORD),
	DOOMCRYER(0x74, Mystic, Race.ORC, WARCRYER),
	
	FORTUNE_SEEKER(0x75, Fighter, Race.DWARF, BOUNTY_HUNTER),
	MAESTRO(0x76, Fighter, Race.DWARF, WARSMITH);


    private static final Set<PlayerClass> mainSubclassSet;
    private static final Set<PlayerClass> neverSubclassed = EnumSet.of(OVERLORD, WARSMITH);

    private static final Set<PlayerClass> knightClasses = EnumSet.of(DARK_AVENGER, PALADIN, TEMPLE_KNIGHT, SHILLIEN_KNIGHT);
    private static final Set<PlayerClass> walkersClasses = EnumSet.of(TREASURE_HUNTER, ABYSS_WALKER, PLAINS_WALKER);
    private static final Set<PlayerClass> rangerClasses = EnumSet.of(HAWKEYE, SILVER_RANGER, PHANTOM_RANGER);
    private static final Set<PlayerClass> summonerClasses = EnumSet.of(WARLOCK, ELEMENTAL_SUMMONER, PHANTOM_SUMMONER);
    private static final Set<PlayerClass> wizardClasses = EnumSet.of(SORCEROR, SPELLSINGER, SPELLHOWLER);

    private static final EnumMap<PlayerClass, Set<PlayerClass>> subclassSetMap = new EnumMap<>(PlayerClass.class);

    static
    {
        Set<PlayerClass> subclasses = getSet(null, 2);
        subclasses.removeAll(neverSubclassed);

        mainSubclassSet = subclasses;

        subclassSetMap.put(DARK_AVENGER, knightClasses);
        subclassSetMap.put(PALADIN, knightClasses);
        subclassSetMap.put(TEMPLE_KNIGHT, knightClasses);
        subclassSetMap.put(SHILLIEN_KNIGHT, knightClasses);

        subclassSetMap.put(TREASURE_HUNTER, walkersClasses);
        subclassSetMap.put(ABYSS_WALKER, walkersClasses);
        subclassSetMap.put(PLAINS_WALKER, walkersClasses);

        subclassSetMap.put(HAWKEYE, rangerClasses);
        subclassSetMap.put(SILVER_RANGER, rangerClasses);
        subclassSetMap.put(PHANTOM_RANGER, rangerClasses);

        subclassSetMap.put(WARLOCK, summonerClasses);
        subclassSetMap.put(ELEMENTAL_SUMMONER, summonerClasses);
        subclassSetMap.put(PHANTOM_SUMMONER, summonerClasses);

        subclassSetMap.put(SORCEROR, wizardClasses);
        subclassSetMap.put(SPELLSINGER, wizardClasses);
        subclassSetMap.put(SPELLHOWLER, wizardClasses);
    }

    private final int id;
    private final Race race;

	private final PlayerClass parent;
    private final ClassType classType;

    PlayerClass(int pId, ClassType classType, Race pRace, PlayerClass pParent) {
		id = pId;
		this.classType = classType;
		race = pRace;
		parent = pParent;
	}

	public final int getId()
	{
		return id;
	}

	public final boolean isMage()
	{
		return classType == ClassType.Mystic || classType == ClassType.Priest;
	}

	public final Race getRace()
	{
		return race;
	}

	public final boolean childOf(PlayerClass cid) {
		if (parent == null) {
			return false;
		}
		
		if (parent == cid) {
			return true;
		}
		
		return parent.childOf(cid);
		
	}

	public final boolean equalsOrChildOf(PlayerClass cid)
	{
		return (this == cid) || childOf(cid);
	}
	

	public final int level() {
		if (parent == null) {
			return 0;
		}
		
		return 1 + parent.level();
	}

    public final Set<PlayerClass> getAvailableSubclasses() {
        Set<PlayerClass> subclasses = null;

        if (level() == 2) {
            subclasses = EnumSet.copyOf(mainSubclassSet);

            subclasses.remove(this);
            switch (race) {
                case ELF:
                    subclasses.removeAll(getSet(Race.DARK_ELF, 2));
                    break;
                case DARK_ELF:
                    subclasses.removeAll(getSet(Race.ELF, 2));
                    break;
            }

            Set<PlayerClass> unavailableClasses = subclassSetMap.get(this);

            if (unavailableClasses != null)  {
                subclasses.removeAll(unavailableClasses);
            }
        }
        return subclasses;
    }

    public static EnumSet<PlayerClass> getSet(Race race, int level) {
        EnumSet<PlayerClass> allOf = EnumSet.noneOf(PlayerClass.class);

        for (PlayerClass playerClass : EnumSet.allOf(PlayerClass.class))  {
            if ((race == null) || playerClass.isOfRace(race)) {
                if ((level == -1) || playerClass.level() == level)  {
                    allOf.add(playerClass);
                }
            }
        }
        return allOf;
    }

    public boolean isOfRace(Race race) {
	    return this.race == race;
    }

    public final PlayerClass getParent()
	{
		return parent;
	}

	public String humanize() { return Util.capitalize(toString().replace('_', ' ')); }

    public boolean isOfType(ClassType classType) {
        return  this.classType == classType;
    }
}
