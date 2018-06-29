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
/*
 coded by Balancer
 ported to L2JRU by Mr
 balancer@balancer.ru
 http://balancer.ru

 version 0.1.1, 2005-06-07
 version 0.1, 2005-03-16
 */

package com.l2jbr.gameserver;

import com.l2jbr.commons.database.DatabaseAccess;
import com.l2jbr.gameserver.model.L2Territory;
import com.l2jbr.gameserver.model.entity.database.repository.LocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;


public class Territory {
    private static Logger _log = LoggerFactory.getLogger(TradeController.class.getName());
    private static final Territory _instance = new Territory();
    private static final Map<Integer, L2Territory> _territory = new LinkedHashMap<>();

    public static Territory getInstance() {
        return _instance;
    }

    private Territory() {
        reload_data();
    }

    public int[] getRandomPoint(int terr) {
        return _territory.get(terr).getRandomPoint();
    }

    public int getProcMax(int terr) {
        return _territory.get(terr).getProcMax();
    }

    public void reload_data() {
        _territory.clear();
        LocationRepository repository = DatabaseAccess.getRepository(LocationRepository.class);
        repository.findAll().forEach(location -> {
            if(_territory.get(location.getLocId()) == null) {
                L2Territory territory = new L2Territory(location.getLocId());
                _territory.put(location.getLocId(), territory);
            }
            _territory.get(location.getLocId()).add(location);
        });
    }
}
