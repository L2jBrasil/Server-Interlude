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

import com.l2jbr.gameserver.model.entity.database.Teleport;
import com.l2jbr.gameserver.model.entity.database.repository.TeleportRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static com.l2jbr.commons.database.DatabaseAccess.getRepository;
import static java.util.Objects.isNull;

public class TeleportLocationTable {
    private static Logger _log = LoggerFactory.getLogger(TeleportLocationTable.class);

    private static TeleportLocationTable _instance;

    private Map<Integer, Teleport> _teleports;

    public static TeleportLocationTable getInstance() {
        if (isNull(_instance )) {
            _instance = new TeleportLocationTable();
        }
        return _instance;
    }

    private TeleportLocationTable() {
        reloadAll();
    }

    public void reloadAll() {
        _teleports = new HashMap<>();
        getRepository(TeleportRepository.class).findAll().forEach(teleport -> _teleports.put(teleport.getId(), teleport));
        _log.info("TeleportLocationTable: Loaded {} Teleport Location Templates.", _teleports.size());
    }

    public Teleport getTemplate(int id) {
        return _teleports.get(id);
    }
}
