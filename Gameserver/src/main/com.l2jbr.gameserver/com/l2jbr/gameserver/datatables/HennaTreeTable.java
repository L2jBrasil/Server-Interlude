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

import com.l2jbr.commons.database.L2DatabaseFactory;
import com.l2jbr.gameserver.model.L2HennaInstance;
import com.l2jbr.gameserver.model.base.ClassId;
import com.l2jbr.gameserver.templates.L2Henna;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * This class ...
 *
 * @version $Revision$ $Date$
 */
public class HennaTreeTable {
    private static Logger _log = LoggerFactory.getLogger(HennaTreeTable.class.getName());
    private static final HennaTreeTable _instance = new HennaTreeTable();
    private Map<ClassId, List<L2HennaInstance>> _hennaTrees;
    private final boolean _initialized = true;

    public static HennaTreeTable getInstance() {
        return _instance;
    }

    private HennaTreeTable() {
        _hennaTrees = new LinkedHashMap<>();

        int count = 0;
        try(Connection con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement2 = con.prepareStatement("SELECT class_id, symbol_id FROM henna_trees ORDER BY symbol_id");
            ResultSet hennatree = statement2.executeQuery()){
            List<L2HennaInstance> list;
            while (hennatree.next()) {
                int id = hennatree.getInt("symbol_id");
                int classId = hennatree.getInt("class_id");

                L2Henna template = HennaTable.getInstance().getTemplate(id);
                if (template == null) {
                    continue;
                }
                list = _hennaTrees.getOrDefault(ClassId.values()[classId], new LinkedList<>());
                list.add(new L2HennaInstance(template));
                count++;
            }
        }catch (Exception e){
           _log.error("error while creating henna tree",  e);
        }

        _log.info("HennaTreeTable: Loaded " + count + " Henna Tree Templates.");

    }

    public L2HennaInstance[] getAvailableHenna(ClassId classId) {
        List<L2HennaInstance> result = new LinkedList<>();
        List<L2HennaInstance> henna = _hennaTrees.get(classId);
        if (henna == null) {
            // the hennatree for this class is undefined, so we give an empty list
            _log.warn("Hennatree for class " + classId + " is not defined !");
            return new L2HennaInstance[0];
        }

        for (int i = 0; i < henna.size(); i++) {
            L2HennaInstance temp = henna.get(i);
            result.add(temp);
        }

        return result.toArray(new L2HennaInstance[result.size()]);
    }

    public boolean isInitialized() {
        return _initialized;
    }

}
