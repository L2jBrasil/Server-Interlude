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
import com.l2jbr.gameserver.model.entity.database.Henna;
import com.l2jbr.gameserver.model.entity.database.repository.HennaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static com.l2jbr.gameserver.util.GameserverMessages.getMessage;
import static java.util.Objects.isNull;

public class HennaTable {
    private static Logger _log = LoggerFactory.getLogger(HennaTable.class.getName());

    private static HennaTable _instance;

    private final Map<Integer, Henna> hennas;

    public static HennaTable getInstance() {
        if (isNull(_instance)) {
            _instance = new HennaTable();
        }
        return _instance;
    }

    private HennaTable() {
        hennas = new HashMap<>();
        restoreHennaData();
    }

    private void restoreHennaData() {
        DatabaseAccess.getRepository(HennaRepository.class).findAll().forEach(this::fillHennaTable);
        _log.info(getMessage("info.henna.loaded"),  hennas.size());
    }

    private void fillHennaTable(Henna henna) {
        hennas.put(henna.getSymbolId(), henna);
    }

    public Henna getTemplate(int id) {
        return hennas.get(id);
    }

}
