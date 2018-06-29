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
import com.l2jbr.gameserver.model.FishData;
import com.l2jbr.gameserver.model.entity.database.repository.FishRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;


/**
 * @author -Nemesiss-
 */
public class FishTable {
    private static Logger _log = LoggerFactory.getLogger(SkillTreeTable.class.getName());
    private static final FishTable _instance = new FishTable();

    private static List<FishData> _fishsNormal;
    private static List<FishData> _fishsEasy;
    private static List<FishData> _fishsHard;

    public static FishTable getInstance() {
        return _instance;
    }

    private FishTable() {
        _fishsEasy = new LinkedList<>();
        _fishsNormal = new LinkedList<>();
        _fishsHard = new LinkedList<>();

        FishRepository repository = DatabaseAccess.getRepository(FishRepository.class);
        repository.findAll().forEach(fish ->{
            int id = fish.getId();
            int lvl = fish.getLevel();
            String name = fish.getName();
            int hp = fish.getHp();
            int hpreg = fish.getHpregen();
            int type = fish.getFishType();
            int group = fish.getFishGroup();
            int fish_guts = fish.getFishGuts();
            int guts_check_time = fish.getGutsCheckTime();
            int wait_time = fish.getWaitTime();
            int combat_time = fish.getCombatTime();
            FishData fishData = new FishData(id, lvl, name, hp, hpreg, type, group, fish_guts, guts_check_time, wait_time, combat_time);
            switch (fishData.getGroup()) {
                case 0:
                    _fishsEasy.add(fishData);
                    break;
                case 1:
                    _fishsNormal.add(fishData);
                    break;
                case 2:
                    _fishsHard.add(fishData);
            }
        });

        int count = _fishsEasy.size() + _fishsNormal.size() + _fishsHard.size();
        _log.info("FishTable: Loaded {} Fishes.", count);
    }

    /**
     * @param lvl
     * @param type
     * @param group
     * @return List of Fish that can be fished
     */
    public List<FishData> getfish(int lvl, int type, int group) {
        List<FishData> result = new LinkedList<>();
        List<FishData> _Fishs = null;
        switch (group) {
            case 0:
                _Fishs = _fishsEasy;
                break;
            case 1:
                _Fishs = _fishsNormal;
                break;
            case 2:
                _Fishs = _fishsHard;
        }
        if (_Fishs == null) {
            // the fish list is empty
            _log.warn("Fish are not defined !");
            return null;
        }
        for (FishData f : _Fishs) {
            if (f.getLevel() != lvl) {
                continue;
            }
            if (f.getType() != type) {
                continue;
            }

            result.add(f);
        }
        if (result.size() == 0) {
            _log.warn("Cant Find Any Fish!? - Lvl: " + lvl + " Type: " + type);
        }
        return result;
    }

}
