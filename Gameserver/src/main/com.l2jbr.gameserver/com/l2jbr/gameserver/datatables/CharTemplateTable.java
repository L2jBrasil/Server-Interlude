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
import com.l2jbr.gameserver.model.entity.database.PlayerTemplate;
import com.l2jbr.gameserver.model.entity.database.repository.CharTemplateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class CharTemplateTable {
    private static Logger _log = LoggerFactory.getLogger(CharTemplateTable.class);
    private static CharTemplateTable INSTANCE;

    private final Map<Integer, PlayerTemplate> templates;

    public static CharTemplateTable getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CharTemplateTable();
        }
        return INSTANCE;
    }

    private CharTemplateTable() {
        templates = new LinkedHashMap<>();
        CharTemplateRepository repository = DatabaseAccess.getRepository(CharTemplateRepository.class);
        repository.findAll().forEach(this::addToTemplates);

        _log.info("CharTemplateTable: Loaded {} Character Templates.", templates.size());
    }

    private void addToTemplates(PlayerTemplate playerTemplate) {
        templates.put(playerTemplate.getId(), playerTemplate);
    }

    public PlayerTemplate getTemplate(PlayerClass playerClass) {
        return getTemplate(playerClass.getId());
    }

    public PlayerTemplate getTemplate(int classId) {
        return templates.get(classId);
    }

    public static String getClassNameById(int classId) {
        PlayerTemplate template = getInstance().getTemplate(classId);
        return template.getPlayerClass().humanize();
    }

    public Collection<PlayerTemplate> all() {
        return templates.values();
    }
}
