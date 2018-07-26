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
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.model.base.PlayerClass;
import com.l2jbr.gameserver.model.entity.database.*;
import com.l2jbr.gameserver.model.entity.database.repository.EnchantSkillTreesRepository;
import com.l2jbr.gameserver.model.entity.database.repository.FishingSkillTreeRepository;
import com.l2jbr.gameserver.model.entity.database.repository.PledgeSkillTreesRepository;
import com.l2jbr.gameserver.model.entity.database.repository.SkillTreeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class SkillTreeTable {
    private static Logger _log = LoggerFactory.getLogger(SkillTreeTable.class.getName());
    private static SkillTreeTable INSTANCE;

    private Map<PlayerClass, Map<Integer, SkillInfo>> _skillTrees;
    private List<FishingSkill> _fishingSkillTrees; // all common skills (teached by Fisherman)
    private List<FishingSkill> _expandDwarfCraftSkillTrees; // list of special skill for DWARF (expand DWARF craft) learned by class teacher
    private List<ClanSkillInfo> clanSkillTrees; // pledge skill list
    private List<EnchantSkillInfo> _enchantSkillTrees; // enchant skill list

    public static SkillTreeTable getInstance() {
        if (isNull(INSTANCE)) {
            INSTANCE = new SkillTreeTable();
        }
        return INSTANCE;
    }

    private SkillTreeTable() {
        loadSkills();
        loadFishingSkills();
        loadEnchantSkills();
        loadClanSkills();
    }

    private void loadSkills() {
        int count = 0;
        SkillTreeRepository skillTreeRepository = DatabaseAccess.getRepository(SkillTreeRepository.class);

        for (PlayerTemplate playerTemplate : CharTemplateTable.getInstance().all()) {

            Map<Integer, SkillInfo> map = new HashMap<>();
            PlayerClass playerClass = playerTemplate.getPlayerClass();

            if (nonNull(playerClass.getParent())) {
                Map<Integer, SkillInfo> parentMap = getSkillTrees().get(playerClass.getParent());
                map.putAll(parentMap);
            }

            skillTreeRepository.findAllByClassOrderBySkill(playerClass.getId()).forEach(skill -> {
                int id = skill.getId();
                int lvl = skill.getLevel();
                map.put(SkillTable.getSkillHashCode(id, lvl), skill);
            });

            getSkillTrees().put(playerClass, map);
            count += map.size();

            _log.debug("SkillTreeTable: skill tree for class {} has {} skills.", playerClass.getId(), map.size());
        }

        _log.info("SkillTreeTable: Loaded {} skills.", count);
    }

    private void loadFishingSkills() {
        FishingSkillTreeRepository fishingSkillTreeRepository = DatabaseAccess.getRepository(FishingSkillTreeRepository.class);
        _fishingSkillTrees = fishingSkillTreeRepository.findForNonDwarf();
        _expandDwarfCraftSkillTrees = fishingSkillTreeRepository.findForDwarf();
        _log.info("FishingSkillTreeTable: Loaded {} general skills.", _fishingSkillTrees.size());
        _log.info("FishingSkillTreeTable: Loaded {} dwarven skills.", _expandDwarfCraftSkillTrees.size());
    }

    private void loadEnchantSkills() {
        _enchantSkillTrees = DatabaseAccess.getRepository(EnchantSkillTreesRepository.class).findAllOrderBySkillAndLevel();
        _log.info("EnchantSkillTreeTable: Loaded {} enchant skills.", _enchantSkillTrees.size());
    }

    private void loadClanSkills() {
        clanSkillTrees = DatabaseAccess.getRepository(PledgeSkillTreesRepository.class).findAllOrderBySkillAndLevel();
        _log.info("PledgeSkillTreeTable: Loaded {} pledge skills", clanSkillTrees.size());
    }

    /**
     * @param grade The grade level searched
     * @return the minimum level needed to have this Expertise.
     */
    public int getExpertiseLevel(int grade) {
        if (grade <= 0) {
            return 0;
        }

        // since expertise comes at same level for all classes we use PALADIN for now
        Map<Integer, SkillInfo> learnMap = getSkillTrees().get(PlayerClass.PALADIN);

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
     * @param playerClass  The playerClass of the character
     * @param skillLvl The SkillLvl
     * @return The min level
     */
    public int getMinSkillLevel(int skillId, PlayerClass playerClass, int skillLvl) {
        Map<Integer, SkillInfo> map = getSkillTrees().get(playerClass);

        int skillHashCode = SkillTable.getSkillHashCode(skillId, skillLvl);

        if (map.containsKey(skillHashCode)) {
            return map.get(skillHashCode).getMinLevel();
        }

        return 0;
    }

    public int getMinSkillLevel(int skillId, int skillLvl) {
        int skillHashCode = SkillTable.getSkillHashCode(skillId, skillLvl);

        // Look on all classes for this skill (takes the first one found)
        for (Map<Integer, SkillInfo> map : getSkillTrees().values()) {
            // checks if the current class has this skill
            if (map.containsKey(skillHashCode)) {
                return map.get(skillHashCode).getMinLevel();
            }
        }
        return 0;
    }


    private Map<PlayerClass, Map<Integer, SkillInfo>> getSkillTrees() {
        if (_skillTrees == null) {
            _skillTrees = new HashMap<>();
        }
        return _skillTrees;
    }

    public List<SkillInfo> getAvailableSkills(L2PcInstance cha, PlayerClass playerClass) {
        Collection<SkillInfo> skills = getSkillTrees().get(playerClass).values();
        List<SkillInfo> result = new ArrayList<>(skills.size());

        L2Skill[] oldSkills = cha.getAllSkills();

        for (SkillInfo temp : skills) {
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

        return result;
    }

    public List<SkillInfo> getAvailableSkills(L2PcInstance cha) {
        List<SkillInfo> result = new LinkedList<>();
        List<SkillInfo> skills = new LinkedList<>();

        skills.addAll(_fishingSkillTrees);

        if (skills.isEmpty()) {
            // the skilltree for this class is undefined, so we give an empty list
            _log.warn("Skilltree for fishing is not defined !");
            return result;
        }

        if (cha.hasDwarvenCraft() && (_expandDwarfCraftSkillTrees != null)) {
            skills.addAll(_expandDwarfCraftSkillTrees);
        }

        L2Skill[] oldSkills = cha.getAllSkills();

        for (SkillInfo temp : skills) {
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

        return result;
    }

    public List<EnchantSkillInfo> getAvailableEnchantSkills(L2PcInstance cha) {
        List<EnchantSkillInfo> result = new LinkedList<>();
        List<EnchantSkillInfo> skills = new LinkedList<>();

        skills.addAll(_enchantSkillTrees);

        if (skills.isEmpty()) {
            // the skilltree for this class is undefined, so we give an empty list
            _log.warn("Skilltree for enchanting is not defined !");
            return result;
        }

        L2Skill[] oldSkills = cha.getAllSkills();

        for (EnchantSkillInfo temp : skills) {
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
        return result;
    }

    public List<ClanSkillInfo> getAvailablePledgeSkills(L2PcInstance cha) {
        List<ClanSkillInfo> result = new LinkedList<>();
        List<ClanSkillInfo> skills = clanSkillTrees;

        if (skills == null) {
            // the skilltree for this class is undefined, so we give an empty list

            _log.warn("No clan skills defined!");
            return result;
        }

        L2Skill[] oldSkills = cha.getClan().getAllSkills();

        for (ClanSkillInfo temp : skills) {
            if (temp.getClanLvl() <= cha.getClan().getLevel()) {
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

        return result;
    }

    /**
     * Returns all allowed skills for a given class.
     *
     * @param playerClass
     * @return all allowed skills for a given class.
     */
    public Collection<SkillInfo> getAllowedSkills(PlayerClass playerClass) {
        return getSkillTrees().get(playerClass).values();
    }

    public int getMinLevelForNewSkill(L2PcInstance cha, PlayerClass playerClass) {
        int minLevel = 0;
        Collection<SkillInfo> skills = getSkillTrees().get(playerClass).values();

        for (SkillInfo temp : skills) {
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
        List<SkillInfo> skills = new LinkedList<>();

        skills.addAll(_fishingSkillTrees);

        if (skills.isEmpty()) {
            // the skilltree for this class is undefined, so we give an empty list
            _log.warn("SkillTree for fishing is not defined !");
            return minLevel;
        }

        if (cha.hasDwarvenCraft() && (_expandDwarfCraftSkillTrees != null)) {
            skills.addAll(_expandDwarfCraftSkillTrees);
        }

        for (SkillInfo s : skills) {
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
        PlayerClass playerClass = player.getSkillLearningClassId();
        int skillHashCode = SkillTable.getSkillHashCode(skill);

        if (getSkillTrees().get(playerClass).containsKey(skillHashCode)) {
            SkillInfo skillLearn = getSkillTrees().get(playerClass).get(skillHashCode);
            if (skillLearn.getMinLevel() <= player.getLevel()) {
                skillCost = skillLearn.getSpCost();
                if (!player.getPlayerClass().equalsOrChildOf(playerClass)) {
                    if (skill.getCrossLearnAdd() < 0) {
                        return skillCost;
                    }

                    skillCost += skill.getCrossLearnAdd();
                    skillCost *= skill.getCrossLearnMul();
                }

                if ((playerClass.getRace() != player.getRace()) && !player.isSubClassActive()) {
                    skillCost *= skill.getCrossLearnRace();
                }

                if (playerClass.isMage() != player.getPlayerClass().isMage()) {
                    skillCost *= skill.getCrossLearnProf();
                }
            }
        }

        return skillCost;
    }

    public int getSkillSpCost(L2PcInstance player, L2Skill skill) {
        int skillCost = 100000000;
        List<EnchantSkillInfo> enchantSkillLearnList = getAvailableEnchantSkills(player);

        for (EnchantSkillInfo enchantSkillLearn : enchantSkillLearnList) {
            if (enchantSkillLearn.getId() != skill.getId()) {
                continue;
            }

            if (enchantSkillLearn.getLevel() != skill.getLevel()) {
                continue;
            }

            if (76 > player.getLevel()) {
                continue;
            }

            skillCost = enchantSkillLearn.getSp();
        }
        return skillCost;
    }

    public int getSkillExpCost(L2PcInstance player, L2Skill skill) {
        int skillCost = 100000000;
        List<EnchantSkillInfo> enchantSkillLearnList = getAvailableEnchantSkills(player);

        for (EnchantSkillInfo enchantSkillLearn : enchantSkillLearnList) {
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
        List<EnchantSkillInfo> enchantSkillLearnList = getAvailableEnchantSkills(player);

        for (EnchantSkillInfo enchantSkillLearn : enchantSkillLearnList) {
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
