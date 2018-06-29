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
import com.l2jbr.gameserver.model.L2NpcWalkerNode;
import com.l2jbr.gameserver.model.entity.database.repository.WalkerRoutesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;


/**
 * Main Table to Load NpcTemplate Walkers Routes and Chat SQL Table.<br>
 *
 * @author Rayan RPG for L2Emu Project
 * @since 927
 */
public class NpcWalkerRoutesTable {
    private final static Logger _log = LoggerFactory.getLogger(SpawnTable.class.getName());

    private static NpcWalkerRoutesTable _instance;

    private List<L2NpcWalkerNode> _routes;

    public static NpcWalkerRoutesTable getInstance() {
        if (_instance == null) {
            _instance = new NpcWalkerRoutesTable();
            _log.info("Initializing Walkers Routes Table.");
        }
        return _instance;
    }

    private NpcWalkerRoutesTable() {
    }

    public void load() {
        _routes = new LinkedList<>();
        WalkerRoutesRepository repository = DatabaseAccess.getRepository(WalkerRoutesRepository.class);
        repository.findAll().forEach(walkerRoutes -> {
            L2NpcWalkerNode route = new L2NpcWalkerNode(walkerRoutes);
            _routes.add(route);
        });

        _log.info("WalkerRoutesTable: Loaded {} NpcTemplate Walker Routes.", _routes.size());
    }

    public List<L2NpcWalkerNode> getRouteForNpc(int id) {
        List<L2NpcWalkerNode> _return = new LinkedList<>();

        for (L2NpcWalkerNode n : _routes) {
            if (n.getNpcId() == id) {
                _return.add(n);
            }
        }
        return _return;

    }
}
