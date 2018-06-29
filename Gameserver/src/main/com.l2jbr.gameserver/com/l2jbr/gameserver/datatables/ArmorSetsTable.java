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

/**
 * @author godson
 */

package com.l2jbr.gameserver.datatables;

import com.l2jbr.commons.database.DatabaseAccess;
import com.l2jbr.gameserver.model.L2ArmorSet;
import com.l2jbr.gameserver.model.entity.database.repository.ArmorSetRepository;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Luno
 */
public class ArmorSetsTable {
    private static ArmorSetsTable _instance;
    private final Map<Integer, L2ArmorSet> _armorSets;

    public static ArmorSetsTable getInstance() {
        if (_instance == null) {
            _instance = new ArmorSetsTable();
        }
        return _instance;
    }

    private ArmorSetsTable() {
        _armorSets = new LinkedHashMap<>();
        loadData();
    }

    private void loadData() {
        ArmorSetRepository repository = DatabaseAccess.getRepository(ArmorSetRepository.class);
        repository.findAll().forEach(armorSet -> {
            int chest = armorSet.getChest();
            int legs = armorSet.getLegs();
            int head = armorSet.getHead();
            int gloves = armorSet.getGloves();
            int feet = armorSet.getFeet();
            int skill_id = armorSet.getSkillId();
            int shield = armorSet.getShield();
            int shield_skill_id = armorSet.getShieldSkillId();
            int enchant6skill = armorSet.getEnchant6Skill();
            _armorSets.put(chest, new L2ArmorSet(chest, legs, head, gloves, feet, skill_id, shield, shield_skill_id, enchant6skill));

        });
    }

    public boolean setExists(int chestId) {
        return _armorSets.containsKey(chestId);
    }

    public L2ArmorSet getSet(int chestId) {
        return _armorSets.get(chestId);
    }
}
