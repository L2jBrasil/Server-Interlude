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
import com.l2jbr.gameserver.model.entity.database.Fish;
import com.l2jbr.gameserver.model.entity.database.repository.FishRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

/**
 * @author -Nemesiss-
 */
public class FishTable {
    private static Logger _log = LoggerFactory.getLogger(SkillTreeTable.class.getName());
    private static FishTable _instance;

    private Map<Integer, List<Fish>> fishData;

    public static FishTable getInstance() {
        if(isNull(_instance)) {
            _instance = new FishTable();
        }
        return _instance;
    }

    private FishTable() {
        fishData = new HashMap<>();
        DatabaseAccess.getRepository(FishRepository.class).findAll().forEach(fish -> {
            if(!fishData.containsKey(fish.getGroup())) {
                fishData.put(fish.getGroup(), new ArrayList<>());
            }
            fishData.get(fish.getGroup()).add(fish);

        });
        int count = 0;
        for (List<Fish> value : fishData.values()) {
            count += value.size();
        }
        _log.info("FishTable: Loaded {} Fishes.", count);
    }


    public List<Fish> getfish(int lvl, int type, int group) {
        var fishes = fishData.get(group);
        if (isNull(fishes)) {
            _log.warn("Fish are not defined !");
            return null;
        }

        var result = fishes.parallelStream().filter(fish -> fish.getLevel() == lvl && fish.getType() == type).collect(Collectors.toList());
        if (result.isEmpty()) {
            _log.warn("Cant Find Any Fish!? - Lvl: " + lvl + " Type: " + type);
        }
        return result;
    }

}
