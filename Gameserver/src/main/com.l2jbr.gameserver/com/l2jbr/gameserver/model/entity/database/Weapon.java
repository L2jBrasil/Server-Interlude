package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.gameserver.handler.ISkillHandler;
import com.l2jbr.gameserver.handler.SkillHandler;
import com.l2jbr.gameserver.model.L2Character;
import com.l2jbr.gameserver.model.L2Effect;
import com.l2jbr.gameserver.model.L2ItemInstance;
import com.l2jbr.gameserver.model.L2Skill;
import com.l2jbr.gameserver.model.actor.instance.L2NpcInstance;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.model.quest.Quest;
import com.l2jbr.gameserver.skills.Env;
import com.l2jbr.gameserver.skills.funcs.Func;
import com.l2jbr.gameserver.skills.funcs.FuncTemplate;
import com.l2jbr.gameserver.templates.BodyPart;
import com.l2jbr.gameserver.templates.ItemType;
import com.l2jbr.gameserver.templates.ItemTypeGroup;
import org.springframework.data.annotation.Transient;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Table("weapon")
public class Weapon extends ItemTemplate {

    private BodyPart bodypart;
    private Integer soulshots;
    private Integer spiritshots;
    @Column("p_dam")
    private Integer pDam;
    @Column("rnd_dam")
    private Integer rndDam;
    @Column("weaponType")
    private ItemType type;
    private Integer critical;
    @Column("hit_modify")
    private Double hitModify;
    @Column("avoid_modify")
    private Integer avoidModify;
    @Column("shield_def")
    private Integer shieldDef;
    @Column("shield_def_rate")
    private Integer shieldDefRate;
    @Column("atk_speed")
    private Integer atkSpeed;
    @Column("mp_consume")
    private Integer mpConsume;
    @Column("m_dam")
    private Integer mDam;
    @Column("item_skill_id")
    private Integer itemSkillId;
    @Column("item_skill_lvl")
    private Integer itemSkillLvl;
    @Column("enchant4_skill_id")
    private Integer enchant4SkillId;
    @Column("enchant4_skill_lvl")
    private Integer enchant4SkillLvl;
    @Column("onCast_skill_id")
    private Integer onCastSkillId;
    @Column("onCast_skill_lvl")
    private Integer onCastSkillLvl;
    @Column("onCast_skill_chance")
    private Integer onCastSkillChance;
    @Column("onCrit_skill_id")
    private Integer onCritSkillId;
    @Column("onCrit_skill_lvl")
    private Integer onCritSkillLvl;
    @Column("onCrit_skill_chance")
    private Integer onCritSkillChance;

    @Transient
    protected L2Skill[] _skillsOnCast;
    @Transient
    protected L2Skill[] _skillsOnCrit;


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
    public BodyPart getBodyPart() {
        return bodypart;
    }

    public int getSoulshots() {
        return soulshots;
    }

    public int getSpiritshots() {
        return spiritshots;
    }

    public int getPDam() {
        return pDam;
    }

    public int getRandomDamage() {
        return rndDam;
    }

    public int getCritical() {
        return critical;
    }

    public double getHitModify() {
        return hitModify;
    }

    public int getAvoidModify() {
        return avoidModify;
    }

    public int getShieldDef() {
        return shieldDef;
    }

    public int getShieldDefRate() {
        return shieldDefRate;
    }

    public int getAtkSpeed() {
        return atkSpeed;
    }

    public int getMpConsume() {
        return mpConsume;
    }

    public int getMDam() {
        return mDam;
    }

    public int getItemSkillId() {
        return itemSkillId;
    }

    public int getItemSkillLevel() {
        return itemSkillLvl;
    }

    public int getEnchant4SkillId() {
        return enchant4SkillId;
    }

    public int getEnchant4SkillLevel() {
        return enchant4SkillLvl;
    }

    public int getOnCastSkillId() {
        return onCastSkillId;
    }

    public int getOnCastSkillLevel() {
        return onCastSkillLvl;
    }

    public int getOnCastSkillChance() {
        return onCastSkillChance;
    }

    public int getOnCritSkillId() {
        return onCritSkillId;
    }

    public int getOnCritSkillLevel() {
        return onCritSkillLvl;
    }

    public int getOnCritSkillChance() {
        return onCritSkillChance;
    }

    public L2Skill getSkill() {
        // todo implement
        return  null;
    }

    public L2Skill getEnchant4Skill() {
        // todo implement
        return  null;
    }

    public int getAttackReuseDelay() {
        return type == ItemType.BOW ? 1500 : 0;
    }

    public L2Effect[] getSkillEffects(L2Character caster, L2Character target, boolean crit) {
        if ((_skillsOnCrit == null) || !crit) {
            return _emptyEffectSet;
        }
        List<L2Effect> effects = new LinkedList<>();

        for (L2Skill skill : _skillsOnCrit) {
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
        if (effects.size() == 0) {
            return _emptyEffectSet;
        }
        return effects.toArray(new L2Effect[effects.size()]);
    }

    public L2Effect[] getSkillEffects(L2Character caster, L2Character target, L2Skill trigger) {
        if (_skillsOnCast == null) {
            return _emptyEffectSet;
        }
        List<L2Effect> effects = new LinkedList<>();

        for (L2Skill skill : _skillsOnCast) {
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
        if (effects.size() == 0) {
            return _emptyEffectSet;
        }
        return effects.toArray(new L2Effect[effects.size()]);
    }

    @Override
    public Func[] getStatFuncs(L2ItemInstance instance, L2Character player) {
        List<Func> funcs = new LinkedList<>();
        if (_funcTemplates != null) {
            for (FuncTemplate t : _funcTemplates) {
                Env env = new Env();
                env.player = player;
                env.item = instance;
                Func f = t.getFunc(env, instance);
                if (f != null) {
                    funcs.add(f);
                }
            }
        }
        return funcs.toArray(new Func[funcs.size()]);
    }
}
