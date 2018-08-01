/* This program is free software; you can redistribute it and/or modify
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

import com.l2jbr.commons.database.DatabaseAccess;
import com.l2jbr.gameserver.datatables.NpcTable;
import com.l2jbr.gameserver.model.L2Spawn;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.model.entity.database.CastleSiegeGuard;
import com.l2jbr.gameserver.model.entity.database.NpcTemplate;
import com.l2jbr.gameserver.model.entity.database.repository.CastleSiegeGuardRepository;
import com.l2jbr.gameserver.model.entity.Castle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;


public class SiegeGuardManager {

    private static Logger _log = LoggerFactory.getLogger(SiegeGuardManager.class.getName());

    // =========================================================
    // Data Field
    private final Castle _castle;
    private final List<L2Spawn> _siegeGuardSpawn = new LinkedList<>();

    // =========================================================
    // Constructor
    public SiegeGuardManager(Castle castle) {
        _castle = castle;
    }

    // =========================================================
    // Method - Public

    /**
     * Add guard.<BR>
     * <BR>
     *
     * @param activeChar
     * @param npcId
     */
    public void addSiegeGuard(L2PcInstance activeChar, int npcId) {
        if (activeChar == null) {
            return;
        }
        addSiegeGuard(activeChar.getX(), activeChar.getY(), activeChar.getZ(), activeChar.getHeading(), npcId);
    }

    /**
     * Add guard.<BR>
     * <BR>
     *
     * @param x
     * @param y
     * @param z
     * @param heading
     * @param npcId
     */
    public void addSiegeGuard(int x, int y, int z, int heading, int npcId) {
        saveSiegeGuard(x, y, z, heading, npcId, 0);
    }

    /**
     * Hire merc.<BR>
     * <BR>
     *
     * @param activeChar
     * @param npcId
     */
    public void hireMerc(L2PcInstance activeChar, int npcId) {
        if (activeChar == null) {
            return;
        }
        hireMerc(activeChar.getX(), activeChar.getY(), activeChar.getZ(), activeChar.getHeading(), npcId);
    }

    /**
     * Hire merc.<BR>
     * <BR>
     *
     * @param x
     * @param y
     * @param z
     * @param heading
     * @param npcId
     */
    public void hireMerc(int x, int y, int z, int heading, int npcId) {
        saveSiegeGuard(x, y, z, heading, npcId, 1);
    }

    /**
     * Remove a single mercenary, identified by the npcId and location.
     * Presumably, this is used when a castle lord picks up a previously dropped ticket
     *
     * @param npcId
     * @param x
     * @param y
     * @param z
     */
    public void removeMerc(int npcId, int x, int y, int z) {
        CastleSiegeGuardRepository repository = DatabaseAccess.getRepository(CastleSiegeGuardRepository.class);
        repository.deleteMercenaryByNpcAndLocation(npcId, x, y, z);
    }

    public void removeMercs() {
        CastleSiegeGuardRepository repository = DatabaseAccess.getRepository(CastleSiegeGuardRepository.class);
        repository.deleteMercenaryByCastle(getCastle().getCastleId());
    }

    /**
     * Spawn guards.<BR>
     * <BR>
     */
    public void spawnSiegeGuard() {
        loadSiegeGuard();
        for (L2Spawn spawn : getSiegeGuardSpawn()) {
            if (spawn != null) {
                spawn.init();
            }
        }
    }

    /**
     * Unspawn guards.<BR>
     * <BR>
     */
    public void unspawnSiegeGuard() {
        for (L2Spawn spawn : getSiegeGuardSpawn()) {
            if (spawn == null) {
                continue;
            }

            spawn.stopRespawn();
            spawn.getLastSpawn().doDie(spawn.getLastSpawn());
        }

        getSiegeGuardSpawn().clear();
    }

    private void loadSiegeGuard() {
        CastleSiegeGuardRepository repository = DatabaseAccess.getRepository(CastleSiegeGuardRepository.class);
        repository.findAllByCastleAndHired(getCastle().getCastleId(), getCastle().getOwnerId() > 0 ? 1 : 0).forEach(
           castleSiegeGuard -> {
               NpcTemplate template = NpcTable.getInstance().getTemplate(castleSiegeGuard.getNpcId());
               addSiegeGuard(castleSiegeGuard, template);
        });
    }

    private void addSiegeGuard(CastleSiegeGuard castleSiegeGuard, NpcTemplate template) {
        if (template != null) {
            try {
                L2Spawn spawn = new L2Spawn(template);
                spawn.setId(castleSiegeGuard.getId());
                spawn.setAmount(1);
                spawn.setLocx(castleSiegeGuard.getX());
                spawn.setLocy(castleSiegeGuard.getY());
                spawn.setLocz(castleSiegeGuard.getZ());
                spawn.setHeading(castleSiegeGuard.getHeading());
                spawn.setRespawnDelay(castleSiegeGuard.getRespawnDelay());
                spawn.setLocation(0);

                _siegeGuardSpawn.add(spawn);
            } catch (NoSuchMethodException e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        } else {
            _log.warn("Missing npc data in npc table for id: {}", castleSiegeGuard.getNpcId());
        }
    }

    private void saveSiegeGuard(int x, int y, int z, int heading, int npcId, int isHire) {
        int respawnDelay = isHire == 1 ? 0 : 600;
        CastleSiegeGuard guard = new CastleSiegeGuard(getCastle().getCastleId(), npcId, x, y, z, heading, respawnDelay, isHire);
        CastleSiegeGuardRepository repository = DatabaseAccess.getRepository(CastleSiegeGuardRepository.class);
        repository.save(guard);
    }


    public final Castle getCastle() {
        return _castle;
    }

    public final List<L2Spawn> getSiegeGuardSpawn() {
        return _siegeGuardSpawn;
    }
}
