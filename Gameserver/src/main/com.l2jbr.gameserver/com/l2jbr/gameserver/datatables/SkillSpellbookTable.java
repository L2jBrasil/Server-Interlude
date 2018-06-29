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
import com.l2jbr.gameserver.model.L2Skill;
import com.l2jbr.gameserver.model.entity.database.repository.SkillSpeelBooksRepository;

import java.util.LinkedHashMap;
import java.util.Map;

public class SkillSpellbookTable {
    private static SkillSpellbookTable _instance;

    private static Map<Integer, Integer> _skillSpellbooks;

    public static SkillSpellbookTable getInstance() {
        if (_instance == null) {
            _instance = new SkillSpellbookTable();
        }
        return _instance;
    }

    private SkillSpellbookTable() {
        _skillSpellbooks = new LinkedHashMap<>();
        java.sql.Connection con = null;
        SkillSpeelBooksRepository repository = DatabaseAccess.getRepository(SkillSpeelBooksRepository.class);
        repository.findAll().forEach(skillSpellBooks -> {
            _skillSpellbooks.put(skillSpellBooks.getSkillId(), skillSpellBooks.getItemId());
        });
    }

    public int getBookForSkill(int skillId) {
        if (!_skillSpellbooks.containsKey(skillId)) {
            return -1;
        }
        return _skillSpellbooks.get(skillId);
    }

    public int getBookForSkill(L2Skill skill) {
        return getBookForSkill(skill.getId());
    }
}
