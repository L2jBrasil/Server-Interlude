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
import com.l2jbr.gameserver.model.base.PlayerClass;
import com.l2jbr.gameserver.model.entity.database.Henna;
import com.l2jbr.gameserver.model.entity.database.repository.HennaTreeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static java.util.Objects.isNull;

public class HennaTreeTable {
    private static Logger _log = LoggerFactory.getLogger(HennaTreeTable.class.getName());
    private static HennaTreeTable INSTANCE;
    private Map<PlayerClass, List<Henna>> _hennaTrees;

    public static HennaTreeTable getInstance() {
        if(isNull(INSTANCE)) {
            INSTANCE = new HennaTreeTable();
        }
        return INSTANCE;
    }

    private HennaTreeTable() {
        _hennaTrees = new HashMap<>();

        var count = 0;
        for (var hennaTrees : DatabaseAccess.getRepository(HennaTreeRepository.class).findAll()) {
            var id = hennaTrees.getSymbolId();
            var classId = hennaTrees.getClassId();

            var template = HennaTable.getInstance().getTemplate(id);
            if (isNull(template)) {
                continue;
            }

            var playerClass = PlayerClass.values()[classId];
            if(! _hennaTrees.containsKey(playerClass)) {
                _hennaTrees.put(playerClass, new ArrayList<>());
            }
            _hennaTrees.get(playerClass).add(template);
            count++;
        }
        _log.info("HennaTreeTable: Loaded {} Henna Tree Templates.", count);
    }

    public List<Henna> getAvailableHenna(PlayerClass playerClass) {
        var hennas = _hennaTrees.get(playerClass);
        if (isNull(hennas)) {
            _log.warn("Hennatree for class  {} is not defined !",  playerClass);
            return Collections.emptyList();
        }
        return hennas;
    }

}
