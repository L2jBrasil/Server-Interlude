package com.l2jbr.gameserver.model.database;

import com.l2jbr.commons.Config;
import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.model.Entity;
import com.l2jbr.gameserver.model.L2Character;
import com.l2jbr.gameserver.model.L2Effect;
import com.l2jbr.gameserver.model.L2ItemInstance;
import com.l2jbr.gameserver.model.L2Skill;
import com.l2jbr.gameserver.skills.Env;
import com.l2jbr.gameserver.skills.funcs.Func;
import com.l2jbr.gameserver.skills.funcs.FuncTemplate;
import com.l2jbr.gameserver.templates.BodyPart;
import com.l2jbr.gameserver.templates.CrystalType;
import com.l2jbr.gameserver.templates.ItemType;
import com.l2jbr.gameserver.templates.ItemTypeGroup;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.util.LinkedList;
import java.util.List;

public abstract class ItemTemplate extends Entity<Integer> {

    @Id
    @Column("item_id")
    private int id;
    private String name;
    private boolean crystallizable;
    private int weight;
    @Column("crystal_type")
    private CrystalType crystalType;
    private int duration;
    private int price;
    @Column("crystal_count")
    private int crystalCount;
    private boolean sellable;
    private boolean dropable;
    private boolean destroyable;
    private boolean tradeable;

    @Transient
    protected ItemTypeGroup type1;
    @Transient
    protected ItemTypeGroup type2;
    @Transient
    protected FuncTemplate[] _funcTemplates;
    @Transient
    private static final Func[] _emptyFunctionSet = new Func[0];
    @Transient
    protected L2Skill[] _skills;
    @Transient
    protected static final L2Effect[] _emptyEffectSet = new L2Effect[0];


    public Func[] getStatFuncs(L2ItemInstance instance, L2Character player) {
        if (_funcTemplates == null) {
            return _emptyFunctionSet;
        }
        List<Func> funcs = new LinkedList<>();
        for (FuncTemplate t : _funcTemplates) {
            Env env = new Env();
            env.player = player;
            env.target = player;
            env.item = instance;
            Func f = t.getFunc(env, this); // skill is owner
            if (f != null) {
                funcs.add(f);
            }
        }
        if (funcs.size() == 0) {
            return _emptyFunctionSet;
        }
        return funcs.toArray(new Func[funcs.size()]);
    }

    public L2Effect[] getSkillEffects(L2Character caster, L2Character target) {
        if (_skills == null) {
            return _emptyEffectSet;
        }
        List<L2Effect> effects = new LinkedList<>();

        for (L2Skill skill : _skills) {
            if (!skill.checkCondition(caster, target, true)) {
                continue; // Skill condition not met
            }

            if (target.getFirstEffect(skill.getId()) != null) {
                target.removeEffect(target.getFirstEffect(skill.getId()));
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

    @Override
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isCrystallizable() {
        return crystallizable;
    }

    public int getWeight() {
        return weight;
    }

    public CrystalType getCrystalType() {
        return crystalType;
    }

    public int getDuration() {
        return duration;
    }

    public int getPrice() {
        if(isConsumable() ) {
            return (int) (price * Config.RATE_CONSUMABLE_COST);
        }
        return price;
    }

    public boolean isConsumable() {
        return ItemType.SHOT == getType() || ItemType.POTION == getType();
    }

    public int getCrystalCount() {
        return crystalCount;
    }

    public boolean isSellable() { return sellable; }

    public boolean isDropable() {
        return dropable;
    }

    public boolean isDestroyable() {
        return destroyable;
    }

    public boolean isTradeable() {
        return tradeable;
    }

    public final int getCrystalCount(int enchantLevel) {
        if(enchantLevel == 0) {
            return  crystalCount;
        }
        return crystalCount + getCrystalBonusForEnchant(enchantLevel);
    }

    private int getCrystalBonusForEnchant(int enchantLevel) {
        if(enchantLevel <= 0) {
            return 0;
        }

        int bonusEnchant = 0;
        int mult = 1;
        switch (type2) {
            case TYPE2_SHIELD_ARMOR:
            case TYPE2_ACCESSORY:
                bonusEnchant = crystalType.getEnchantAddArmor();
                mult = 3 * enchantLevel - 6;
                break;
            case TYPE2_WEAPON:
                bonusEnchant = crystalType.getEnchantAddWeapon();
                mult = 2 * enchantLevel - 3;
        }

        if(enchantLevel > 3) {
            return bonusEnchant * mult;
        }
       return bonusEnchant * enchantLevel;
    }

    public int getItemMask() {
        return getType().mask();
    }

    public ItemTypeGroup getType1() {
        return type1;
    }

    public ItemTypeGroup getType2() {
        return type2;
    }

    public abstract BodyPart getBodyPart();
    public abstract ItemType getType();
    public abstract boolean isStackable();


    public boolean isForWolf() {
        return type2 == ItemTypeGroup.TYPE2_PET_WOLF;
    }

    public boolean isForHatchling() {
        return type2 == ItemTypeGroup.TYPE2_PET_HATCHLING;
    }

    public boolean isForStrider() {
        return type2 == ItemTypeGroup.TYPE2_PET_STRIDER;
    }

    public boolean isForBabyPet() {
        return type2 == ItemTypeGroup.TYPE2_PET_BABY;
    }
}
