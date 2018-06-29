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
import com.l2jbr.gameserver.model.L2TeleportLocation;
import com.l2jbr.gameserver.model.entity.database.repository.TeleportRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

public class TeleportLocationTable {
    private static Logger _log = LoggerFactory.getLogger(TeleportLocationTable.class.getName());

    private static TeleportLocationTable _instance;

    private Map<Integer, L2TeleportLocation> _teleports;

    public static TeleportLocationTable getInstance() {
        if (_instance == null) {
            _instance = new TeleportLocationTable();
        }
        return _instance;
    }

    private TeleportLocationTable() {
        reloadAll();
    }

    public void reloadAll() {
        _teleports = new LinkedHashMap<>();
        TeleportRepository repository = DatabaseAccess.getRepository(TeleportRepository.class);
        repository.findAll().forEach(teleportData -> {
            L2TeleportLocation teleport = new L2TeleportLocation();

            teleport.setTeleId(teleportData.getId());
            teleport.setLocX(teleportData.getLocX());
            teleport.setLocY(teleportData.getLocY());
            teleport.setLocZ(teleportData.getLocZ());
            teleport.setPrice(teleportData.getPrice());
            teleport.setIsForNoble(teleportData.getFornoble() == 1);

            _teleports.put(teleport.getTeleId(), teleport);
        });
        _log.info("TeleportLocationTable: Loaded {} Teleport Location Templates.", _teleports.size());
    }

    public L2TeleportLocation getTemplate(int id) {
        return _teleports.get(id);
    }
}
