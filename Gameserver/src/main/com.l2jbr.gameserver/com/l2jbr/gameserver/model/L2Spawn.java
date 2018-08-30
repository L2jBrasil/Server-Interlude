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
package com.l2jbr.gameserver.model;

import com.l2jbr.commons.Config;
import com.l2jbr.commons.util.Rnd;
import com.l2jbr.gameserver.GeoData;
import com.l2jbr.gameserver.Territory;
import com.l2jbr.gameserver.ThreadPoolManager;
import com.l2jbr.gameserver.idfactory.IdFactory;
import com.l2jbr.gameserver.model.actor.instance.L2NpcInstance;
import com.l2jbr.gameserver.model.entity.database.NpcTemplate;
import com.l2jbr.gameserver.model.entity.database.Spawn;
import com.l2jbr.gameserver.templates.ISpawn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;

import static com.l2jbr.gameserver.templates.NpcType.L2Minion;
import static com.l2jbr.gameserver.templates.NpcType.L2Pet;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNullElse;

/**
 * This class manages the entity and respawn of a group of L2NpcInstance that are in the same are and have the same type. <B><U> Concept</U> :</B><BR>
 * <BR>
 * L2NpcInstance can be spawned either in a random position into a location area (if Lox=0 and Locy=0), either at an exact position. The heading of the L2NpcInstance can be a random heading if not defined (value= -1) or an exact heading (ex : merchant...).<BR>
 * <BR>
 *
 * @author Nightmare
 */
public class L2Spawn {
    protected static final Logger _log = LoggerFactory.getLogger(L2Spawn.class.getName());

    private ISpawn entity;
    private final NpcTemplate _template;

    /**
     * The current number of L2NpcInstance managed by this L2Spawn
     */
    private int _currentCount;

    /**
     * The current number of SpawnTask in progress or stand by of this L2Spawn
     */
    private int _scheduledCount;

    /**
     * The generic constructor of L2NpcInstance managed by this L2Spawn
     */
    private Constructor<?> _constructor;

    /**
     * If True a L2NpcInstance is respawned each time that another is killed
     */
    private boolean _doRespawn;

    private L2NpcInstance _lastSpawn;
    private final static List<SpawnListener> _spawnListeners = new LinkedList<>();

    /**
     * Constructor of L2Spawn.<BR>
     * <BR>
     * <B><U> Concept</U> :</B><BR>
     * <BR>
     * Each L2Spawn owns generic and static properties (ex : RewardExp, RewardSP, AggroRange...). All of those properties are stored in a different L2NpcTemplate for each type of L2Spawn. Each template is loaded once in the server cache memory (reduce memory use). When a new instance of L2Spawn is
     * created, server just create a link between the instance and the template. This link is stored in <B>_template</B><BR>
     * <BR>
     * Each L2NpcInstance is linked to a L2Spawn that manages its entity and respawn (delay, location...). This link is stored in <B>_spawn</B> of the L2NpcInstance<BR>
     * <BR>
     * <B><U> Actions</U> :</B><BR>
     * <BR>
     * <li>Set the _template of the L2Spawn</li> <li>Calculate the implementationName used to generate the generic constructor of L2NpcInstance managed by this L2Spawn</li> <li>Create the generic constructor of L2NpcInstance managed by this L2Spawn</li><BR>
     * <BR>
     *
     */
    public L2Spawn(NpcTemplate mobTemplate) throws SecurityException, NoSuchMethodException {
        _template = mobTemplate;

        if (_template == null) {
            return;
        }

        _constructor = _template.getType().getInstanceClass().getConstructor(int.class, NpcTemplate.class);
        entity = new Spawn();
    }

    public L2Spawn(ISpawn entity) throws NoSuchMethodException {
        this(entity.getNpcTemplate());
        this.entity = entity;
    }


    /**
     * Decrease the current number of L2NpcInstance of this L2Spawn and if necessary create a SpawnTask to launch after the respawn Delay.<BR>
     * <BR>
     * <B><U> Actions</U> :</B><BR>
     * <BR>
     * <li>Decrease the current number of L2NpcInstance of this L2Spawn</li> <li>Check if respawn is possible to prevent multiple respawning caused by lag</li> <li>Update the current number of SpawnTask in progress or stand by of this L2Spawn</li> <li>Create a new SpawnTask to launch after the
     * respawn Delay</li><BR>
     * <BR>
     * <FONT COLOR=#FF0000><B> <U>Caution</U> : A respawn is possible ONLY if _doRespawn=True and _scheduledCount + _currentCount < _maximumCount</B></FONT><BR>
     * <BR>
     *
     */
    public void decreaseCount(/* int npcId */L2NpcInstance oldNpc) {
        // Decrease the current number of L2NpcInstance of this L2Spawn
        _currentCount--;

        // Check if respawn is possible to prevent multiple respawning caused by lag
        if (_doRespawn && ((_scheduledCount + _currentCount) < entity.getCount())) {
            // Update the current number of SpawnTask in progress or stand by of this L2Spawn
            _scheduledCount++;

            // Create a new SpawnTask to launch after the respawn Delay
            // ClientScheduler.getInstance().scheduleLow(new SpawnTask(npcId), _respawnDelay);
            ThreadPoolManager.getInstance().scheduleGeneral(new SpawnTask(oldNpc), entity.getRespawnDelay() * 1000);
        }
    }

    /**
     * Create the initial spawning and set _doRespawn to True.
     *
     * @return The number of L2NpcInstance that were spawned
     */
    public int init() {
        while (_currentCount < entity.getCount()) {
            doSpawn();
        }
        _doRespawn = true;

        return _currentCount;
    }

    public L2NpcInstance spawnOne() {
        return doSpawn();
    }

    /**
     * Set _doRespawn to False to stop respawn in this L2Spawn.
     */
    public void stopRespawn() {
        _doRespawn = false;
    }

    /**
     * Set _doRespawn to True to start or restart respawn in this L2Spawn.
     */
    public void startRespawn() {
        _doRespawn = true;
    }

    /**
     * Create the L2NpcInstance, add it to the world and launch its OnSpawn action.<BR>
     * <BR>
     * <B><U> Concept</U> :</B><BR>
     * <BR>
     * L2NpcInstance can be spawned either in a random position into a location area (if Lox=0 and Locy=0), either at an exact position. The heading of the L2NpcInstance can be a random heading if not defined (value= -1) or an exact heading (ex : merchant...).<BR>
     * <BR>
     * <B><U> Actions for an random entity into location area</U> : <I>(if Locx=0 and Locy=0)</I></B><BR>
     * <BR>
     * <li>Get L2NpcInstance Init parameters and its generate an Identifier</li> <li>Call the constructor of the L2NpcInstance</li> <li>Calculate the random position in the location area (if Locx=0 and Locy=0) or get its exact position from the L2Spawn</li> <li>Set the position of the L2NpcInstance</li>
     * <li>Set the HP and MP of the L2NpcInstance to the max</li> <li>Set the heading of the L2NpcInstance (random heading if not defined : value=-1)</li> <li>Link the L2NpcInstance to this L2Spawn</li> <li>Init other values of the L2NpcInstance (ex : from its L2CharTemplate for INT, STR, DEX...)
     * and add it in the world</li> <li>Launch the action OnSpawn fo the L2NpcInstance</li><BR>
     * <BR>
     * <li>Increase the current number of L2NpcInstance managed by this L2Spawn</li><BR>
     * <BR>
     *
     */
    public L2NpcInstance doSpawn() {
        L2NpcInstance mob = null;
        try {
            if (L2Pet.equals(_template.getType()) || L2Minion.equals(_template.getType())) {
                _currentCount++;
                return mob;
            }


            // Call the constructor of the L2NpcInstance
            // (can be a L2ArtefactInstance, L2FriendlyMobInstance, L2GuardInstance, L2MonsterInstance, L2SiegeGuardInstance,
            // L2FeedableBeastInstance, L2TamedBeastInstance, L2FolkInstance or L2TvTEventNpcInstance)
            Object tmp = _constructor.newInstance(IdFactory.getInstance().getNextId(),  _template );

            // Check if the Instance is a L2NpcInstance
            if (!(tmp instanceof L2NpcInstance)) {
                return mob;
            }
            mob = (L2NpcInstance) tmp;
            return intializeNpcInstance(mob);
        } catch (Exception e) {
            _log.warn( "NPC {} class not found", _template.getId());
            _log.error(e.getLocalizedMessage(), e);
        }
        return mob;
    }

    private L2NpcInstance intializeNpcInstance(L2NpcInstance mob) {
        int newlocx, newlocy, newlocz;

        // If Locx=0 and Locy=0, the L2NpcInstance must be spawned in an area defined by location
        if ((getLocx() == 0) && (getLocy() == 0)) {
            if (getLocation() == 0) {
                return mob;
            }

            // Calculate the random position in the location area
            int p[] = Territory.getInstance().getRandomPoint(getLocation());

            // Set the calculated position of the L2NpcInstance
            newlocx = p[0];
            newlocy = p[1];
            newlocz = GeoData.getInstance().getSpawnHeight(newlocx, newlocy, p[2], p[3], entity.getId());
        } else {
            // The L2NpcInstance is spawned at the exact position (Lox, Locy, Locz)
            newlocx = getLocx();
            newlocy = getLocy();
            if (Config.GEODATA > 0) {
                newlocz = GeoData.getInstance().getSpawnHeight(newlocx, newlocy, getLocz(), getLocz(), entity.getId());
            } else {
                newlocz = getLocz();
            }
        }

        for (L2Effect f : mob.getAllEffects()) {
            if (f != null) {
                mob.removeEffect(f);
            }
        }

        // Set the HP and MP of the L2NpcInstance to the max
        mob.setCurrentHpMp(mob.getMaxHp(), mob.getMaxMp());

        // Set the heading of the L2NpcInstance (random heading if not defined)
        if (getHeading() == -1) {
            mob.setHeading(Rnd.nextInt(61794));
        } else {
            mob.setHeading(getHeading());
        }

        // Reset decay info
        mob.setDecayed(false);

        // Link the L2NpcInstance to this L2Spawn
        mob.setSpawn(this);

        // Init other values of the L2NpcInstance (ex : from its L2CharTemplate for INT, STR, DEX...) and add it in the world as a visible object
        mob.spawnMe(newlocx, newlocy, newlocz);

        L2Spawn.notifyNpcSpawned(mob);

        _lastSpawn = mob;

        if (Config.DEBUG) {
            _log.debug("spawned Mob ID: {}, at: {} x, {} y, {} z", _template.getId(), mob.getX(), mob.getY(), mob.getZ());
        }

        _currentCount++;
        return mob;
    }

    public static void addSpawnListener(SpawnListener listener) {
        synchronized (_spawnListeners) {
            _spawnListeners.add(listener);
        }
    }

    public static void removeSpawnListener(SpawnListener listener) {
        synchronized (_spawnListeners) {
            _spawnListeners.remove(listener);
        }
    }

    private static void notifyNpcSpawned(L2NpcInstance npc) {
        synchronized (_spawnListeners) {
            for (SpawnListener listener : _spawnListeners) {
                listener.npcSpawned(npc);
            }
        }
    }

    public void setRespawnDelay(int delay) {
        if (delay < 0) {
            _log.warn("respawn delay is negative for spawnId: {}", entity.getId());
        }

        if (delay < 60) {
            delay = 60;
        }
        entity.setRespawnDelay(delay);
    }

    public L2NpcInstance getLastSpawn() {
        return _lastSpawn;
    }


    private void respawnNpc(L2NpcInstance oldNpc) {
        oldNpc.refreshID();
        intializeNpcInstance(oldNpc);
    }

    public NpcTemplate getTemplate() {
        return _template;
    }

    public int getAmount() {
        return entity.getCount();
    }

    public int getId() {
        return requireNonNullElse(entity.getId(), 0);
    }

    public int getLocation() {
        return entity.getLocId();
    }

    public int getLocx() {
        return entity.getX();
    }

    public int getLocy() {
        return entity.getY();
    }

    public int getLocz() {
        return entity.getZ();
    }

    public int getNpcId() {
        if(isNull(_template)) {
            return 0;
        }
        return requireNonNullElse(_template.getId(), 0);
    }
    public int getHeading() {
        return entity.getHeading();
    }

    public int getRespawnDelay() {
        return entity.getRespawnDelay() * 1000;
    }

    public int getRespawnMinDelay() {
        return entity.getRespawnMinDelay();
    }

    public int getRespawnMaxDelay() {
        return entity.getRespawnMaxDelay();
    }

    public void setAmount(int amount) {
        entity.setCount(amount);
    }

    public void setId(int id) {
        entity.setId(id);
    }

    public void setLocation(int location) {
        entity.setLocId(location);
    }

    public void setLocx(int locx) {
        entity.setX(locx);
    }

    public void setLocy(int locy) {
        entity.setY(locy);
    }

    public void setLocz(int locz) {
        entity.setZ(locz);
    }

    public void setHeading(int heading) {
        entity.setHeading(heading);
    }


    class SpawnTask implements Runnable {

        private final L2NpcInstance _oldNpc;

        SpawnTask(L2NpcInstance pOldNpc) {
            _oldNpc = pOldNpc;
        }

        @Override
        public void run() {
            try {
                respawnNpc(_oldNpc);
            } catch (Exception e) {
                _log.warn(e.getLocalizedMessage(), e);
            }
            _scheduledCount--;
        }
    }
}
