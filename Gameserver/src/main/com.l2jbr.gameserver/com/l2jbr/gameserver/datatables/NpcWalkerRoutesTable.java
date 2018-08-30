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
import com.l2jbr.gameserver.model.entity.database.WalkerRouteNode;
import com.l2jbr.gameserver.model.entity.database.repository.WalkerRoutesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main Table to Load NpcTemplate Walkers Routes and Chat SQL Table.<br>
 *
 * @author Rayan RPG for L2Emu Project
 * @since 927
 */
public class NpcWalkerRoutesTable {
    private final static Logger _log = LoggerFactory.getLogger(SpawnTable.class.getName());

    private static NpcWalkerRoutesTable _instance;

    private Map<Integer, List<WalkerRouteNode>> routes;

    public static NpcWalkerRoutesTable getInstance() {
        if (_instance == null) {
            _instance = new NpcWalkerRoutesTable();
        }
        return _instance;
    }

    private NpcWalkerRoutesTable() {
        load();
    }

    public void load() {
        _log.info("Initializing Walkers Routes Table.");
        routes = new HashMap<>();
        DatabaseAccess.getRepository(WalkerRoutesRepository.class).findAll().forEach(node ->{
            if(!routes.containsKey(node.getNpcId())) {
                routes.put(node.getNpcId(), new ArrayList<>());
            }
            routes.get(node.getNpcId()).add(node);
        });

        _log.info("WalkerRoutesTable: Loaded {} NpcTemplate Walker Routes.", routes.size());
    }

    public List<WalkerRouteNode> getRouteForNpc(int npcId) {
        return routes.get(npcId);
    }
}
