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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class CharTemplateTable {
    private static Logger _log = LoggerFactory.getLogger(CharTemplateTable.class);

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

    private final Map<Integer, PlayerTemplate> templates;

    public static CharTemplateTable getInstance() {
        if (_instance == null) {
            _instance = new CharTemplateTable();
        }
        return _instance;
    }

    private CharTemplateTable() {
        templates = new LinkedHashMap<>();
        CharTemplateRepository repository = DatabaseAccess.getRepository(CharTemplateRepository.class);
        repository.findAll().forEach(this::addToTemplates);

        _log.info("CharTemplateTable: Loaded {} Character Templates.", templates.size());
    }

    private void addToTemplates(PlayerTemplate playerTemplate) {
        templates.put(playerTemplate.getId(), playerTemplate);
    }

    public PlayerTemplate getTemplate(ClassId classId) {
        return getTemplate(classId.getId());
    }

    public PlayerTemplate getTemplate(int classId) {
        return templates.get(classId);
    }

    public static String getClassNameById(int classId) {
        return CHAR_CLASSES[classId];
    }

    public Collection<PlayerTemplate> all() {
        return templates.values();
    }
}
