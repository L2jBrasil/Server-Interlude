package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.gameserver.datatables.SkillTable;
import com.l2jbr.gameserver.handler.ISkillHandler;
import com.l2jbr.gameserver.handler.SkillHandler;
import com.l2jbr.gameserver.model.L2Character;
import com.l2jbr.gameserver.model.L2Effect;
import com.l2jbr.gameserver.model.L2Skill;
import com.l2jbr.gameserver.model.actor.instance.L2NpcInstance;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.model.quest.Quest;
import com.l2jbr.gameserver.templates.BodyPart;
import com.l2jbr.gameserver.templates.ItemType;
import com.l2jbr.gameserver.templates.ItemTypeGroup;
import org.springframework.data.annotation.Transient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Table("weapon")
public class Weapon extends ItemTemplate {

    private BodyPart bodypart;
    private Integer soulshots;
    private Integer spiritshots;
    @Column("rnd_dam")
    private Integer rndDam;
    @Column("weaponType")
    private ItemType type;
    @Column("mp_consume")
    private Integer mpConsume;
    @Column("item_skill_id")
    private Integer itemSkillId;
    @Column("item_skill_lvl")
    private Integer itemSkillLvl;

    @Transient private L2Skill skill;
    @Transient protected List<L2Skill> skillsOnCast;
    @Transient protected List<L2Skill> skillsOnCrit;


    @Override
    public void attach(L2Skill skill, String triggerType) {
        switch (triggerType.toLowerCase()) {
            case "oncrit":
                attachOnCrit(skill);
                break;
            case "oncast":
                attachOnCast(skill);
                break;
            default:
                super.attach(skill, triggerType);
        }
    }

    public void attachOnCrit(L2Skill skill) {
        if (isNull(skillsOnCrit)) {
            skillsOnCrit = new ArrayList<>();
        }
        skillsOnCrit.add(skill);
    }

    public void attachOnCast(L2Skill skill) {
        if (isNull(skillsOnCast)) {
            skillsOnCast = new ArrayList<>();
        }
        skillsOnCast.add(skill);
    }


    public List<L2Effect> getSkillEffects(L2Character caster, L2Character target, boolean crit) {
        List<L2Effect> effects = new ArrayList<>();
        if(isNull(skillsOnCrit)) {
            return effects;
        }

        for (L2Skill skill : skillsOnCrit) {
            if (target.isRaid() && ((skill.getSkillType() == L2Skill.SkillType.CONFUSION) || (skill.getSkillType() == L2Skill.SkillType.MUTE) || (skill.getSkillType() == L2Skill.SkillType.PARALYZE) || (skill.getSkillType() == L2Skill.SkillType.ROOT))) {
                continue; // These skills should not work on RaidBoss
            }

            if (!skill.checkCondition(caster, target, true)) {
                continue; // Skill condition not met
            }

            if (target.getFirstEffect(skill.getId()) != null) {
                target.getFirstEffect(skill.getId()).exit();
            }
            for (L2Effect e : skill.getEffects(caster, target)) {
                effects.add(e);
            }
        }
        return effects;
    }

    public List<L2Effect> getSkillEffects(L2Character caster, L2Character target, L2Skill trigger) {
        List<L2Effect> effects = new ArrayList<>();

        if (isNull(skillsOnCast)) {
            return effects;
        }

        for (L2Skill skill : skillsOnCast) {
            if (trigger.isOffensive() != skill.isOffensive()) {
                continue; // Trigger only same type of skill
            }

            if (target.isRaid() && ((skill.getSkillType() == L2Skill.SkillType.CONFUSION) || (skill.getSkillType() == L2Skill.SkillType.MUTE) || (skill.getSkillType() == L2Skill.SkillType.PARALYZE) || (skill.getSkillType() == L2Skill.SkillType.ROOT))) {
                continue; // These skills should not work on RaidBoss
            }

            if (trigger.isToggle() && (skill.getSkillType() == L2Skill.SkillType.BUFF)) {
                continue; // No buffing with toggle skills
            }

            if (!skill.checkCondition(caster, target, true)) {
                continue; // Skill condition not met
            }

            try {
                // Get the skill handler corresponding to the skill type
                ISkillHandler handler = SkillHandler.getInstance().getSkillHandler(skill.getSkillType());

                L2Character[] targets = new L2Character[1];
                targets[0] = target;

                // Launch the magic skill and calculate its effects
                if (handler != null) {
                    handler.useSkill(caster, skill, targets);
                } else {
                    skill.useSkill(caster, targets);
                }

                if ((caster instanceof L2PcInstance) && (target instanceof L2NpcInstance)) {
                    Quest[] quests = ((L2NpcInstance) target).getTemplate().getEventQuests(Quest.QuestEventType.MOB_TARGETED_BY_SKILL);
                    if (quests != null) {
                        for (Quest quest : quests) {
                            quest.notifySkillUse((L2NpcInstance) target, (L2PcInstance) caster, skill);
                        }
                    }
                }
            } catch (IOException e) {
            }
        }

        return effects;
    }


    @Override
    public void onLoad() {
        super.onLoad();

        if(ItemType.NONE == type) {
            type1 = ItemTypeGroup.TYPE1_ARMOR_SHIELD;
            type2 = ItemTypeGroup.TYPE2_SHIELD_ARMOR;
        } else  if (ItemType.PET_WEAPON == type) {
            type1 = ItemTypeGroup.TYPE1_WEAPON_ACCESSORY;

            switch (bodypart) {
                case WOLF:
                    type2 = ItemTypeGroup.TYPE2_PET_WOLF;
                    break;
                case HATCHLING:
                    type2 = ItemTypeGroup.TYPE2_PET_HATCHLING;
                    break;
                case BABYPET:
                    type2 = ItemTypeGroup.TYPE2_PET_BABY;
                    break;
                default:
                    type2 = ItemTypeGroup.TYPE2_PET_STRIDER;
            }
            bodypart = BodyPart.RIGHT_HAND;
        } else {
            type1 = ItemTypeGroup.TYPE1_WEAPON_ACCESSORY;
            type2 = ItemTypeGroup.TYPE2_WEAPON;
        }

        if(! isNull(itemSkillId)) {
            skill = SkillTable.getInstance().getInfo(itemSkillId, itemSkillLvl);
        }
    }

    @Override
    public ItemType getType() {
        return type;
    }

    @Override
    public boolean isStackable() {
        return false;
    }

    @Override
    public boolean isEquipable() {
        return BodyPart.NONE != bodypart;
    }

    @Override
    public BodyPart getBodyPart() {
        return bodypart;
    }

    public int getSoulshots() {
        return soulshots;
    }

    public int getSpiritshots() {
        return spiritshots;
    }

    public int getRandomDamage() {
        return rndDam;
    }

    public int getMpConsume() { return mpConsume; }

    public L2Skill getSkill() {
        return skill;
    }

    public int getAttackReuseDelay() {
        return type == ItemType.BOW ? 1500 : 0;
    }
}
