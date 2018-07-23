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
package com.l2jbr.gameserver.skills;

import com.l2jbr.commons.Config;
import com.l2jbr.gameserver.datatables.SkillTable;
import com.l2jbr.gameserver.model.L2Skill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SkillsEngine {

    protected static final Logger _log = LoggerFactory.getLogger(SkillsEngine.class.getName());

    private static final SkillsEngine _instance = new SkillsEngine();

    private final List<File> _skillFiles = new LinkedList<>();

    public static SkillsEngine getInstance() {
        return _instance;
    }

    private SkillsEngine() {
        hashFiles("data/stats/skills", _skillFiles);
    }

    private void hashFiles(String dirname, List<File> hash) {
        File dir = new File(Config.DATAPACK_ROOT, dirname);
        if (!dir.exists()) {
            _log.info("Dir " + dir.getAbsolutePath() + " not exists");
            return;
        }
        File[] files = dir.listFiles();
        for (File f : files) {
            if (f.getName().endsWith(".xml")) {
                if (!f.getName().startsWith("custom")) {
                    hash.add(f);
                }
            }
        }
        File customfile = new File(Config.DATAPACK_ROOT, dirname + "/custom.xml");
        if (customfile.exists()) {
            hash.add(customfile);
        }
    }

    public List<L2Skill> loadSkills(File file) {
        if (file == null) {
            _log.info("Skill file not found.");
            return null;
        }
        DocumentSkill doc = new DocumentSkill(file);
        doc.parse();
        return doc.getSkills();
    }

    public void loadAllSkills(Map<Integer, L2Skill> allSkills) {
        int count = 0;
        for (File file : _skillFiles) {
            List<L2Skill> s = loadSkills(file);
            if (s == null) {
                continue;
            }
            for (L2Skill skill : s) {
                allSkills.put(SkillTable.getSkillHashCode(skill), skill);
                count++;
            }
        }
        _log.info("SkillsEngine: Loaded " + count + " Skill templates from XML files.");
    }
}
