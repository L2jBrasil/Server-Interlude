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
import com.l2jbr.gameserver.datatables.ClanTable;
import com.l2jbr.gameserver.model.L2Clan;
import com.l2jbr.gameserver.model.entity.ClanHall;
import com.l2jbr.gameserver.model.entity.database.repository.ClanHallRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static com.l2jbr.gameserver.util.GameserverMessages.getMessage;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * @author Steuf
 */
public class ClanHallManager {

    private static final Logger logger = LoggerFactory.getLogger(ClanHallManager.class);
    private static ClanHallManager _instance;

    private final Map<Integer, ClanHall> _clanHall;
    private final Map<Integer, ClanHall> _freeClanHall;
    private boolean _loaded = false;

    public static ClanHallManager getInstance() {
        if (isNull(_instance)) {
            logger.info("Initializing ClanHallManager");
            _instance = new ClanHallManager();
        }
        return _instance;
    }

    public boolean loaded() {
        return _loaded;
    }

    private ClanHallManager() {
        _clanHall = new HashMap<>();
        _freeClanHall = new HashMap<>();
        load();
    }

    private void load() {
        ClanHallRepository repository = DatabaseAccess.getRepository(ClanHallRepository.class);
        repository.findAll().forEach(clanHall -> {
            int id = clanHall.getId();
            if (clanHall.getOwnerId() == 0) {
                _freeClanHall.put(id, new ClanHall(clanHall));
            } else {
                if (nonNull(ClanTable.getInstance().getClan(clanHall.getOwnerId()))) {
                    _clanHall.put(id, new ClanHall(clanHall));
                    ClanTable.getInstance().getClan(clanHall.getOwnerId()).setHasHideout(id);
                } else {
                    _freeClanHall.put(id, new ClanHall(clanHall));
                    _freeClanHall.get(id).free();
                    AuctionManager.getInstance().initNPC(id);
                }
            }
        });
        logger.info(getMessage("info.loaded.clanhall"), getClanHalls().size());
        logger.info(getMessage("info.loaded.free.clanhall"), getFreeClanHalls().size());
        _loaded = true;
    }

    /**
     * Get Map with all FreeClanHalls
     *
     * @return
     */
    public final Map<Integer, ClanHall> getFreeClanHalls() {
        return _freeClanHall;
    }

    /**
     * Get Map with all ClanHalls
     *
     * @return
     */
    public final Map<Integer, ClanHall> getClanHalls() {
        return _clanHall;
    }

    /**
     * Check is free ClanHall
     *
     * @param chId
     * @return
     */
    public final boolean isFree(int chId) {
        if (_freeClanHall.containsKey(chId)) {
            return true;
        }
        return false;
    }

    /**
     * Free a ClanHall
     *
     * @param chId
     */
    public final synchronized void setFree(int chId) {
        _freeClanHall.put(chId, _clanHall.get(chId));
        ClanTable.getInstance().getClan(_freeClanHall.get(chId).getOwnerId()).setHasHideout(0);
        _freeClanHall.get(chId).free();
        _clanHall.remove(chId);
    }

    /**
     * Set ClanHallOwner
     *
     * @param chId
     * @param clan
     */
    public final synchronized void setOwner(int chId, L2Clan clan) {
        if (!_clanHall.containsKey(chId)) {
            _clanHall.put(chId, _freeClanHall.get(chId));
            _freeClanHall.remove(chId);
        } else {
            _clanHall.get(chId).free();
        }
        ClanTable.getInstance().getClan(clan.getClanId()).setHasHideout(chId);
        _clanHall.get(chId).setOwner(clan);
    }

    /**
     * Get Clan Hall by Id
     *
     * @param clanHallId
     * @return
     */
    public final ClanHall getClanHallById(int clanHallId) {
        if (_clanHall.containsKey(clanHallId)) {
            return _clanHall.get(clanHallId);
        }
        if (_freeClanHall.containsKey(clanHallId)) {
            return _freeClanHall.get(clanHallId);
        }
        return null;
    }

    /** Get Clan Hall by x,y,z */
    /*
     * public final ClanHall getClanHall(int x, int y, int z) { for (Map.Entry<Integer, ClanHall> ch : _clanHall.entrySet()) if (ch.getValue().getZone().isInsideZone(x, y, z)) return ch.getValue(); for (Map.Entry<Integer, ClanHall> ch : _freeClanHall.entrySet()) if
     * (ch.getValue().getZone().isInsideZone(x, y, z)) return ch.getValue(); return null; }
     */

    /**
     * @param x
     * @param y
     * @param maxDist
     * @return
     */
    public final ClanHall getNearbyClanHall(int x, int y, int maxDist) {

        for (Map.Entry<Integer, ClanHall> ch : _clanHall.entrySet()) {
            if (ch.getValue().getZone().getDistanceToZone(x, y) < maxDist) {
                return ch.getValue();
            }
        }

        for (Map.Entry<Integer, ClanHall> ch : _freeClanHall.entrySet()) {
            if (ch.getValue().getZone().getDistanceToZone(x, y) < maxDist) {
                return ch.getValue();
            }
        }

        return null;
    }

    /**
     * Get Clan Hall by Owner
     *
     * @param clan
     * @return
     */
    public final ClanHall getClanHallByOwner(L2Clan clan) {
        for (Map.Entry<Integer, ClanHall> ch : _clanHall.entrySet()) {
            if (clan.getClanId() == ch.getValue().getOwnerId()) {
                return ch.getValue();
            }
        }
        return null;
    }
}