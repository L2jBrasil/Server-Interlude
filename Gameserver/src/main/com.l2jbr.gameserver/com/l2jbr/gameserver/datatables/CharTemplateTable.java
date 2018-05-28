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
package com.l2jbr.gameserver.datatables;

import com.l2jbr.commons.database.DatabaseAccess;
import com.l2jbr.gameserver.model.base.ClassId;
import com.l2jbr.gameserver.model.database.PlayerTemplate;
import com.l2jbr.gameserver.model.database.repository.CharTemplateRepository;
import com.l2jbr.gameserver.templates.L2PcTemplate;
import com.l2jbr.gameserver.templates.StatsSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * This class ...
 *
 * @version $Revision: 1.6.2.1.2.10 $ $Date: 2005/03/29 14:00:54 $
 */
public class CharTemplateTable {
    private static Logger _log = LoggerFactory.getLogger(CharTemplateTable.class.getName());

    private static CharTemplateTable _instance;

    private static final String[] CHAR_CLASSES = {
        "Human Fighter",
        "Warrior",
        "Gladiator",
        "Warlord",
        "Human Knight",
        "Paladin",
        "Dark Avenger",
        "Rogue",
        "Treasure Hunter",
        "Hawkeye",
        "Human Mystic",
        "Human Wizard",
        "Sorceror",
        "Necromancer",
        "Warlock",
        "Cleric",
        "Bishop",
        "Prophet",
        "Elven Fighter",
        "Elven Knight",
        "Temple Knight",
        "Swordsinger",
        "Elven Scout",
        "Plainswalker",
        "Silver Ranger",
        "Elven Mystic",
        "Elven Wizard",
        "Spellsinger",
        "Elemental Summoner",
        "Elven Oracle",
        "Elven Elder",
        "Dark Fighter",
        "Palus Knight",
        "Shillien Knight",
        "Bladedancer",
        "Assassin",
        "Abyss Walker",
        "Phantom Ranger",
        "Dark Elven Mystic",
        "Dark Elven Wizard",
        "Spellhowler",
        "Phantom Summoner",
        "Shillien Oracle",
        "Shillien Elder",
        "Orc Fighter",
        "Orc Raider",
        "Destroyer",
        "Orc Monk",
        "Tyrant",
        "Orc Mystic",
        "Orc Shaman",
        "Overlord",
        "Warcryer",
        "Dwarven Fighter",
        "Dwarven Scavenger",
        "Bounty Hunter",
        "Dwarven Artisan",
        "Warsmith",
        "dummyEntry1",
        "dummyEntry2",
        "dummyEntry3",
        "dummyEntry4",
        "dummyEntry5",
        "dummyEntry6",
        "dummyEntry7",
        "dummyEntry8",
        "dummyEntry9",
        "dummyEntry10",
        "dummyEntry11",
        "dummyEntry12",
        "dummyEntry13",
        "dummyEntry14",
        "dummyEntry15",
        "dummyEntry16",
        "dummyEntry17",
        "dummyEntry18",
        "dummyEntry19",
        "dummyEntry20",
        "dummyEntry21",
        "dummyEntry22",
        "dummyEntry23",
        "dummyEntry24",
        "dummyEntry25",
        "dummyEntry26",
        "dummyEntry27",
        "dummyEntry28",
        "dummyEntry29",
        "dummyEntry30",
        "Duelist",
        "DreadNought",
        "Phoenix Knight",
        "Hell Knight",
        "Sagittarius",
        "Adventurer",
        "Archmage",
        "Soultaker",
        "Arcana Lord",
        "Cardinal",
        "Hierophant",
        "Eva Templar",
        "Sword Muse",
        "Wind Rider",
        "Moonlight Sentinel",
        "Mystic Muse",
        "Elemental Master",
        "Eva's Saint",
        "Shillien Templar",
        "Spectral Dancer",
        "Ghost Hunter",
        "Ghost Sentinel",
        "Storm Screamer",
        "Spectral Master",
        "Shillien Saint",
        "Titan",
        "Grand Khauatari",
        "Dominator",
        "Doomcryer",
        "Fortune Seeker",
        "Maestro"
    };

    private final Map<Integer, PlayerTemplate> _templates;

    public static CharTemplateTable getInstance() {
        if (_instance == null) {
            _instance = new CharTemplateTable();
        }
        return _instance;
    }

    private CharTemplateTable() {
        _templates = new LinkedHashMap<>();
        java.sql.Connection con = null;

        CharTemplateRepository repository = DatabaseAccess.getRepository(CharTemplateRepository.class);
        repository.findAll().forEach(charTemplate -> {
            StatsSet set = new StatsSet();
            set.set("classId", charTemplate.getId());
            set.set("className", charTemplate.getClassName());
            set.set("raceId", charTemplate.getRaceId());
            set.set("baseSTR", charTemplate.getStrength());
            set.set("baseCON", charTemplate.getConstitution());
            set.set("baseDEX", charTemplate.getDexterity());
            set.set("baseINT", charTemplate.getIntellienge());
            set.set("baseWIT", charTemplate.getWitness());
            set.set("baseMEN", charTemplate.getMentality());
            set.set("baseHpMax", charTemplate.getHp());
            set.set("lvlHpAdd", charTemplate.getHpAdd());
            set.set("lvlHpMod", charTemplate.getHpMod());
            set.set("baseMpMax", charTemplate.getMp());
            set.set("baseCpMax", charTemplate.getCp());
            set.set("lvlCpAdd", charTemplate.getCpAdd());
            set.set("lvlCpMod", charTemplate.getCpMod());
            set.set("lvlMpAdd", charTemplate.getMpAdd());
            set.set("lvlMpMod", charTemplate.getMpMod());
            set.set("baseHpReg", 1.5);
            set.set("baseMpReg", 0.9);
            set.set("basePAtk", charTemplate.getpAtk());
            set.set("basePDef", charTemplate.getpDef());
            set.set("baseMAtk", charTemplate.getMAtk());
            set.set("baseMDef", charTemplate.getMDef());
            set.set("classBaseLevel", charTemplate.getClassLevel());
            set.set("basePAtkSpd", charTemplate.getPAtkSpd());
            set.set("baseMAtkSpd", charTemplate.getMAtkSpd());
            set.set("baseCritRate", charTemplate.getCritRate() / 10);
            set.set("baseRunSpd", charTemplate.getRunSpd());
            set.set("baseWalkSpd", 0);
            set.set("baseShldDef", 0);
            set.set("baseShldRate", 0);
            set.set("baseAtkRange", 40);

            set.set("spawnX", charTemplate.getX());
            set.set("spawnY", charTemplate.getY());
            set.set("spawnZ", charTemplate.getZ());


            set.set("collision_radius", charTemplate.getCollisionRadius());
            set.set("collision_height", charTemplate.getCollisionRadius());

            L2PcTemplate ct = new L2PcTemplate(set);

            ct.addItem(charTemplate.getItem1());
            ct.addItem(charTemplate.getItem2());
            ct.addItem(charTemplate.getItem3());
            ct.addItem(charTemplate.getItem4());
            ct.addItem(charTemplate.getItem5());
            _templates.put(charTemplate.getId(), charTemplate);
        });

        _log.info("CharTemplateTable: Loaded " + _templates.size() + " Character Templates.");
    }

    public PlayerTemplate getTemplate(ClassId classId) {
        return getTemplate(classId.getId());
    }

    public PlayerTemplate getTemplate(int classId) {
        int key = classId;
        return _templates.get(key);
    }

    public static final String getClassNameById(int classId) {
        return CHAR_CLASSES[classId];
    }

    public static final int getClassIdByName(String className) {
        int currId = 1;

        for (String name : CHAR_CLASSES) {
            if (name.equalsIgnoreCase(className)) {
                break;
            }

            currId++;
        }

        return currId;
    }

    // public L2CharTemplate[] getAllTemplates()
    // {
    // return _templates.values().toArray(new L2CharTemplate[_templates.size()]);
    // }
}
