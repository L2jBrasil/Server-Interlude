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
import com.l2jbr.gameserver.model.entity.database.NpcTemplate;
import com.l2jbr.gameserver.model.entity.database.repository.NpcRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static com.l2jbr.gameserver.templates.NpcType.L2Monster;
import static com.l2jbr.gameserver.templates.NpcType.L2Npc;
import static com.l2jbr.gameserver.util.GameserverMessages.getMessage;
import static java.util.Objects.isNull;

public class NpcTable {
    private static Logger _log = LoggerFactory.getLogger(NpcTable.class);

    private static NpcTable INSTANCE;

    private final Map<Integer, NpcTemplate> npcs;

    public static NpcTable getInstance() {
        if (isNull(INSTANCE)) {
            INSTANCE = new NpcTable();
        }
        return INSTANCE;
    }

    private NpcTable() {
        npcs = new LinkedHashMap<>();
        restoreNpcData();
    }

    private void restoreNpcData() {
        NpcRepository npcRepository = DatabaseAccess.getRepository(NpcRepository.class);
        npcRepository.findAll().forEach(this::addToNpcMap);
        _log.info(getMessage("info.npc.loaded"), npcs.size());
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
        return list.toArray(new NpcTemplate[0]);
    }

    public List<NpcTemplate> getAllMonstersOfLevel(int level) {
        return npcs.values().stream().filter(t -> t.getLevel() == level && L2Monster.equals(t.getType())).collect(Collectors.toList());
    }

    public List<NpcTemplate> getAllNpcStartingWith(String letter) {
        return npcs.values().stream().filter(n -> n.getName().startsWith(letter) && L2Npc.equals(n.getType())).collect(Collectors.toList());
    }

    public Set<Integer> getAllNpcOfClassType(String classType) {
        // TODO Auto-generated method stub
        return null;
    }

    public Set<Integer> getAllNpcOfL2jClass(Class<?> clazz) {
        // TODO Auto-generated method stub
        return null;
    }

    public Set<Integer> getAllNpcOfAiType(String aiType) {
        // TODO Auto-generated method stub
        return null;
    }

}