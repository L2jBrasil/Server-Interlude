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

import com.l2jbr.commons.Config;
import com.l2jbr.commons.database.DatabaseAccess;
import com.l2jbr.commons.database.L2DatabaseFactory;
import com.l2jbr.commons.util.Util;
import com.l2jbr.gameserver.model.L2DropCategory;
import com.l2jbr.gameserver.model.L2DropData;
import com.l2jbr.gameserver.model.L2MinionData;
import com.l2jbr.gameserver.model.L2Skill;
import com.l2jbr.gameserver.model.base.ClassId;
import com.l2jbr.gameserver.model.database.Minions;
import com.l2jbr.gameserver.model.database.Npc;
import com.l2jbr.gameserver.model.database.repository.MinionRepository;
import com.l2jbr.gameserver.model.database.repository.NpcRepository;
import com.l2jbr.gameserver.model.database.repository.NpcSkillRepository;
import com.l2jbr.gameserver.model.database.repository.SkillLearnRepository;
import com.l2jbr.gameserver.skills.Stats;
import com.l2jbr.gameserver.templates.L2NpcTemplate;
import com.l2jbr.gameserver.templates.StatsSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


/**
 * This class ...
 *
 * @version $Revision: 1.8.2.6.2.9 $ $Date: 2005/04/06 16:13:25 $
 */
public class NpcTable {
    private static Logger _log = LoggerFactory.getLogger(NpcTable.class.getName());

    private static NpcTable _instance;

    private final Map<Integer, L2NpcTemplate> _npcs;
    private boolean _initialized = false;

    public static NpcTable getInstance() {
        if (_instance == null) {
            _instance = new NpcTable();
        }

        return _instance;
    }

    private NpcTable() {
        _npcs = new LinkedHashMap<>();

        restoreNpcData();
    }

    private void restoreNpcData() {
        NpcRepository npcRepository = DatabaseAccess.getRepository(NpcRepository.class);
        npcRepository.findAll().forEach(this::addToNpcMap);

        _log.info("info.loaded.npc", _npcs.size());

        try (Connection con = L2DatabaseFactory.getInstance().getConnection()) {
            NpcSkillRepository npcSkillRepository = DatabaseAccess.getRepository(NpcSkillRepository.class);
            npcSkillRepository.findAll().forEach(npcSkill -> {
                int mobId = npcSkill.getNpcid();
                L2NpcTemplate npcDat = _npcs.get(mobId);

                if (Util.isNull(npcDat)) {
                    return;
                }

                int skillId = npcSkill.getSkillid();
                int level = npcSkill.getLevel();

                if (Util.isNull(npcDat.race) && (skillId == 4416)) {
                    npcDat.setRace(level);
                    return;
                }

                L2Skill skill = SkillTable.getInstance().getInfo(skillId, level);

                if (Util.isNull(skill)) {
                    return;
                }

                npcDat.addSkill(skill);
            });

            try {
                PreparedStatement statement2 = con.prepareStatement("SELECT " + L2DatabaseFactory.getInstance().safetyString(new String[]
                        {
                                "mobId",
                                "itemId",
                                "min",
                                "max",
                                "category",
                                "chance"
                        }) + " FROM droplist ORDER BY mobId, chance DESC");
                ResultSet dropData = statement2.executeQuery();
                L2DropData dropDat = null;
                L2NpcTemplate npcDat = null;

                while (dropData.next()) {
                    int mobId = dropData.getInt("mobId");
                    npcDat = _npcs.get(mobId);
                    if (npcDat == null) {
                        _log.error("NPCTable: No npc correlating with id : " + mobId);
                        continue;
                    }
                    dropDat = new L2DropData();

                    dropDat.setItemId(dropData.getInt("itemId"));
                    dropDat.setMinDrop(dropData.getInt("min"));
                    dropDat.setMaxDrop(dropData.getInt("max"));
                    dropDat.setChance(dropData.getInt("chance"));

                    int category = dropData.getInt("category");

                    npcDat.addDropData(dropDat, category);
                }

                dropData.close();
                statement2.close();
            } catch (Exception e) {
                _log.error("NPCTable: Error reading NPC drop data: " + e);
            }

            SkillLearnRepository repository = DatabaseAccess.getRepository(SkillLearnRepository.class);
            repository.findAll().forEach(skillLearn -> {
                int npcId = skillLearn.getNpcId();
                int classId = skillLearn.getClassId();
                L2NpcTemplate npc = getTemplate(npcId);

                if (npc == null) {
                    _log.warn("NPCTable: Error getting NPC template ID " + npcId + " while trying to load skill trainer data.");
                    return;
                }

                npc.addTeachInfo(ClassId.values()[classId]);
            });

            MinionRepository minionRepository = DatabaseAccess.getRepository(MinionRepository.class);
            int cnt = 0;
            L2NpcTemplate npcDat = null;
            L2MinionData minionDat = null;
            for (Minions minion : minionRepository.findAll()) {
                int raidId = minion.getBossId();
                npcDat = _npcs.get(raidId);
                minionDat = new L2MinionData();
                minionDat.setMinionId(minion.getMinionId());
                minionDat.setAmountMin(minion.getAmountMin());
                minionDat.setAmountMax(minion.getAmountMin());
                npcDat.addRaidData(minionDat);
                cnt++;
            }
            _log.info("NpcTable: Loaded {} Minions", cnt);
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        _initialized = true;
    }

    private StatsSet npcToStatSet(Npc npc) {
        StatsSet npcDat = new StatsSet();
        int id = npc.getId();

        if (Config.ASSERT) {
            assert id < 1000000;
        }

        npcDat.set("npcId", id);
        npcDat.set("idTemplate", npc.getIdTemplate());
        int level = npc.getLevel();
        npcDat.set("level", level);
        npcDat.set("jClass", npc.getNpcClass());

        npcDat.set("baseShldDef", 0);
        npcDat.set("baseShldRate", 0);
        npcDat.set("baseCritRate", 38);

        npcDat.set("name", npc.getName());
        npcDat.set("serverSideName", npc.getServerSideName() == 1);
        // npcDat.set("name", "");
        npcDat.set("title", npc.getTitle());
        npcDat.set("serverSideTitle", npc.getServerSideTitle() == 1);
        npcDat.set("collision_radius", npc.getCollision_radius());
        npcDat.set("collision_height", npc.getCollision_height());
        npcDat.set("sex", npc.getSex());
        npcDat.set("type", npc.getType());
        npcDat.set("baseAtkRange", npc.getAttackrange());
        npcDat.set("rewardExp", npc.getExp());
        npcDat.set("rewardSp", npc.getSp());
        npcDat.set("basePAtkSpd", npc.getAtkspd());
        npcDat.set("baseMAtkSpd", npc.getMatkspd());
        npcDat.set("aggroRange", npc.getAggro());
        npcDat.set("rhand", npc.getRhand());
        npcDat.set("lhand", npc.getLhand());
        npcDat.set("armor", npc.getArmor());
        npcDat.set("baseWalkSpd", npc.getWalkspd());
        npcDat.set("baseRunSpd", npc.getRunspd());

        // constants, until we have stats in DB
        npcDat.set("baseSTR", npc.getStr());
        npcDat.set("baseCON", npc.getCon());
        npcDat.set("baseDEX", npc.getDex());
        npcDat.set("baseINT", npc.getInt());
        npcDat.set("baseWIT", npc.getWit());
        npcDat.set("baseMEN", npc.getMen());

        npcDat.set("baseHpMax", npc.getHp());
        npcDat.set("baseCpMax", 0);
        npcDat.set("baseMpMax", npc.getMp());
        float hpReg = npc.getHpreg();
        npcDat.set("baseHpReg", hpReg > 0 ? hpReg : 1.5 + ((level - 1) / 10.0));
        float mpReg = npc.getMpreg();
        npcDat.set("baseMpReg", mpReg > 0 ? mpReg : 0.9 + (0.3 * ((level - 1) / 10.0)));
        npcDat.set("basePAtk", npc.getPatk());
        npcDat.set("basePDef", npc.getPdef());
        npcDat.set("baseMAtk", npc.getMatk());
        npcDat.set("baseMDef", npc.getMdef());

        npcDat.set("factionId", npc.getFactionId());
        npcDat.set("factionRange", npc.getFactionRange());

        npcDat.set("isUndead", npc.getIsUndead());

        npcDat.set("absorb_level", npc.getAbsorbLevel());
        npcDat.set("absorb_type", npc.getAbsorbType());
        return npcDat;
    }


    public void reloadNpc(int id) {
        Connection con = null;

        try {
            // save a copy of the old data
            L2NpcTemplate old = getTemplate(id);
            Map<Integer, L2Skill> skills = new LinkedHashMap<>();

            if (old.getSkills() != null) {
                skills.putAll(old.getSkills());
            }

            List<L2DropCategory> categories = new LinkedList<>();

            if (old.getDropData() != null) {
                categories.addAll(old.getDropData());
            }

            ClassId[] classIds = null;

            if (old.getTeachInfo() != null) {
                classIds = old.getTeachInfo().clone();
            }

            List<L2MinionData> minions = new LinkedList<>();

            if (old.getMinionData() != null) {
                minions.addAll(old.getMinionData());
            }

            // reload the NPC base data
            NpcRepository repository = DatabaseAccess.getRepository(NpcRepository.class);
            repository.findById(id).ifPresent(this::addToNpcMap);


            // restore additional data from saved copy
            L2NpcTemplate created = getTemplate(id);

            for (L2Skill skill : skills.values()) {
                created.addSkill(skill);
            }

            if (classIds != null) {
                for (ClassId classId : classIds) {
                    created.addTeachInfo(classId);
                }
            }

            for (L2MinionData minion : minions) {
                created.addRaidData(minion);
            }
        } catch (Exception e) {
            _log.warn("NPCTable: Could not reload data for NPC " + id + ": " + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
            }
        }
    }

    private void addToNpcMap(Npc npc) {
        StatsSet npcDat = npcToStatSet(npc);
        L2NpcTemplate template = new L2NpcTemplate(npcDat);
        template.addVulnerability(Stats.BOW_WPN_VULN, 1);
        template.addVulnerability(Stats.BLUNT_WPN_VULN, 1);
        template.addVulnerability(Stats.DAGGER_WPN_VULN, 1);

        _npcs.put(npc.getId(), template);
    }

    // just wrapper
    public void reloadAllNpc() {
        restoreNpcData();
    }

    public void saveNpc(StatsSet npc) {
        Connection con = null;
        String query = "";

        try {
            con = L2DatabaseFactory.getInstance().getConnection();
            Map<String, Object> set = npc.getSet();

            String name = "";
            String values = "";

            for (Object obj : set.keySet()) {
                name = (String) obj;

                if (!name.equalsIgnoreCase("npcId")) {
                    if (values != "") {
                        values += ", ";
                    }

                    values += name + " = '" + set.get(name) + "'";
                }
            }

            //TODO remove this
            query = "UPDATE npc SET " + values + " WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, npc.getInteger("npcId"));
            statement.execute();
            statement.close();
        } catch (Exception e) {
            _log.warn("NPCTable: Could not store new NPC data in database: " + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
            }
        }
    }

    public boolean isInitialized() {
        return _initialized;
    }

    public void replaceTemplate(L2NpcTemplate npc) {
        _npcs.put(npc.npcId, npc);
    }

    public L2NpcTemplate getTemplate(int id) {
        return _npcs.get(id);
    }

    public L2NpcTemplate getTemplateByName(String name) {
        for (L2NpcTemplate npcTemplate : _npcs.values()) {
            if (npcTemplate.name.equalsIgnoreCase(name)) {
                return npcTemplate;
            }
        }

        return null;
    }

    public L2NpcTemplate[] getAllOfLevel(int lvl) {
        List<L2NpcTemplate> list = new LinkedList<>();

        for (L2NpcTemplate t : _npcs.values()) {
            if (t.level == lvl) {
                list.add(t);
            }
        }

        return list.toArray(new L2NpcTemplate[list.size()]);
    }

    public L2NpcTemplate[] getAllMonstersOfLevel(int lvl) {
        List<L2NpcTemplate> list = new LinkedList<>();

        for (L2NpcTemplate t : _npcs.values()) {
            if ((t.level == lvl) && "L2Monster".equals(t.type)) {
                list.add(t);
            }
        }

        return list.toArray(new L2NpcTemplate[list.size()]);
    }

    public L2NpcTemplate[] getAllNpcStartingWith(String letter) {
        List<L2NpcTemplate> list = new LinkedList<>();

        for (L2NpcTemplate t : _npcs.values()) {
            if (t.name.startsWith(letter) && "L2Npc".equals(t.type)) {
                list.add(t);
            }
        }

        return list.toArray(new L2NpcTemplate[list.size()]);
    }

    /**
     * @param classType
     * @return
     */
    public Set<Integer> getAllNpcOfClassType(String classType) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param clazz
     * @return
     */
    public Set<Integer> getAllNpcOfL2jClass(Class<?> clazz) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param aiType
     * @return
     */
    public Set<Integer> getAllNpcOfAiType(String aiType) {
        // TODO Auto-generated method stub
        return null;
    }

}