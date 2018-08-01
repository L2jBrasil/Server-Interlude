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

import com.l2jbr.commons.Config;
import com.l2jbr.commons.util.Rnd;
import com.l2jbr.gameserver.GmListTable;
import com.l2jbr.gameserver.ThreadPoolManager;
import com.l2jbr.gameserver.datatables.NpcTable;
import com.l2jbr.gameserver.datatables.SpawnTable;
import com.l2jbr.gameserver.model.L2Spawn;
import com.l2jbr.gameserver.model.actor.instance.L2RaidBossInstance;
import com.l2jbr.gameserver.model.entity.database.NpcTemplate;
import com.l2jbr.gameserver.model.entity.database.RaidbossSpawn;
import com.l2jbr.gameserver.model.entity.database.repository.RaidBossSpawnListRepository;
import com.l2jbr.gameserver.templates.StatsSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import static com.l2jbr.commons.database.DatabaseAccess.getRepository;
import static com.l2jbr.gameserver.templates.NpcType.L2RaidBoss;
import static java.util.Objects.isNull;

/**
 * @author godson
 */
public class RaidBossSpawnManager {

    private static Logger _log = LoggerFactory.getLogger(RaidBossSpawnManager.class.getName());

    private static RaidBossSpawnManager _instance;
    private static Map<Integer, L2RaidBossInstance> _bosses;
    private static Map<Integer, L2Spawn> _spawns;
    private Map<Integer, StatsSet> _storedInfo;
    private static Map<Integer, ScheduledFuture<?>> _schedules;

    public enum StatusEnum {
        ALIVE,
        DEAD,
        UNDEFINED
    }

    private RaidBossSpawnManager() {
        init();
    }

    public static RaidBossSpawnManager getInstance() {
        if (isNull(_instance)) {
            _instance = new RaidBossSpawnManager();
        }

        return _instance;
    }

    private void init() {
        _bosses = new LinkedHashMap<>();
        _schedules = new LinkedHashMap<>();
        _storedInfo = new ConcurrentHashMap<>();
        _spawns = new LinkedHashMap<>();


        getRepository(RaidBossSpawnListRepository.class).findAll().forEach(spawn -> {
            NpcTemplate template = getValidTemplate(spawn.getId());
            if (Objects.nonNull(template)) {
                try {
                    L2Spawn spawnDat = new L2Spawn(spawn);
                    addNewSpawn(spawnDat, spawn.getRespawnTime(), spawn.getCurrentHp(),spawn.getCurrentMp(), false);
                } catch (NoSuchMethodException e) {
                    _log.warn("RaidBossSpawnManager: Could not load raidboss #{} from DB", spawn.getId());
                    _log.error(e.getLocalizedMessage(), e);
                }
            } else {
                _log.warn("RaidBossSpawnManager: Could not load raidboss #{} from DB. Template not found", spawn.getId());
            }
        });
    }

    private class spawnSchedule implements Runnable {
        private final int bossId;

        public spawnSchedule(int npcId) {
            bossId = npcId;
        }

        @Override
        public void run() {
            L2RaidBossInstance raidboss = null;

            if (bossId == 25328) {
                raidboss = DayNightSpawnManager.getInstance().handleBoss(_spawns.get(bossId));
            } else {
                raidboss = (L2RaidBossInstance) _spawns.get(bossId).doSpawn();
            }

            if (raidboss != null) {
                raidboss.setRaidStatus(StatusEnum.ALIVE);

                StatsSet info = new StatsSet();
                info.set("currentHP", raidboss.getCurrentHp());
                info.set("currentMP", raidboss.getCurrentMp());
                info.set("respawnTime", 0L);

                _storedInfo.put(bossId, info);

                GmListTable.broadcastMessageToGMs("Spawning Raid Boss " + raidboss.getName());

                _bosses.put(bossId, raidboss);
            }

            _schedules.remove(bossId);
        }
    }

    public void updateStatus(L2RaidBossInstance boss, boolean isBossDead) {
        if (!_storedInfo.containsKey(boss.getNpcId())) {
            return;
        }

        StatsSet info = _storedInfo.get(boss.getNpcId());

        if (isBossDead) {
            boss.setRaidStatus(StatusEnum.DEAD);

            long respawnTime;
            int RespawnMinDelay = boss.getSpawn().getRespawnMinDelay();
            int RespawnMaxDelay = boss.getSpawn().getRespawnMaxDelay();
            long respawn_delay = Rnd.get((int) (RespawnMinDelay * 1000 * Config.RAID_MIN_RESPAWN_MULTIPLIER), (int) (RespawnMaxDelay * 1000 * Config.RAID_MAX_RESPAWN_MULTIPLIER));
            respawnTime = Calendar.getInstance().getTimeInMillis() + respawn_delay;

            info.set("currentHP", boss.getMaxHp());
            info.set("currentMP", boss.getMaxMp());
            info.set("respawnTime", respawnTime);

            _log.info("RaidBossSpawnManager: Updated " + boss.getName() + " respawn time to " + respawnTime);

            ScheduledFuture<?> futureSpawn;
            futureSpawn = ThreadPoolManager.getInstance().scheduleGeneral(new spawnSchedule(boss.getNpcId()), respawn_delay);

            _schedules.put(boss.getNpcId(), futureSpawn);
            // To update immediately Database uncomment on the following line, to post the hour of respawn raid boss on your site for example or to envisage a crash landing of the waiter.
            // updateDb();
        } else {
            boss.setRaidStatus(StatusEnum.ALIVE);

            info.set("currentHP", boss.getCurrentHp());
            info.set("currentMP", boss.getCurrentMp());
            info.set("respawnTime", 0L);
        }

        _storedInfo.remove(boss.getNpcId());
        _storedInfo.put(boss.getNpcId(), info);
    }

    public void addNewSpawn(L2Spawn spawnDat, long respawnTime, double currentHP, double currentMP, boolean storeInDb) {
        if (spawnDat == null) {
            return;
        }
        if (_spawns.containsKey(spawnDat.getNpcId())) {
            return;
        }

        int bossId = spawnDat.getNpcId();
        long time = Calendar.getInstance().getTimeInMillis();

        SpawnTable.getInstance().addNewSpawn(spawnDat, false);

        if ((respawnTime == 0L) || (time > respawnTime)) {
            L2RaidBossInstance raidboss = null;

            if (bossId == 25328) {
                raidboss = DayNightSpawnManager.getInstance().handleBoss(spawnDat);
            } else {
                raidboss = (L2RaidBossInstance) spawnDat.doSpawn();
            }

            if (raidboss != null) {
                raidboss.setCurrentHp(currentHP);
                raidboss.setCurrentMp(currentMP);
                raidboss.setRaidStatus(StatusEnum.ALIVE);

                _bosses.put(bossId, raidboss);

                StatsSet info = new StatsSet();
                info.set("currentHP", currentHP);
                info.set("currentMP", currentMP);
                info.set("respawnTime", 0L);

                _storedInfo.put(bossId, info);
            }
        } else {
            ScheduledFuture<?> futureSpawn;
            long spawnTime = respawnTime - Calendar.getInstance().getTimeInMillis();

            futureSpawn = ThreadPoolManager.getInstance().scheduleGeneral(new spawnSchedule(bossId), spawnTime);

            _schedules.put(bossId, futureSpawn);
        }

        _spawns.put(bossId, spawnDat);

        if (storeInDb) {
            RaidbossSpawn spawnList = new RaidbossSpawn(spawnDat.getNpcId(), spawnDat.getAmount(),
                spawnDat.getLocx(), spawnDat.getLocy(), spawnDat.getLocz(), spawnDat.getHeading(), respawnTime, currentHP, currentMP);
            RaidBossSpawnListRepository repository = getRepository(RaidBossSpawnListRepository.class);
            repository.save(spawnList);
        }
    }

    public void deleteSpawn(L2Spawn spawnDat, boolean updateDb) {
        if (spawnDat == null) {
            return;
        }
        if (!_spawns.containsKey(spawnDat.getNpcId())) {
            return;
        }

        int bossId = spawnDat.getNpcId();

        SpawnTable.getInstance().deleteSpawn(spawnDat, false);
        _spawns.remove(bossId);

        if (_bosses.containsKey(bossId)) {
            _bosses.remove(bossId);
        }

        if (_schedules.containsKey(bossId)) {
            ScheduledFuture<?> f = _schedules.get(bossId);
            f.cancel(true);
            _schedules.remove(bossId);
        }

        if (_storedInfo.containsKey(bossId)) {
            _storedInfo.remove(bossId);
        }

        if (updateDb) {
            RaidBossSpawnListRepository repository = getRepository(RaidBossSpawnListRepository.class);
            repository.deleteById(bossId);
        }
    }

    private void updateDb() {
        for (Map.Entry<Integer, StatsSet> entry : _storedInfo.entrySet()) {

            L2RaidBossInstance boss = _bosses.get(entry.getKey());

            if (boss == null) {
                continue;
            }

            StatsSet info = entry.getValue();

            if (info == null) {
                continue;
            }

            RaidBossSpawnListRepository repository = getRepository(RaidBossSpawnListRepository.class);
            repository.updateById(entry.getKey(), info.getLong("respawnTime"), info.getDouble("currentHP"), info.getDouble("currentMP"));

        }
    }

    public String[] getAllRaidBossStatus() {
        String[] msg = new String[_bosses == null ? 0 : _bosses.size()];

        if (_bosses == null) {
            msg[0] = "None";
            return msg;
        }

        int index = 0;

        for (int i : _bosses.keySet()) {
            L2RaidBossInstance boss = _bosses.get(i);

            msg[index] = boss.getName() + ": " + boss.getRaidStatus().name();
            index++;
        }

        return msg;
    }

    public String getRaidBossStatus(int bossId) {
        String msg = "RaidBoss Status....\n";

        if (_bosses == null) {
            msg += "None";
            return msg;
        }

        if (_bosses.containsKey(bossId)) {
            L2RaidBossInstance boss = _bosses.get(bossId);

            msg += boss.getName() + ": " + boss.getRaidStatus().name();
        }

        return msg;
    }

    public StatusEnum getRaidBossStatusId(int bossId) {
        if (_bosses.containsKey(bossId)) {
            return _bosses.get(bossId).getRaidStatus();
        } else if (_schedules.containsKey(bossId)) {
            return StatusEnum.DEAD;
        } else {
            return StatusEnum.UNDEFINED;
        }
    }

    public NpcTemplate getValidTemplate(int bossId) {
        NpcTemplate template = NpcTable.getInstance().getTemplate(bossId);
        if (isNull(template) || !L2RaidBoss.equals(template.getType())) {
            return null;
        }
        return template;
    }

    public void notifySpawnNightBoss(L2RaidBossInstance raidboss) {
        StatsSet info = new StatsSet();
        info.set("currentHP", raidboss.getCurrentHp());
        info.set("currentMP", raidboss.getCurrentMp());
        info.set("respawnTime", 0L);

        raidboss.setRaidStatus(StatusEnum.ALIVE);

        _storedInfo.put(raidboss.getNpcId(), info);

        GmListTable.broadcastMessageToGMs("Spawning Raid Boss " + raidboss.getName());

        _bosses.put(raidboss.getNpcId(), raidboss);
    }

    public boolean isDefined(int bossId) {
        return _spawns.containsKey(bossId);
    }

    public Map<Integer, L2RaidBossInstance> getBosses() {
        return _bosses;
    }

    public Map<Integer, L2Spawn> getSpawns() {
        return _spawns;
    }

    public void reloadBosses() {
        init();
    }

    /**
     * Saves all raidboss status and then clears all info from memory, including all schedules.
     */

    public void cleanUp() {
        updateDb();

        _bosses.clear();

        if (_schedules != null) {
            for (Integer bossId : _schedules.keySet()) {
                ScheduledFuture<?> f = _schedules.get(bossId);
                f.cancel(true);
            }
        }

        _schedules.clear();
        _storedInfo.clear();
        _spawns.clear();
    }
}
