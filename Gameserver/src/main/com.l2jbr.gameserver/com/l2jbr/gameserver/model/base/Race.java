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

public enum Race {
	HUMAN,
    ELF,
    DARK_ELF,
    ORC,
    DWARF,
    UNDEAD,
    MAGICC_REATURE,
    BEAST,
    ANIMAL,
    PLANT,
    HUMANOID,
    SPIRIT,
    ANGEL,
    DEMON,
    DRAGON,
    GIANT,
    BUG,
    FAIRY,
    OTHER,
    NON_LIVING,
    SIEGE_WEAPON,
    DEFENDING_ARMY,
    MERCENARY,
    UNKNOWN;

	// TODO implement RACE skill (4416) to remove this
    public static Race fromRaceSkillLevel(int skillLevel) {
        int raceIndex = skillLevel;
        if(skillLevel > 0 && skillLevel < 14) {
            raceIndex = skillLevel + 4;
        } else if(skillLevel >= 14 && skillLevel <= 18) {
            raceIndex= skillLevel - 4;
        } else if(skillLevel > 23) {
            raceIndex = 23;
        }

        return Race.values()[raceIndex];
    }

}