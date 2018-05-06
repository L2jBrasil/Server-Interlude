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
import com.l2jbr.gameserver.model.L2EnchantSkillLearn;
import com.l2jbr.gameserver.model.L2PledgeSkillLearn;
import com.l2jbr.gameserver.model.L2Skill;
import com.l2jbr.gameserver.model.L2SkillLearn;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.model.base.ClassId;
import com.l2jbr.gameserver.model.database.CharTemplate;
import com.l2jbr.gameserver.model.database.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * This class ...
 *
 * @version $Revision: 1.13.2.2.2.8 $ $Date: 2005/04/06 16:13:25 $
 */
public class SkillTreeTable {
    private static Logger _log = LoggerFactory.getLogger(SkillTreeTable.class.getName());
    private static SkillTreeTable _instance;

    private Map<ClassId, Map<Integer, L2SkillLearn>> _skillTrees;
    private List<L2SkillLearn> _fishingSkillTrees; // all common skills (teached by Fisherman)
    private List<L2SkillLearn> _expandDwarfCraftSkillTrees; // list of special skill for dwarf (expand dwarf craft) learned by class teacher
    private List<L2PledgeSkillLearn> _pledgeSkillTrees; // pledge skill list
    private List<L2EnchantSkillLearn> _enchantSkillTrees; // enchant skill list

    public static SkillTreeTable getInstance() {
        if (_instance == null) {
            _instance = new SkillTreeTable();
        }

        return _instance;
    }

    /**
     * @param grade The grade level searched
     * @return the minimum level needed to have this Expertise.
     */
    public int getExpertiseLevel(int grade) {
        if (grade <= 0) {
            return 0;
        }

        // since expertise comes at same level for all classes we use paladin for now
        Map<Integer, L2SkillLearn> learnMap = getSkillTrees().get(ClassId.paladin);

        int skillHashCode = SkillTable.getSkillHashCode(239, grade);
        if (learnMap.containsKey(skillHashCode)) {
            return learnMap.get(skillHashCode).getMinLevel();
        }

        _log.error("Expertise not found for grade " + grade);
        return 0;
    }

    /**
     * Each class receives new skill on certain levels, this methods allow the retrieval of the minimum character level of given class required to learn a given skill
     *
     * @param skillId  The iD of the skill
     * @param classId  The classId of the character
     * @param skillLvl The SkillLvl
     * @return The min level
     */
    public int getMinSkillLevel(int skillId, ClassId classId, int skillLvl) {
        Map<Integer, L2SkillLearn> map = getSkillTrees().get(classId);

        int skillHashCode = SkillTable.getSkillHashCode(skillId, skillLvl);

        if (map.containsKey(skillHashCode)) {
            return map.get(skillHashCode).getMinLevel();
        }

        return 0;
    }

    public int getMinSkillLevel(int skillId, int skillLvl) {
        int skillHashCode = SkillTable.getSkillHashCode(skillId, skillLvl);

        // Look on all classes for this skill (takes the first one found)
        for (Map<Integer, L2SkillLearn> map : getSkillTrees().values()) {
            // checks if the current class has this skill
            if (map.containsKey(skillHashCode)) {
                return map.get(skillHashCode).getMinLevel();
            }
        }
        return 0;
    }

    private SkillTreeTable() {
        int count = 0;
        CharTemplateRepository charTemplateRepository = DatabaseAccess.getRepository(CharTemplateRepository.class);
        SkillTreeRepository skillTreeRepository = DatabaseAccess.getRepository(SkillTreeRepository.class);
        int parentClassId;
        int classId;
        for (CharTemplate charTemplate : charTemplateRepository.findAll()) {
            Map<Integer, L2SkillLearn>  map = new LinkedHashMap<>();
            parentClassId = charTemplate.getParentId();
            classId = charTemplate.getId();

            if (parentClassId != -1) {
                Map<Integer, L2SkillLearn> parentMap = getSkillTrees().get(ClassId.values()[parentClassId]);
                map.putAll(parentMap);
            }

            skillTreeRepository.findAllByClassOrderBySkill(classId).forEach(skillTrees -> {
                int id = skillTrees.getSkillId();
                int lvl = skillTrees.getLevel();
                String name = skillTrees.getName();
                int minLvl = skillTrees.getMinLevel();
                int cost = skillTrees.getSp();

                L2SkillLearn skillLearn = new L2SkillLearn(id, lvl, minLvl, name, cost, 0, 0);
                map.put(SkillTable.getSkillHashCode(id, lvl), skillLearn);
            });

            getSkillTrees().put(ClassId.values()[classId], map);
            count += map.size();

            _log.debug("SkillTreeTable: skill tree for class {} has {} skills.", classId, map.size());
        }

        _log.info("SkillTreeTable: Loaded " + count + " skills.");


        _fishingSkillTrees = new LinkedList<>();
        _expandDwarfCraftSkillTrees = new LinkedList<>();
        FishingSkillTreeRepository fishingSkillTreeRepository = DatabaseAccess.getRepository(FishingSkillTreeRepository.class);
        fishingSkillTreeRepository.findAllOrderBySkillAndLevel().forEach(fishingSkillTree -> {
            int id = fishingSkillTree.getSkillId();
            int lvl = fishingSkillTree.getLevel();
            String name = fishingSkillTree.getName();
            int minLvl = fishingSkillTree.getMinLevel();
            int cost = fishingSkillTree.getSp();
            int costId = fishingSkillTree.getCostId();
            int costCount = fishingSkillTree.getCost();
            int isDwarven = fishingSkillTree.getIsForDwarf();

            L2SkillLearn skill = new L2SkillLearn(id, lvl, minLvl, name, cost, costId, costCount);

            if (isDwarven == 0) {
                _fishingSkillTrees.add(skill);
            } else {
                _expandDwarfCraftSkillTrees.add(skill);
            }
        });


        int count2 = _fishingSkillTrees.size();
        int count3 = _expandDwarfCraftSkillTrees.size();

        _enchantSkillTrees = new LinkedList<>();

        EnchantSkillTreesRepository enchantSkillTreesRepository = DatabaseAccess.getRepository(EnchantSkillTreesRepository.class);
        enchantSkillTreesRepository.findAllOrderBySkillAndLevel().forEach(enchantSkillTrees -> {
            int id = enchantSkillTrees.getSkillId();
            int lvl = enchantSkillTrees.getLevel();
            String name = enchantSkillTrees.getName();
            int baseLvl = enchantSkillTrees.getBaseLvl();
            int minSkillLvl = enchantSkillTrees.getMinSkillLvl();
            int sp = enchantSkillTrees.getSp();
            int exp = enchantSkillTrees.getExp();
            byte rate76 = enchantSkillTrees.getSuccessRate76();
            byte rate77 = enchantSkillTrees.getSuccessRate77();
            byte rate78 = enchantSkillTrees.getSuccessRate78();

            L2EnchantSkillLearn skill = new L2EnchantSkillLearn(id, lvl, minSkillLvl, baseLvl, name, sp, exp, rate76, rate77, rate78);

            _enchantSkillTrees.add(skill);
        });

        int count4 = _enchantSkillTrees.size();

        _pledgeSkillTrees = new LinkedList<>();
        PledgeSkillTreesRepository pledgeSkillTreesRepository = DatabaseAccess.getRepository(PledgeSkillTreesRepository.class);
        pledgeSkillTreesRepository.findAllOrderBySkillAndLevel().forEach(pledgeSkill -> {
            int id = pledgeSkill.getSkillId();
            int lvl = pledgeSkill.getLevel();
            String name = pledgeSkill.getName();
            int baseLvl = pledgeSkill.getClanLvl();
            int sp = pledgeSkill.getRepCost();
            int itemId = pledgeSkill.getItemId();


            L2PledgeSkillLearn skill = new L2PledgeSkillLearn(id, lvl, baseLvl, name, sp, itemId);

            _pledgeSkillTrees.add(skill);
        });

        int count5 = _pledgeSkillTrees.size();

        _log.info("FishingSkillTreeTable: Loaded " + count2 + " general skills.");
        _log.info("FishingSkillTreeTable: Loaded " + count3 + " dwarven skills.");
        _log.info("EnchantSkillTreeTable: Loaded " + count4 + " enchant skills.");
        _log.info("PledgeSkillTreeTable: Loaded " + count5 + " pledge skills");
    }

    private Map<ClassId, Map<Integer, L2SkillLearn>> getSkillTrees() {
        if (_skillTrees == null) {
            _skillTrees = new LinkedHashMap<>();
        }

        return _skillTrees;
    }

    public L2SkillLearn[] getAvailableSkills(L2PcInstance cha, ClassId classId) {
        List<L2SkillLearn> result = new LinkedList<>();
        Collection<L2SkillLearn> skills = getSkillTrees().get(classId).values();

        if (skills == null) {
            // the skilltree for this class is undefined, so we give an empty list
            _log.warn("Skilltree for class " + classId + " is not defined !");
            return new L2SkillLearn[0];
        }

        L2Skill[] oldSkills = cha.getAllSkills();

        for (L2SkillLearn temp : skills) {
            if (temp.getMinLevel() <= cha.getLevel()) {
                boolean knownSkill = false;

                for (int j = 0; (j < oldSkills.length) && !knownSkill; j++) {
                    if (oldSkills[j].getId() == temp.getId()) {
                        knownSkill = true;

                        if (oldSkills[j].getLevel() == (temp.getLevel() - 1)) {
                            // this is the next level of a skill that we know
                            result.add(temp);
                        }
                    }
                }

                if (!knownSkill && (temp.getLevel() == 1)) {
                    // this is a new skill
                    result.add(temp);
                }
            }
        }

        return result.toArray(new L2SkillLearn[result.size()]);
    }

    public L2SkillLearn[] getAvailableSkills(L2PcInstance cha) {
        List<L2SkillLearn> result = new LinkedList<>();
        List<L2SkillLearn> skills = new LinkedList<>();

        skills.addAll(_fishingSkillTrees);

        if (skills.isEmpty()) {
            // the skilltree for this class is undefined, so we give an empty list
            _log.warn("Skilltree for fishing is not defined !");
            return new L2SkillLearn[0];
        }

        if (cha.hasDwarvenCraft() && (_expandDwarfCraftSkillTrees != null)) {
            skills.addAll(_expandDwarfCraftSkillTrees);
        }

        L2Skill[] oldSkills = cha.getAllSkills();

        for (L2SkillLearn temp : skills) {
            if (temp.getMinLevel() <= cha.getLevel()) {
                boolean knownSkill = false;

                for (int j = 0; (j < oldSkills.length) && !knownSkill; j++) {
                    if (oldSkills[j].getId() == temp.getId()) {
                        knownSkill = true;

                        if (oldSkills[j].getLevel() == (temp.getLevel() - 1)) {
                            // this is the next level of a skill that we know
                            result.add(temp);
                        }
                    }
                }

                if (!knownSkill && (temp.getLevel() == 1)) {
                    // this is a new skill
                    result.add(temp);
                }
            }
        }

        return result.toArray(new L2SkillLearn[result.size()]);
    }

    public L2EnchantSkillLearn[] getAvailableEnchantSkills(L2PcInstance cha) {
        List<L2EnchantSkillLearn> result = new LinkedList<>();
        List<L2EnchantSkillLearn> skills = new LinkedList<>();

        skills.addAll(_enchantSkillTrees);

        if (skills.isEmpty()) {
            // the skilltree for this class is undefined, so we give an empty list
            _log.warn("Skilltree for enchanting is not defined !");
            return new L2EnchantSkillLearn[0];
        }

        L2Skill[] oldSkills = cha.getAllSkills();

        for (L2EnchantSkillLearn temp : skills) {
            if (76 <= cha.getLevel()) {
                boolean knownSkill = false;

                for (int j = 0; (j < oldSkills.length) && !knownSkill; j++) {
                    if (oldSkills[j].getId() == temp.getId()) {
                        knownSkill = true;

                        if (oldSkills[j].getLevel() == temp.getMinSkillLevel()) {
                            // this is the next level of a skill that we know
                            result.add(temp);
                        }
                    }
                }

            }
        }
        // cha.sendMessage("loaded "+ result.size()+" enchant skills for this char(You)");
        return result.toArray(new L2EnchantSkillLearn[result.size()]);
    }

    public L2PledgeSkillLearn[] getAvailablePledgeSkills(L2PcInstance cha) {
        List<L2PledgeSkillLearn> result = new LinkedList<>();
        List<L2PledgeSkillLearn> skills = _pledgeSkillTrees;

        if (skills == null) {
            // the skilltree for this class is undefined, so we give an empty list

            _log.warn("No clan skills defined!");
            return new L2PledgeSkillLearn[0];
        }

        L2Skill[] oldSkills = cha.getClan().getAllSkills();

        for (L2PledgeSkillLearn temp : skills) {
            if (temp.getBaseLevel() <= cha.getClan().getLevel()) {
                boolean knownSkill = false;

                for (int j = 0; (j < oldSkills.length) && !knownSkill; j++) {
                    if (oldSkills[j].getId() == temp.getId()) {
                        knownSkill = true;

                        if (oldSkills[j].getLevel() == (temp.getLevel() - 1)) {
                            // this is the next level of a skill that we know
                            result.add(temp);
                        }
                    }
                }

                if (!knownSkill && (temp.getLevel() == 1)) {
                    // this is a new skill
                    result.add(temp);
                }
            }
        }

        return result.toArray(new L2PledgeSkillLearn[result.size()]);
    }

    /**
     * Returns all allowed skills for a given class.
     *
     * @param classId
     * @return all allowed skills for a given class.
     */
    public Collection<L2SkillLearn> getAllowedSkills(ClassId classId) {
        return getSkillTrees().get(classId).values();
    }

    public int getMinLevelForNewSkill(L2PcInstance cha, ClassId classId) {
        int minLevel = 0;
        Collection<L2SkillLearn> skills = getSkillTrees().get(classId).values();

        if (skills == null) {
            // the skilltree for this class is undefined, so we give an empty list
            _log.warn("Skilltree for class " + classId + " is not defined !");
            return minLevel;
        }

        for (L2SkillLearn temp : skills) {
            if ((temp.getMinLevel() > cha.getLevel()) && (temp.getSpCost() != 0)) {
                if ((minLevel == 0) || (temp.getMinLevel() < minLevel)) {
                    minLevel = temp.getMinLevel();
                }
            }
        }

        return minLevel;
    }

    public int getMinLevelForNewSkill(L2PcInstance cha) {
        int minLevel = 0;
        List<L2SkillLearn> skills = new LinkedList<>();

        skills.addAll(_fishingSkillTrees);

        if (skills.isEmpty()) {
            // the skilltree for this class is undefined, so we give an empty list
            _log.warn("SkillTree for fishing is not defined !");
            return minLevel;
        }

        if (cha.hasDwarvenCraft() && (_expandDwarfCraftSkillTrees != null)) {
            skills.addAll(_expandDwarfCraftSkillTrees);
        }

        for (L2SkillLearn s : skills) {
            if (s.getMinLevel() > cha.getLevel()) {
                if ((minLevel == 0) || (s.getMinLevel() < minLevel)) {
                    minLevel = s.getMinLevel();
                }
            }
        }

        return minLevel;
    }

    public int getSkillCost(L2PcInstance player, L2Skill skill) {
        int skillCost = 100000000;
        ClassId classId = player.getSkillLearningClassId();
        int skillHashCode = SkillTable.getSkillHashCode(skill);

        if (getSkillTrees().get(classId).containsKey(skillHashCode)) {
            L2SkillLearn skillLearn = getSkillTrees().get(classId).get(skillHashCode);
            if (skillLearn.getMinLevel() <= player.getLevel()) {
                skillCost = skillLearn.getSpCost();
                if (!player.getClassId().equalsOrChildOf(classId)) {
                    if (skill.getCrossLearnAdd() < 0) {
                        return skillCost;
                    }

                    skillCost += skill.getCrossLearnAdd();
                    skillCost *= skill.getCrossLearnMul();
                }

                if ((classId.getRace() != player.getRace()) && !player.isSubClassActive()) {
                    skillCost *= skill.getCrossLearnRace();
                }

                if (classId.isMage() != player.getClassId().isMage()) {
                    skillCost *= skill.getCrossLearnProf();
                }
            }
        }

        return skillCost;
    }

    public int getSkillSpCost(L2PcInstance player, L2Skill skill) {
        int skillCost = 100000000;
        L2EnchantSkillLearn[] enchantSkillLearnList = getAvailableEnchantSkills(player);

        for (L2EnchantSkillLearn enchantSkillLearn : enchantSkillLearnList) {
            if (enchantSkillLearn.getId() != skill.getId()) {
                continue;
            }

            if (enchantSkillLearn.getLevel() != skill.getLevel()) {
                continue;
            }

            if (76 > player.getLevel()) {
                continue;
            }

            skillCost = enchantSkillLearn.getSpCost();
        }
        return skillCost;
    }

    public int getSkillExpCost(L2PcInstance player, L2Skill skill) {
        int skillCost = 100000000;
        L2EnchantSkillLearn[] enchantSkillLearnList = getAvailableEnchantSkills(player);

        for (L2EnchantSkillLearn enchantSkillLearn : enchantSkillLearnList) {
            if (enchantSkillLearn.getId() != skill.getId()) {
                continue;
            }

            if (enchantSkillLearn.getLevel() != skill.getLevel()) {
                continue;
            }

            if (76 > player.getLevel()) {
                continue;
            }

            skillCost = enchantSkillLearn.getExp();
        }
        return skillCost;
    }

    public byte getSkillRate(L2PcInstance player, L2Skill skill) {
        L2EnchantSkillLearn[] enchantSkillLearnList = getAvailableEnchantSkills(player);

        for (L2EnchantSkillLearn enchantSkillLearn : enchantSkillLearnList) {
            if (enchantSkillLearn.getId() != skill.getId()) {
                continue;
            }

            if (enchantSkillLearn.getLevel() != skill.getLevel()) {
                continue;
            }

            return enchantSkillLearn.getRate(player);
        }
        return 0;
    }
}
