package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.util.Util;
import com.l2jbr.gameserver.datatables.SkillTable;
import com.l2jbr.gameserver.model.L2DropCategory;
import com.l2jbr.gameserver.model.L2DropData;
import com.l2jbr.gameserver.model.L2Skill;
import com.l2jbr.gameserver.model.base.PlayerClass;
import com.l2jbr.gameserver.model.base.Race;
import com.l2jbr.gameserver.model.quest.Quest;
import com.l2jbr.gameserver.skills.SkillConstants;
import com.l2jbr.gameserver.skills.Stats;
import com.l2jbr.gameserver.templates.NpcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.isNull;

@Table("npc")
public class NpcTemplate extends CharTemplate {

    @Id
    private Integer id;
    @Column("template_id")
    private Integer templateId;
    private String name;
    @Column("server_side_name")
    private boolean serverSideName;
    private String title;
    @Column("server_side_title")
    private boolean serverSideTitle;
    private byte level;
    private String sex;
    private NpcType type;
    private Integer exp;
    private Integer sp;
    private Integer aggro;
    private Integer rhand;
    private Integer lhand;
    private Integer armor;
    @Column("faction_id")
    private String factionId;
    @Column("faction_range")
    private Integer factionRange;
    private boolean isUndead;
    @Column("absorb_level")
    private Integer absorbLevel;
    @Column("absorb_type")
    private AbsorbCrystalType absorbType;
    @Column("npcid")
    private Set<NpcSkills> npcSkills;
    @Column("mobid")
    private Set<DropList> dropLists;
    @Column("npc_id")
    private Set<SkillLearn> skillLearns;
    @Column("boss_id")
    private Set<Minions> minions;


    @Transient
    public static final Logger log = LoggerFactory.getLogger(NpcTemplate.class);
    @Transient
    private Map<Stats, Float> vulnerabilities;
    @Transient
    private Map<Integer, L2Skill> skills;
    @Transient
    private Map<Integer, L2DropCategory> dropCategories;
    @Transient
    private Race race;
    @Transient
    private Set<PlayerClass> teachInfo;
    // contains a list of quests for each event type (questStart, questAttack, questKill, etc)
    @Transient
    private Map<Quest.QuestEventType, Quest[]> _questEvents;

    public NpcTemplate() {
        vulnerabilities = new LinkedHashMap<>();
        vulnerabilities.put(Stats.BOW_WPN_VULN, 1f);
        vulnerabilities.put(Stats.BLUNT_WPN_VULN, 1f);
        vulnerabilities.put(Stats.DAGGER_WPN_VULN, 1f);
    }

    @Override
    public void onLoad() {
        super.onLoad();

        float hpRegen = getHpRegen();
        if(hpRegen <= 0) {
            hpRegen = 1.5f + ((level - 1) / 10.0f);
            setHpRegen(hpRegen);
        }

        float mpRegen = getMpRegen();
        if(mpRegen <= 0) {
            mpRegen = 0.9f + (0.3f * ((level - 1) / 10.0f));
            setMpRegen(mpRegen);
        }
    }

    private void loadTeachInfo() {
        if(Util.isNullOrEmpty(skillLearns)) {
            return;
        }
        teachInfo = new LinkedHashSet<>();
        for(SkillLearn skillLearn : skillLearns) {
            teachInfo.add(PlayerClass.values()[skillLearn.getClassId()]);
        }
    }

    private void loadDrops() {
        if(Util.isNullOrEmpty(dropLists)) {
            return;
        }
        dropCategories = new LinkedHashMap<>();
        for (DropList dropList : dropLists) {
            L2DropData dropDat = new L2DropData();

            dropDat.setItemId(dropList.getItemId());
            dropDat.setMinDrop(dropList.getMin());
            dropDat.setMaxDrop(dropList.getMax());
            dropDat.setChance(dropList.getChance());

            int category = dropList.getCategory();

            addDropData(dropDat, category);
        }
    }

    public void addDropData(L2DropData dropDat, int category) {
        if(isNull(dropCategories)) {
          loadDrops();
        }
        L2DropCategory dropCategory = dropCategories.getOrDefault(category, new L2DropCategory(category));
        dropCategory.addDropData(dropDat);
        dropCategories.putIfAbsent(category, dropCategory);
    }

    private void loadSkills() {
        if(Util.isNullOrEmpty(npcSkills)) {
           return;
        }
        skills = new ConcurrentHashMap<>();
        for (NpcSkills npcSkill : npcSkills) {
            int skillId = npcSkill.getSkillid();
            int skillLevel = npcSkill.getLevel();

            //TODO implement RACE skill
            if (isNull(race) && (skillId == SkillConstants.RACES)) {
                race = Race.fromRaceSkillLevel(skillLevel);
            }

            L2Skill skill = SkillTable.getInstance().getInfo(skillId, skillLevel);

            if (isNull(skill)) {
                continue;
            }

            skills.put(skillId, skill);
        }
    }

    public Quest[] getEventQuests(Quest.QuestEventType EventType) {
        if (_questEvents == null) {
            return null;
        }
        return _questEvents.get(EventType);
    }

    public boolean canTeach(PlayerClass playerClass) {
        if (teachInfo == null) {
            return false;
        }

        // If the player is on a third class, fetch the class teacher
        // information for its parent class.
        if (playerClass.getId() >= 88) {
            return teachInfo.contains(playerClass.getParent());
        }

        return teachInfo.contains(playerClass);
    }

    public void addQuestEvent(Quest.QuestEventType EventType, Quest quest) {
        if (_questEvents == null) {
            _questEvents = new LinkedHashMap<>();
        }

        if (_questEvents.get(EventType) == null) {
            _questEvents.put(EventType, new Quest[] {  quest });
        } else {
            Quest[] _quests = _questEvents.get(EventType);
            int len = _quests.length;

            // if only one registration per npc is allowed for this event type
            // then only register this NPC if not already registered for the specified event.
            // if a quest allows multiple registrations, then register regardless of count
            // In all cases, check if this new registration is replacing an older copy of the SAME quest
            if (!EventType.isMultipleRegistrationAllowed()) {
                if (_quests[0].getName().equals(quest.getName())) {
                    _quests[0] = quest;
                } else {
                    log.warn("Quest event not allowed in multiple quests.  Skipped addition of Event Type \"" + EventType + "\" for NPC \"" + name + "\" and quest \"" + quest.getName() + "\".");
                }
            } else {
                // be ready to add a new quest to a new copy of the list, with larger size than previously.
                Quest[] tmp = new Quest[len + 1];
                // loop through the existing quests and copy them to the new list. While doing so, also
                // check if this new quest happens to be just a replacement for a previously loaded quest.
                // If so, just save the updated reference and do NOT use the new list. Else, add the new
                // quest to the end of the new list
                for (int i = 0; i < len; i++) {
                    if (_quests[i].getName().equals(quest.getName())) {
                        _quests[i] = quest;
                        return;
                    }
                    tmp[i] = _quests[i];
                }
                tmp[len] = quest;
                _questEvents.put(EventType, tmp);
            }
        }
    }

    @Override
    public Integer getId() {
        return  id;
    }

    public int getTemplateId() {
        return templateId;
    }

    public String getName() {
        return name;
    }

    public boolean isServerSideName() {
        return serverSideName;
    }

    public String getTitle() {
        return title;
    }

    public boolean isServerSideTitle() {
        return serverSideTitle;
    }

    public byte getLevel() {
        return level;
    }

    public String getSex() {
        return sex;
    }

    public NpcType getType() {
        return type;
    }

    public int getExp() {
        return exp;
    }

    public int getSp() {
        return sp;
    }

    public int getAggro() {
        return aggro;
    }

    public int getRhand() {
        return rhand;
    }

    public int getLhand() {
        return lhand;
    }

    public int getArmor() {
        return armor;
    }

    public String getFactionId() {
        return factionId;
    }

    public int getFactionRange() {
        return factionRange;
    }

    public boolean isUndead() {
        return isUndead;
    }

    public int getAbsorbLevel() {
        return absorbLevel;
    }

    public AbsorbCrystalType getAbsorbType() {
        return absorbType;
    }

    public Map<Integer, L2Skill> getSkills() {
        if(isNull(skills)) {
            loadSkills();
        }
        return skills;
    }

    public Set<Minions> getMinions() {
        return minions;
    }

    public Map<Integer, L2DropCategory> getDropCategories() {
        if(isNull(dropCategories)) {
            loadDrops();
        }
        return dropCategories;
    }

    public Set<PlayerClass> getTeachInfo() {
        if(isNull(teachInfo)) {
            loadTeachInfo();
        }
        return teachInfo;
    }

    public void clearAllDropData() {
        dropCategories.clear();
    }

    public Race getRace() {
        if(isNull(race)) {
            return  Race.UNKNOWN;
        }
        return race;
    }

    public double getVulnerability(Stats id) {
        if ((vulnerabilities == null) || (vulnerabilities.get(id) == null)) {
            return 1;
        }
        return vulnerabilities.get(id);
    }

    public enum AbsorbCrystalType {
        LAST_HIT,
        FULL_PARTY,
        PARTY_ONE_RANDOM
    }
}
