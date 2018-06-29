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
import com.l2jbr.gameserver.templates.L2Henna;
import com.l2jbr.gameserver.templates.StatsSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.l2jbr.gameserver.util.GameserverMessages.getMessage;

public class HennaTable {
    private static Logger _log = LoggerFactory.getLogger(HennaTable.class.getName());

    private static HennaTable _instance;

    private final Map<Integer, L2Henna> _henna;
    private final boolean _initialized = true;

    public static HennaTable getInstance() {
        if (_instance == null) {
            _instance = new HennaTable();
        }
        return _instance;
    }

    private HennaTable() {
        _henna = new LinkedHashMap<>();
        restoreHennaData();
    }

    private void restoreHennaData() {
        HennaRepository repository = DatabaseAccess.getRepository(HennaRepository.class);
        repository.findAll().forEach(this::fillHennaTable);
        _log.info(getMessage("info.henna.loaded"),  _henna.size());
    }

    private void fillHennaTable(Henna henna) {
            StatsSet hennaDat = new StatsSet();
            int id = henna.getSymbolId();

            hennaDat.set("symbol_id", id);
            hennaDat.set("dye", henna.getDyeId());
            hennaDat.set("price", henna.getPrice());
            hennaDat.set("amount", henna.getDyeAmount());
            hennaDat.set("stat_INT", henna.getStat_INT());
            hennaDat.set("stat_STR", henna.getStat_STR());
            hennaDat.set("stat_CON", henna.getStat_CON());
            hennaDat.set("stat_MEM", henna.getStat_MEM());
            hennaDat.set("stat_DEX", henna.getStat_DEX());
            hennaDat.set("stat_WIT", henna.getStat_WIT());

            L2Henna template = new L2Henna(hennaDat);
            _henna.put(id, template);
    }

    public boolean isInitialized() {
        return _initialized;
    }

    public L2Henna getTemplate(int id) {
        return _henna.get(id);
    }

}
