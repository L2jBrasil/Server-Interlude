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

import com.l2jbr.commons.Config;
import com.l2jbr.gameserver.instancemanager.DayNightSpawnManager;
import com.l2jbr.gameserver.model.L2Spawn;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.model.entity.database.Spawn;
import com.l2jbr.gameserver.model.entity.database.repository.SpawnListRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.l2jbr.commons.database.DatabaseAccess.getRepository;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class SpawnTable {
    private static Logger _log = LoggerFactory.getLogger(SpawnTable.class.getName());

    private static SpawnTable _instance;

    private final Map<Integer, L2Spawn> _spawntable = new LinkedHashMap<>();
    private int _npcSpawnCount;

    private int _highestId;

    public static SpawnTable getInstance() {
        return isNull(_instance) ? _instance = new SpawnTable() : _instance;
    }

    private SpawnTable() {
        if (!Config.ALT_DEV_NO_SPAWNS) {
            fillSpawnTable();
        }
    }

    public Map<Integer, L2Spawn> getSpawnTable() {
        return _spawntable;
    }

    private void fillSpawnTable() {
        getRepository(SpawnListRepository.class).findAll().forEach(spawn -> {

            if (nonNull(spawn.getNpcTemplate())) {
                doSpawn(spawn);
            } else {
                _log.warn("SpawnTable: Data missing in NPC table for ID: {} .", spawn.getNpcTemplateId());
            }
        });

        _log.info("SpawnTable: Loaded {} NpcTemplate Spawn Locations.", _spawntable.size());
        _log.debug("SpawnTable: Spawning completed, total number of NPCs in the world: {}", _npcSpawnCount);
    }

    private void doSpawn(Spawn spawn) {
        switch (spawn.getNpcTemplate().getType()) {
            case L2ClassMaster:
                if(Config.ALLOW_CLASS_MASTERS) {
                   break;
                }
            case L2SiegeGuard:
            case L2RaidBoss:
                return;
        }

        try {
            var spawnDat = new L2Spawn(spawn);
            switch (spawn.getPeriodOfDay()) {
                case 0: // default
                    _npcSpawnCount += spawnDat.init();
                    break;
                case 1: // Day
                    DayNightSpawnManager.getInstance().addDayCreature(spawnDat);
                    _npcSpawnCount++;
                    break;
                case 2: // Night
                    DayNightSpawnManager.getInstance().addNightCreature(spawnDat);
                    _npcSpawnCount++;
                    break;
            }

            _spawntable.put(spawnDat.getId(), spawnDat);
            if (spawnDat.getId() > _highestId) {
                _highestId = spawnDat.getId();
            }
        } catch (NoSuchMethodException e) {
            _log.error("SpawnTable: Error on spawn id {}", spawn.getId());
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public L2Spawn getTemplate(int id) {
        return _spawntable.get(id);
    }

    public void addNewSpawn(L2Spawn spawn, boolean storeInDb) {
        _highestId++;
        spawn.setId(_highestId);
        _spawntable.put(_highestId, spawn);

        if (storeInDb) {
            Spawn spawnlist = new Spawn(spawn);
            SpawnListRepository repository = getRepository(SpawnListRepository.class);
            repository.save(spawnlist);
        }
    }

    public void deleteSpawn(L2Spawn spawn, boolean updateDb) {
        if (_spawntable.remove(spawn.getId()) == null) {
            return;
        }

        if (updateDb) {
            SpawnListRepository repository = getRepository(SpawnListRepository.class);
            repository.deleteById(spawn.getId());
        }
    }

    // just wrapper
    public void reloadAll() {
        fillSpawnTable();
    }

    /**
     * Get all the spawn of a NPC
     *
     * @param activeChar
     * @param npcId         : ID of the NPC to find.
     * @param teleportIndex
     */
    public void findNPCInstances(L2PcInstance activeChar, int npcId, int teleportIndex) {
        int index = 0;
        for (L2Spawn spawn : _spawntable.values()) {
            if (npcId == spawn.getNpcId()) {
                index++;

                if (teleportIndex > -1) {
                    if (teleportIndex == index) {
                        activeChar.teleToLocation(spawn.getLocx(), spawn.getLocy(), spawn.getLocz(), true);
                    }
                } else {
                    activeChar.sendMessage(index + " - " + spawn.getTemplate().getName() + " (" + spawn.getId() + "): " + spawn.getLocx() + " " + spawn.getLocy() + " " + spawn.getLocz());
                }
            }
        }

        if (index == 0) {
            activeChar.sendMessage("No current spawns found.");
        }
    }
}
