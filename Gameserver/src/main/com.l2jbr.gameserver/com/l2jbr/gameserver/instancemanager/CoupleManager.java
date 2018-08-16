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

import com.l2jbr.gameserver.model.L2World;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.model.entity.database.Wedding;
import com.l2jbr.gameserver.model.entity.database.repository.ModsWeddingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.l2jbr.commons.database.DatabaseAccess.getRepository;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNullElse;

/**
 * @author evill33t
 */
public class CoupleManager {
    private static final Logger _log = LoggerFactory.getLogger(CoupleManager.class.getName());

    private static CoupleManager _instance;

    public static CoupleManager getInstance() {
        if (isNull(_instance)) {
            _log.info("L2JMOD: Initializing CoupleManager");
            _instance = new CoupleManager();
            _instance.load();
        }
        return _instance;
    }

    private CoupleManager() {
        _couples = new ArrayList<>();
    }

    private List<Wedding> _couples;


    public void reload() {
        getCouples().clear();
        load();
    }

    private void load() {
        getRepository(ModsWeddingRepository.class).findAll().forEach(wedding -> _couples.add(wedding));
        _log.info("Loaded: {} couples(s)", getCouples().size());
    }


    public final Wedding getCouple(int coupleId) {
        int index = getCoupleIndex(coupleId);
        if (index >= 0) {
            return getCouples().get(index);
        }
        return null;
    }

    public void createCouple(L2PcInstance player1, L2PcInstance player2) {
        if ((player1 != null) && (player2 != null)) {
            if ((player1.getPartnerId() == 0) && (player2.getPartnerId() == 0)) {
                int _player1id = player1.getObjectId();
                int _player2id = player2.getObjectId();

                Wedding wedding = new Wedding(player1.getObjectId(), player2.getObjectId());

                getRepository(ModsWeddingRepository.class).save(wedding);
                _couples.add(wedding);
                var weddingId = requireNonNullElse(wedding.getId(), 0);
                player1.setPartnerId(_player2id);
                player1.setCoupleId(weddingId);
                player2.setPartnerId(_player1id);
                player2.setCoupleId(weddingId);
            }
        }
    }

    public void deleteCouple(int coupleId) {
        int index = getCoupleIndex(coupleId);
        Wedding couple = getCouples().get(index);
        if (couple != null) {
            L2PcInstance player1 = (L2PcInstance) L2World.getInstance().findObject(couple.getPlayer1Id());
            L2PcInstance player2 = (L2PcInstance) L2World.getInstance().findObject(couple.getPlayer2Id());
            if (player1 != null) {
                player1.setPartnerId(0);
                player1.setMarried(false);
                player1.setCoupleId(0);

            }
            if (player2 != null) {
                player2.setPartnerId(0);
                player2.setMarried(false);
                player2.setCoupleId(0);

            }
            couple.divorce();
            getCouples().remove(index);
        }
    }

    public final int getCoupleIndex(int coupleId) {
        int i = 0;
        for (Wedding temp : getCouples()) {
            if ((temp != null) && (Objects.equals(coupleId, temp.getId()))) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public final List<Wedding> getCouples() {
        return _couples;
    }
}
