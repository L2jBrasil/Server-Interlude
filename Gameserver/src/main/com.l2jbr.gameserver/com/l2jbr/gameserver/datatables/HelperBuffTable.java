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
import com.l2jbr.gameserver.model.entity.database.HelperBuffList;
import com.l2jbr.gameserver.model.entity.database.repository.HelperBuffRepository;
import com.l2jbr.gameserver.templates.L2HelperBuff;
import com.l2jbr.gameserver.templates.StatsSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;


/**
 * This class represents the Newbie Helper Buff list.
 *
 * @author Ayor
 */
public class HelperBuffTable {

    private static Logger _log = LoggerFactory.getLogger(HennaTable.class.getName());

    private static HelperBuffTable _instance;

    /**
     * The table containing all Buff of the Newbie Helper
     */
    private final List<L2HelperBuff> _helperBuff;

    private final boolean _initialized = true;

    /**
     * The player level since Newbie Helper can give the fisrt buff <BR>
     * Used to generate message : "Come back here when you have reached level ...")
     */
    private int _magicClassLowestLevel = 100;
    private int _physicClassLowestLevel = 100;

    /**
     * The player level above which Newbie Helper won't give any buff <BR>
     * Used to generate message : "Only novice character of level ... or less can receive my support magic.")
     */
    private int _magicClassHighestLevel = 1;
    private int _physicClassHighestLevel = 1;

    public static HelperBuffTable getInstance() {
        if (_instance == null) {
            _instance = new HelperBuffTable();
        }
        return _instance;
    }

    /**
     * Create and Load the Newbie Helper Buff list from SQL Table helper_buff_list
     */
    private HelperBuffTable() {
        _helperBuff = new LinkedList<>();
        restoreHelperBuffData();
    }

    private void restoreHelperBuffData() {
        HelperBuffRepository repository = DatabaseAccess.getRepository(HelperBuffRepository.class);
        repository.findAll().forEach(helperBuffList -> {
            fillHelperBuffTable(helperBuffList);
        });
    }

    private void fillHelperBuffTable(HelperBuffList helperBuffList) {
        StatsSet helperBuffDat = new StatsSet();
        int id = helperBuffList.getId();

        helperBuffDat.set("id", id);
        helperBuffDat.set("skillID", helperBuffList.getSkillId());
        helperBuffDat.set("skillLevel", helperBuffList.getSkillLevel());
        helperBuffDat.set("lowerLevel", helperBuffList.getLowerLevel());
        helperBuffDat.set("upperLevel", helperBuffList.getUpperLevel());
        helperBuffDat.set("isMagicClass", helperBuffList.getIsMagicClass());

        // Calulate the range level in wich player must be to obtain buff from Newbie Helper
        if ("false".equals(helperBuffList.getIsMagicClass())) {
            if (helperBuffList.getLowerLevel() < _physicClassLowestLevel) {
                _physicClassLowestLevel = helperBuffList.getLowerLevel();
            }

            if (helperBuffList.getUpperLevel() > _physicClassHighestLevel) {
                _physicClassHighestLevel = helperBuffList.getUpperLevel();
            }
        } else {
            if (helperBuffList.getLowerLevel() < _magicClassLowestLevel) {
                _magicClassLowestLevel = helperBuffList.getLowerLevel();
            }

            if (helperBuffList.getUpperLevel() > _magicClassHighestLevel) {
                _magicClassHighestLevel = helperBuffList.getUpperLevel();
            }
        }

        L2HelperBuff template = new L2HelperBuff(helperBuffDat);
        _helperBuff.add(template);
    }

    public boolean isInitialized() {
        return _initialized;
    }

    public L2HelperBuff getHelperBuffTableItem(int id) {
        return _helperBuff.get(id);
    }

    /**
     * @return the Helper Buff List.
     */
    public List<L2HelperBuff> getHelperBuffTable() {
        return _helperBuff;
    }

    /**
     * @return Returns the magicClassHighestLevel.
     */
    public int getMagicClassHighestLevel() {
        return _magicClassHighestLevel;
    }

    /**
     * @param magicClassHighestLevel The magicClassHighestLevel to set.
     */
    public void setMagicClassHighestLevel(int magicClassHighestLevel) {
        _magicClassHighestLevel = magicClassHighestLevel;
    }

    /**
     * @return Returns the magicClassLowestLevel.
     */
    public int getMagicClassLowestLevel() {
        return _magicClassLowestLevel;
    }

    /**
     * @param magicClassLowestLevel The magicClassLowestLevel to set.
     */
    public void setMagicClassLowestLevel(int magicClassLowestLevel) {
        _magicClassLowestLevel = magicClassLowestLevel;
    }

    /**
     * @return Returns the physicClassHighestLevel.
     */
    public int getPhysicClassHighestLevel() {
        return _physicClassHighestLevel;
    }

    /**
     * @param physicClassHighestLevel The physicClassHighestLevel to set.
     */
    public void setPhysicClassHighestLevel(int physicClassHighestLevel) {
        _physicClassHighestLevel = physicClassHighestLevel;
    }

    /**
     * @return Returns the physicClassLowestLevel.
     */
    public int getPhysicClassLowestLevel() {
        return _physicClassLowestLevel;
    }

    /**
     * @param physicClassLowestLevel The physicClassLowestLevel to set.
     */
    public void setPhysicClassLowestLevel(int physicClassLowestLevel) {
        _physicClassLowestLevel = physicClassLowestLevel;
    }

}
