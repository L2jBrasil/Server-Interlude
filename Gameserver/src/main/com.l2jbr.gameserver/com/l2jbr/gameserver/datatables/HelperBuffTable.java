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
import com.l2jbr.gameserver.model.entity.database.HelperBuff;
import com.l2jbr.gameserver.model.entity.database.repository.HelperBuffRepository;

import java.util.List;

import static java.util.Objects.isNull;

/**
 * This class represents the Newbie Helper Buff list.
 *
 * @author Ayor
 */
public class HelperBuffTable {

    private static HelperBuffTable _instance;

    private final List<HelperBuff> helperBuffs;

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
        if (isNull(_instance)) {
            _instance = new HelperBuffTable();
        }
        return _instance;
    }

    private HelperBuffTable() {
        helperBuffs = DatabaseAccess.getRepository(HelperBuffRepository.class).findAll();
        processTable();
    }

    private void processTable() {
        helperBuffs.forEach(helperBuff -> {
            if(helperBuff.isMagicClass()) {
                if (helperBuff.getLowerLevel() < _magicClassLowestLevel) {
                    _magicClassLowestLevel = helperBuff.getLowerLevel();
                }

                if (helperBuff.getUpperLevel() > _magicClassHighestLevel) {
                    _magicClassHighestLevel = helperBuff.getUpperLevel();
                }
            } else {
                if (helperBuff.getLowerLevel() < _physicClassLowestLevel) {
                    _physicClassLowestLevel = helperBuff.getLowerLevel();
                }

                if (helperBuff.getUpperLevel() > _physicClassHighestLevel) {
                    _physicClassHighestLevel = helperBuff.getUpperLevel();
                }
            }
        });
    }

    public List<HelperBuff> getHelperBuffTable() { return helperBuffs; }

    public int getMagicClassHighestLevel() {
        return _magicClassHighestLevel;
    }

    public int getMagicClassLowestLevel() {
        return _magicClassLowestLevel;
    }

    public int getPhysicClassHighestLevel() {
        return _physicClassHighestLevel;
    }

    public int getPhysicClassLowestLevel() {
        return _physicClassLowestLevel;
    }
}
