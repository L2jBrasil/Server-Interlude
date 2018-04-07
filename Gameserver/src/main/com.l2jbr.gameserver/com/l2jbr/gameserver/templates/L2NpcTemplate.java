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
package com.l2jbr.gameserver.templates;

import com.l2jbr.gameserver.model.L2DropCategory;
import com.l2jbr.gameserver.model.L2DropData;
import com.l2jbr.gameserver.model.L2MinionData;
import com.l2jbr.gameserver.model.L2Skill;
import com.l2jbr.gameserver.model.base.ClassId;
import com.l2jbr.gameserver.model.quest.Quest;
import com.l2jbr.gameserver.skills.Stats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * This cl contains all generic data of a L2Spawn object.<BR>
 * <BR>
 * <B><U> Data</U> :</B><BR>
 * <BR>
 * <li>npcId, type, name, sex</li> <li>rewardExp, rewardSp</li> <li>aggroRange, factionId, factionRange</li> <li>rhand, lhand, armor</li> <li>isUndead</li> <li>_drops</li> <li>_minions</li> <li>_teachInfo</li> <li>_skills</li> <li>_questsStart</li><BR>
 * <BR>
 *
 * @version $Revision: 1.1.2.4 $ $Date: 2005/04/02 15:57:51 $
 */
public final class L2NpcTemplate extends L2CharTemplate {
    protected static final Logger _log = LoggerFactory.getLogger(Quest.class.getName());

    public final int npcId;
    public final int idTemplate;
    public final String type;
    public final String name;
    public final boolean serverSideName;
    public final String title;
    public final boolean serverSideTitle;
    public final String sex;
    public final byte level;
    public final int rewardExp;
    public final int rewardSp;
    public final int aggroRange;
    public final int rhand;
    public final int lhand;
    public final int armor;
    public final String factionId;
    public final int factionRange;
    public final int absorbLevel;
    public final AbsorbCrystalType absorbType;
    public Race race;

    public static enum AbsorbCrystalType {
        LAST_HIT,
        FULL_PARTY,
        PARTY_ONE_RANDOM
    }

    public static enum Race {
        UNDEAD,
        MAGICCREATURE,
        BEAST,
        ANIMAL,
        PLANT,
        HUMANOID,
        SPIRIT,
        ANGEL,
        DEMON,
        DRAGON,
        GIANT,
        BUG,
        FAIRIE,
        HUMAN,
        ELVE,
        DARKELVE,
        ORC,
        DWARVE,
        OTHER,
        NONLIVING,
        SIEGEWEAPON,
        DEFENDINGARMY,
        MERCENARIE,
        UNKNOWN
    }

    private final StatsSet _npcStatsSet;

    /**
     * The table containing all Item that can be dropped by L2NpcInstance using this L2NpcTemplate
     */
    private final List<L2DropCategory> _categories = new LinkedList<>();

    /**
     * The table containing all Minions that must be spawn with the L2NpcInstance using this L2NpcTemplate
     */
    private final List<L2MinionData> _minions = new LinkedList<>();

    private List<ClassId> _teachInfo;
    private Map<Integer, L2Skill> _skills;
    private Map<Stats, Double> _vulnerabilities;
    // contains a list of quests for each event type (questStart, questAttack, questKill, etc)
    private Map<Quest.QuestEventType, Quest[]> _questEvents;

    /**
     * Constructor of L2Character.<BR>
     * <BR>
     *
     * @param set The StatsSet object to transfert data to the method
     */
    public L2NpcTemplate(StatsSet set) {
        super(set);
        npcId = set.getInteger("npcId");
        idTemplate = set.getInteger("idTemplate");
        type = set.getString("type");
        name = set.getString("name");
        serverSideName = set.getBool("serverSideName");
        title = set.getString("title");
        serverSideTitle = set.getBool("serverSideTitle");
        sex = set.getString("sex");
        level = set.getByte("level");
        rewardExp = set.getInteger("rewardExp");
        rewardSp = set.getInteger("rewardSp");
        aggroRange = set.getInteger("aggroRange");
        rhand = set.getInteger("rhand");
        lhand = set.getInteger("lhand");
        armor = set.getInteger("armor");
        String f = set.getString("factionId", null);
        if (f == null) {
            factionId = null;
        } else {
            factionId = f.intern();
        }
        factionRange = set.getInteger("factionRange");
        absorbLevel = set.getInteger("absorb_level", 0);
        absorbType = AbsorbCrystalType.valueOf(set.getString("absorb_type"));
        race = null;
        _npcStatsSet = set;
        _teachInfo = null;
    }

    public void addTeachInfo(ClassId classId) {
        if (_teachInfo == null) {
            _teachInfo = new LinkedList<>();
        }
        _teachInfo.add(classId);
    }

    public ClassId[] getTeachInfo() {
        if (_teachInfo == null) {
            return null;
        }
        return _teachInfo.toArray(new ClassId[_teachInfo.size()]);
    }

    public boolean canTeach(ClassId classId) {
        if (_teachInfo == null) {
            return false;
        }

        // If the player is on a third class, fetch the class teacher
        // information for its parent class.
        if (classId.getId() >= 88) {
            return _teachInfo.contains(classId.getParent());
        }

        return _teachInfo.contains(classId);
    }

    // add a drop to a given category. If the category does not exist, create it.
    public void addDropData(L2DropData drop, int categoryType) {
        if (drop.isQuestDrop()) {
            // if (_questDrops == null)
            // _questDrops = new List<L2DropData>(0);
            // _questDrops.add(drop);
        } else {
            // if the category doesn't already exist, create it first
            synchronized (_categories) {
                boolean catExists = false;
                for (L2DropCategory cat : _categories) {
                    // if the category exists, add the drop to this category.
                    if (cat.getCategoryType() == categoryType) {
                        cat.addDropData(drop);
                        catExists = true;
                        break;
                    }
                }
                // if the category doesn't exit, create it and add the drop
                if (!catExists) {
                    L2DropCategory cat = new L2DropCategory(categoryType);
                    cat.addDropData(drop);
                    _categories.add(cat);
                }
            }
        }
    }

    public void addRaidData(L2MinionData minion) {
        _minions.add(minion);
    }

    public void addSkill(L2Skill skill) {
        if (_skills == null) {
            _skills = new LinkedHashMap<>();
        }
        _skills.put(skill.getId(), skill);
    }

    public void addVulnerability(Stats id, double vuln) {
        if (_vulnerabilities == null) {
            _vulnerabilities = new LinkedHashMap<>();
        }
        _vulnerabilities.put(id, vuln);
    }

    public double getVulnerability(Stats id) {
        if ((_vulnerabilities == null) || (_vulnerabilities.get(id) == null)) {
            return 1;
        }
        return _vulnerabilities.get(id);
    }

    public double removeVulnerability(Stats id) {
        return _vulnerabilities.remove(id);
    }

    /**
     * Return the list of all possible UNCATEGORIZED drops of this L2NpcTemplate.<BR>
     * <BR>
     *
     * @return
     */
    public List<L2DropCategory> getDropData() {
        return _categories;
    }

    /**
     * Return the list of all possible item drops of this L2NpcTemplate.<BR>
     * (ie full drops and part drops, mats, miscellaneous & UNCATEGORIZED)<BR>
     * <BR>
     *
     * @return
     */
    public List<L2DropData> getAllDropData() {
        List<L2DropData> lst = new LinkedList<>();
        for (L2DropCategory tmp : _categories) {
            lst.addAll(tmp.getAllDrops());
        }
        return lst;
    }

    /**
     * Empty all possible drops of this L2NpcTemplate.<BR>
     * <BR>
     */
    public synchronized void clearAllDropData() {
        _categories.forEach( c -> c.clearAllDrops());
        _categories.clear();
    }

    /**
     * Return the list of all Minions that must be spawn with the L2NpcInstance using this L2NpcTemplate.<BR>
     * <BR>
     *
     * @return
     */
    public List<L2MinionData> getMinionData() {
        return _minions;
    }

    public Map<Integer, L2Skill> getSkills() {
        return _skills;
    }

    public void addQuestEvent(Quest.QuestEventType EventType, Quest q) {
        if (_questEvents == null) {
            _questEvents = new LinkedHashMap<>();
        }

        if (_questEvents.get(EventType) == null) {
            _questEvents.put(EventType, new Quest[]
                    {
                            q
                    });
        } else {
            Quest[] _quests = _questEvents.get(EventType);
            int len = _quests.length;

            // if only one registration per npc is allowed for this event type
            // then only register this NPC if not already registered for the specified event.
            // if a quest allows multiple registrations, then register regardless of count
            // In all cases, check if this new registration is replacing an older copy of the SAME quest
            if (!EventType.isMultipleRegistrationAllowed()) {
                if (_quests[0].getName().equals(q.getName())) {
                    _quests[0] = q;
                } else {
                    _log.warn("Quest event not allowed in multiple quests.  Skipped addition of Event Type \"" + EventType + "\" for NPC \"" + name + "\" and quest \"" + q.getName() + "\".");
                }
            } else {
                // be ready to add a new quest to a new copy of the list, with larger size than previously.
                Quest[] tmp = new Quest[len + 1];
                // loop through the existing quests and copy them to the new list. While doing so, also
                // check if this new quest happens to be just a replacement for a previously loaded quest.
                // If so, just save the updated reference and do NOT use the new list. Else, add the new
                // quest to the end of the new list
                for (int i = 0; i < len; i++) {
                    if (_quests[i].getName().equals(q.getName())) {
                        _quests[i] = q;
                        return;
                    }
                    tmp[i] = _quests[i];
                }
                tmp[len] = q;
                _questEvents.put(EventType, tmp);
            }
        }
    }

    public Quest[] getEventQuests(Quest.QuestEventType EventType) {
        if (_questEvents == null) {
            return null;
        }
        return _questEvents.get(EventType);
    }

    public StatsSet getStatsSet() {
        return _npcStatsSet;
    }

    public void setRace(int raceId) {
        switch (raceId) {
            case 1:
                race = Race.UNDEAD;
                break;
            case 2:
                race = Race.MAGICCREATURE;
                break;
            case 3:
                race = Race.BEAST;
                break;
            case 4:
                race = Race.ANIMAL;
                break;
            case 5:
                race = Race.PLANT;
                break;
            case 6:
                race = Race.HUMANOID;
                break;
            case 7:
                race = Race.SPIRIT;
                break;
            case 8:
                race = Race.ANGEL;
                break;
            case 9:
                race = Race.DEMON;
                break;
            case 10:
                race = Race.DRAGON;
                break;
            case 11:
                race = Race.GIANT;
                break;
            case 12:
                race = Race.BUG;
                break;
            case 13:
                race = Race.FAIRIE;
                break;
            case 14:
                race = Race.HUMAN;
                break;
            case 15:
                race = Race.ELVE;
                break;
            case 16:
                race = Race.DARKELVE;
                break;
            case 17:
                race = Race.ORC;
                break;
            case 18:
                race = Race.DWARVE;
                break;
            case 19:
                race = Race.OTHER;
                break;
            case 20:
                race = Race.NONLIVING;
                break;
            case 21:
                race = Race.SIEGEWEAPON;
                break;
            case 22:
                race = Race.DEFENDINGARMY;
                break;
            case 23:
                race = Race.MERCENARIE;
                break;
            default:
                race = Race.UNKNOWN;
                break;
        }
    }

    public Race getRace() {
        if (race == null) {
            race = Race.UNKNOWN;
        }

        return race;
    }
}
