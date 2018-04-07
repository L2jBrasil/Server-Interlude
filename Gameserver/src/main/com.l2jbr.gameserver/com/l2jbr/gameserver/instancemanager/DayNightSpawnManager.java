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
package com.l2jbr.gameserver.instancemanager;

import com.l2jbr.gameserver.GameTimeController;
import com.l2jbr.gameserver.model.L2Spawn;
import com.l2jbr.gameserver.model.actor.instance.L2NpcInstance;
import com.l2jbr.gameserver.model.actor.instance.L2RaidBossInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * This class ...
 *
 * @author godson
 * @version $Revision: $ $Date: $
 */

public class DayNightSpawnManager {

    private Logger _log = LoggerFactory.getLogger(DayNightSpawnManager.class.getName());

    private static DayNightSpawnManager _instance;
    private Set<L2Spawn> dayCreatures;
    private Set<L2Spawn> nightCreatures;
    private List<L2NpcInstance> spawnedCreatures;
    private static Map<L2Spawn, L2RaidBossInstance> bosses;

    public static DayNightSpawnManager getInstance() {
        if (_instance == null) {
            _instance = new DayNightSpawnManager();
        }
        return _instance;
    }

    private DayNightSpawnManager() {
        dayCreatures = ConcurrentHashMap.newKeySet();
        nightCreatures = ConcurrentHashMap.newKeySet();
        spawnedCreatures = new LinkedList<>();
        bosses = new LinkedHashMap<>();

        _log.info("DayNightSpawnManager: Day/Night handler initialized");
    }

    public void addDayCreature(L2Spawn spawnDat) {
        dayCreatures.add(spawnDat);
    }

    public void addNightCreature(L2Spawn spawnDat) {
        nightCreatures.add(spawnDat);
    }

    /*
     * Spawn Day Creatures, and Unspawn Night Creatures
     */
    public void spawnDayCreatures() {
        unspawnCreatures();
        dayCreatures.forEach(this::doSpawn);

        _log.info("DayNightSpawnManager: Spawning " + spawnedCreatures.size() + " day creatures");
    }

    private void doSpawn(L2Spawn spawn) {
        L2NpcInstance creature = spawn.doSpawn();
        if(creature == null) {
            return;
        }
        spawn.startRespawn();
        spawnedCreatures.add(creature);
    }

    private void unspawnCreatures() {
        spawnedCreatures.forEach(creature -> {
            creature.getSpawn().stopRespawn();
            creature.deleteMe();
        });
        _log.info("DayNightSpawnManager: Deleted " + spawnedCreatures.size() + " night creatures");
        spawnedCreatures.clear();
    }

    /*
     * Spawn Night Creatures, and Unspawn Day Creatures
     */
    public void spawnNightCreatures() {
        unspawnCreatures();
        nightCreatures.forEach(this::doSpawn);

        _log.info("DayNightSpawnManager: Spawning " + spawnedCreatures.size() + " night creatures");
    }

    private void changeMode(int mode) {
        if ((nightCreatures.size() == 0) && (dayCreatures.size() == 0)) {
            return;
        }

        switch (mode) {
            case 0:
                spawnDayCreatures();
                specialNightBoss(0);
                break;
            case 1:
                spawnNightCreatures();
                specialNightBoss(1);
                break;
            default:
                _log.warn("DayNightSpawnManager: Wrong mode sent");
                break;
        }
    }

    public void notifyChangeMode() {
        try {
            if (GameTimeController.getInstance().isNowNight()) {
                changeMode(1);
            } else {
                changeMode(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cleanUp() {
        nightCreatures.clear();
        dayCreatures.clear();
        bosses.clear();
    }

    private void specialNightBoss(int mode) {
        try {
            for (L2Spawn spawn : bosses.keySet()) {
                L2RaidBossInstance boss = bosses.get(spawn);

                if ((boss == null) && (mode == 1)) {
                    boss = (L2RaidBossInstance) spawn.doSpawn();
                    RaidBossSpawnManager.getInstance().notifySpawnNightBoss(boss);
                    bosses.remove(spawn);
                    bosses.put(spawn, boss);
                    continue;
                }

                if ((boss == null) && (mode == 0)) {
                    continue;
                }

                if ((boss.getNpcId() == 25328) && boss.getRaidStatus().equals(RaidBossSpawnManager.StatusEnum.ALIVE)) {
                    handleHellmans(boss, mode);
                }
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleHellmans(L2RaidBossInstance boss, int mode) {
        switch (mode) {
            case 0:
                boss.deleteMe();
                _log.info("DayNightSpawnManager: Deleting Hellman raidboss");
                break;
            case 1:
                boss.spawnMe();
                _log.info("DayNightSpawnManager: Spawning Hellman raidboss");
                break;
        }
    }

    public L2RaidBossInstance handleBoss(L2Spawn spawnDat) {
        if (bosses.containsKey(spawnDat)) {
            return bosses.get(spawnDat);
        }

        if (GameTimeController.getInstance().isNowNight()) {
            L2RaidBossInstance raidboss = (L2RaidBossInstance) spawnDat.doSpawn();
            bosses.put(spawnDat, raidboss);

            return raidboss;
        }
        bosses.put(spawnDat, null);

        return null;
    }
}
