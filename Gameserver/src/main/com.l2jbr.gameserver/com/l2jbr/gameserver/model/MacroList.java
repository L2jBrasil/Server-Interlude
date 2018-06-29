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
package com.l2jbr.gameserver.model;

import com.l2jbr.commons.database.DatabaseAccess;
import com.l2jbr.commons.database.L2DatabaseFactory;
import com.l2jbr.gameserver.model.L2Macro.L2MacroCmd;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.model.entity.database.CharacterMacroses;
import com.l2jbr.gameserver.model.entity.database.repository.CharacterMacrosesRepository;
import com.l2jbr.gameserver.serverpackets.SendMacroList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;


/**
 * This class ...
 *
 * @version $Revision: 1.1.2.1.2.2 $ $Date: 2005/03/02 15:38:41 $
 */
public class MacroList {
    private static Logger _log = LoggerFactory.getLogger(MacroList.class.getName());

    private final L2PcInstance _owner;
    private int _revision;
    private int _macroId;
    private final Map<Integer, L2Macro> _macroses = new LinkedHashMap<>();

    public MacroList(L2PcInstance owner) {
        _owner = owner;
        _revision = 1;
        _macroId = 1000;
    }

    public int getRevision() {
        return _revision;
    }

    public L2Macro[] getAllMacroses() {
        return _macroses.values().toArray(new L2Macro[_macroses.size()]);
    }

    public L2Macro getMacro(int id) {
        return _macroses.get(id - 1);
    }

    public void registerMacro(L2Macro macro) {
        if (macro.id == 0) {
            macro.id = _macroId++;
            while (_macroses.get(macro.id) != null) {
                macro.id = _macroId++;
            }
            _macroses.put(macro.id, macro);
            registerMacroInDb(macro);
        } else {
            L2Macro old = _macroses.put(macro.id, macro);
            if (old != null) {
                deleteMacroFromDb(old);
            }
            registerMacroInDb(macro);
        }
        sendUpdate();
    }

    public void deleteMacro(int id) {
        L2Macro toRemove = _macroses.get(id);
        if (toRemove != null) {
            deleteMacroFromDb(toRemove);
        }
        _macroses.remove(id);

        L2ShortCut[] allShortCuts = _owner.getAllShortCuts();
        for (L2ShortCut sc : allShortCuts) {
            if ((sc.getId() == id) && (sc.getType() == L2ShortCut.TYPE_MACRO)) {
                _owner.deleteShortCut(sc.getSlot(), sc.getPage());
            }
        }

        sendUpdate();
    }

    public void sendUpdate() {
        _revision++;
        L2Macro[] all = getAllMacroses();
        if (all.length == 0) {
            _owner.sendPacket(new SendMacroList(_revision, all.length, null));
        } else {
            for (L2Macro m : all) {
                _owner.sendPacket(new SendMacroList(_revision, all.length, m));
            }
        }
    }

    private void registerMacroInDb(L2Macro macro) {
        CharacterMacroses characterMacro = new CharacterMacroses(_owner.getObjectId(), macro);
        CharacterMacrosesRepository repository = DatabaseAccess.getRepository(CharacterMacrosesRepository.class);
        repository.save(characterMacro);
    }

    private void deleteMacroFromDb(L2Macro macro) {
        CharacterMacrosesRepository repository = DatabaseAccess.getRepository(CharacterMacrosesRepository.class);
        repository.deleteByMacroId(_owner.getObjectId(), macro.id);
    }

    public void restore() {
        _macroses.clear();

        CharacterMacrosesRepository repository = DatabaseAccess.getRepository(CharacterMacrosesRepository.class);
        repository.findAllByCharacter(_owner.getObjectId()).forEach(macro -> {
            int id = macro.getId();
            int icon = macro.getIcon();
            String name = macro.getName();
            String descr = macro.getDescr();
            String acronym = macro.getAcronym();
            List<L2MacroCmd> commands = new LinkedList<>();
            StringTokenizer st1 = new StringTokenizer(macro.getCommands(), ";");
            while (st1.hasMoreTokens()) {
                StringTokenizer st = new StringTokenizer(st1.nextToken(), ",");
                if (st.countTokens() < 3) {
                    continue;
                }
                int type = Integer.parseInt(st.nextToken());
                int d1 = Integer.parseInt(st.nextToken());
                int d2 = Integer.parseInt(st.nextToken());
                String cmd = "";
                if (st.hasMoreTokens()) {
                    cmd = st.nextToken();
                }
                L2MacroCmd mcmd = new L2MacroCmd(commands.size(), type, d1, d2, cmd);
                commands.add(mcmd);
            }

            L2Macro m = new L2Macro(id, icon, name, descr, acronym, commands.toArray(new L2MacroCmd[commands.size()]));
            _macroses.put(m.id, m);
        });
    }
}
