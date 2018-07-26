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
import com.l2jbr.gameserver.model.entity.database.ArmorSet;
import com.l2jbr.gameserver.model.entity.database.repository.ArmorSetRepository;

import java.util.HashMap;
import java.util.Map;

public class ArmorSetsTable {
    private static ArmorSetsTable _instance;
    private final Map<Integer, ArmorSet> _armorSets;

    public static ArmorSetsTable getInstance() {
        if (_instance == null) {
            _instance = new ArmorSetsTable();
        }
        return _instance;
    }

    private ArmorSetsTable() {
        _armorSets = new HashMap<>();
        loadData();
    }

    private void loadData() {
        ArmorSetRepository repository = DatabaseAccess.getRepository(ArmorSetRepository.class);
        repository.findAll().forEach(armorSet -> {
            _armorSets.put(armorSet.getChest(), armorSet);
        });
    }

    public ArmorSet getSet(int chestId) {
        return _armorSets.get(chestId);
    }
}
