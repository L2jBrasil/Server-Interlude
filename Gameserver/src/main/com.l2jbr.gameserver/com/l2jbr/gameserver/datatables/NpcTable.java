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
import com.l2jbr.commons.database.L2DatabaseFactory;
import com.l2jbr.gameserver.model.database.NpcTemplate;
import com.l2jbr.gameserver.model.database.repository.NpcRepository;
import com.l2jbr.gameserver.templates.StatsSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.*;

import static com.l2jbr.gameserver.util.GameserverMessages.getMessage;

public class NpcTable {
    private static Logger _log = LoggerFactory.getLogger(NpcTable.class);

    private static NpcTable _instance;

    private final Map<Integer, NpcTemplate> npcs;
    private boolean _initialized = false;

    public static NpcTable getInstance() {
        if (_instance == null) {
            _instance = new NpcTable();
        }
        return _instance;
    }

    private NpcTable() {
        npcs = new LinkedHashMap<>();
        restoreNpcData();
    }

    private void restoreNpcData() {
        NpcRepository npcRepository = DatabaseAccess.getRepository(NpcRepository.class);
        npcRepository.findAll().forEach(this::addToNpcMap);
        _log.info(getMessage("info.npc.loaded"), npcs.size());
        _initialized = true;
    }

    public void reloadNpc(int id) {
        NpcRepository repository = DatabaseAccess.getRepository(NpcRepository.class);
        repository.findById(id).ifPresent(this::addToNpcMap);
    }

    private void addToNpcMap(NpcTemplate npc) {
        npcs.put(npc.getId(), npc);
    }

    public void reloadAllNpc() {
        restoreNpcData();
    }

    public void saveNpc(StatsSet npc) {
        Connection con = null;
        String query = "";

        try {
            con = L2DatabaseFactory.getInstance().getConnection();
            Map<String, Object> set = npc.getSet();

            String name = "";
            String values = "";

            for (Object obj : set.keySet()) {
                name = (String) obj;

                if (!name.equalsIgnoreCase("npcId")) {
                    if (values != "") {
                        values += ", ";
                    }

                    values += name + " = '" + set.get(name) + "'";
                }
            }

            //TODO use repositories instead
            query = "UPDATE npc SET " + values + " WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, npc.getInteger("npcId"));
            statement.execute();
            statement.close();
        } catch (Exception e) {
            _log.warn("NPCTable: Could not store new NPC data in database: " + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
            }
        }
    }

    public boolean isInitialized() {
        return _initialized;
    }

    public void replaceTemplate(NpcTemplate npc) {
        npcs.put(npc.getId(), npc);
    }

    public NpcTemplate getTemplate(int id) {
        return npcs.get(id);
    }

    public NpcTemplate getTemplateByName(String name) {
        for (NpcTemplate npcTemplate : npcs.values()) {
            if (npcTemplate.getName().equalsIgnoreCase(name)) {
                return npcTemplate;
            }
        }
        return null;
    }

    public NpcTemplate[] getAllOfLevel(int lvl) {
        List<NpcTemplate> list = new LinkedList<>();

        for (NpcTemplate t : npcs.values()) {
            if (t.getLevel() == lvl) {
                list.add(t);
            }
        }
        return list.toArray(new NpcTemplate[list.size()]);
    }

    public NpcTemplate[] getAllMonstersOfLevel(int lvl) {
        List<NpcTemplate> list = new LinkedList<>();

        for (NpcTemplate t : npcs.values()) {
            if ((t.getLevel() == lvl) && "L2Monster".equals(t.getType())) {
                list.add(t);
            }
        }
        return list.toArray(new NpcTemplate[list.size()]);
    }

    public NpcTemplate[] getAllNpcStartingWith(String letter) {
        List<NpcTemplate> list = new LinkedList<>();

        for (NpcTemplate t : npcs.values()) {
            if (t.getName().startsWith(letter) && "L2Npc".equals(t.getType())) {
                list.add(t);
            }
        }

        return list.toArray(new NpcTemplate[list.size()]);
    }

    /**
     * @param classType
     * @return
     */
    public Set<Integer> getAllNpcOfClassType(String classType) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param clazz
     * @return
     */
    public Set<Integer> getAllNpcOfL2jClass(Class<?> clazz) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param aiType
     * @return
     */
    public Set<Integer> getAllNpcOfAiType(String aiType) {
        // TODO Auto-generated method stub
        return null;
    }

}