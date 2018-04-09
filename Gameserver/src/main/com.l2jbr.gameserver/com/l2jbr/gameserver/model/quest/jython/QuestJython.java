/* This program is free software; you can redistribute it and/or modify
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
package com.l2jbr.gameserver.model.quest.jython;

import com.l2jbr.commons.Config;
import com.l2jbr.gameserver.model.quest.Quest;
import com.l2jbr.gameserver.script.ScriptingManager;

import javax.script.ScriptEngine;
import javax.script.ScriptException;


public abstract class QuestJython extends Quest {

    public static void init() {
        try {
            String dataPackDirForwardSlashes = Config.DATAPACK_ROOT.getPath().replaceAll("\\\\", "/");
            String loadingScript = "import sys;" + "sys.path.insert(0,'" + dataPackDirForwardSlashes + "');" + "import data";

            ScriptEngine engine = ScriptingManager.getInstance().getEngine("python");
            engine.eval(loadingScript);

        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    public static boolean reloadQuest(String questFolder) {
        try {
            ScriptEngine engine = ScriptingManager.getInstance().getEngine("python");
            engine.eval("reload(data.jscript." + questFolder + ");");
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * Constructor used in jython files.
     *
     * @param questId : int designating the ID of the quest
     * @param name    : String designating the name of the quest
     * @param descr   : String designating the description of the quest
     */
    public QuestJython(int questId, String name, String descr) {
        super(questId, name, descr);
    }
}