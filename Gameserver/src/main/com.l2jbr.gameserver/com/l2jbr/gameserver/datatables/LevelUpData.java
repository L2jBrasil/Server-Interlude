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
import com.l2jbr.gameserver.model.L2LvlupData;
import com.l2jbr.gameserver.model.base.ClassId;
import com.l2jbr.gameserver.model.database.repository.CharTemplateRepository;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * This class ...
 *
 * @author NightMarez
 * @version $Revision: 1.3.2.4.2.3 $ $Date: 2005/03/27 15:29:18 $
 */
public class LevelUpData {

    private static LevelUpData _instance;

    private final Map<Integer, L2LvlupData> _lvlTable;

    public static LevelUpData getInstance() {
        if (_instance == null) {
            _instance = new LevelUpData();
        }
        return _instance;
    }

    private LevelUpData() {
        _lvlTable = new LinkedHashMap<>();
        CharTemplateRepository repository = DatabaseAccess.getRepository(CharTemplateRepository.class);
        repository.findAll().forEach(charTemplate -> {
            L2LvlupData lvlDat = new L2LvlupData();
            lvlDat.setClassid(charTemplate.getId());
            lvlDat.setClassLvl(charTemplate.getClassLevel());
            lvlDat.setClassHpBase(charTemplate.getDefaultHpBase());
            lvlDat.setClassHpAdd(charTemplate.getDefaultHpAdd());
            lvlDat.setClassHpModifier(charTemplate.getDefaultHpMod());
            lvlDat.setClassCpBase(charTemplate.getDefaultCpBase());
            lvlDat.setClassCpAdd(charTemplate.getDefaultCpAdd());
            lvlDat.setClassCpModifier(charTemplate.getDefaultCpMod());
            lvlDat.setClassMpBase(charTemplate.getDefaultMpBase());
            lvlDat.setClassMpAdd(charTemplate.getDefaultMpAdd());
            lvlDat.setClassMpModifier(charTemplate.getDefaultMpMod());

            _lvlTable.put(lvlDat.getClassid(), lvlDat);
        });
    }

    /**
     * @param classId
     * @return
     */
    public L2LvlupData getTemplate(int classId) {
        return _lvlTable.get(classId);
    }

    public L2LvlupData getTemplate(ClassId classId) {
        return _lvlTable.get(classId.getId());
    }
}
