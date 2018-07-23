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

import com.l2jbr.commons.Config;
import com.l2jbr.gameserver.model.L2Character;
import com.l2jbr.gameserver.model.L2Effect;
import com.l2jbr.gameserver.model.L2ItemInstance;
import com.l2jbr.gameserver.model.L2Skill;
import com.l2jbr.gameserver.skills.Env;
import com.l2jbr.gameserver.skills.effects.EffectTemplate;
import com.l2jbr.gameserver.skills.funcs.Func;
import com.l2jbr.gameserver.skills.funcs.FuncTemplate;

import java.util.LinkedList;
import java.util.List;

/**
 * This class contains all informations concerning the item (weapon, armor, etc).<BR>
 * Mother class of : <LI>Armor</LI> <LI>L2EtcItem</LI> <LI>Weapon</LI>
 *
 * @version $Revision: 1.7.2.2.2.5 $ $Date: 2005/04/06 18:25:18 $
 */
public abstract class L2Item {

    private final int _itemId;
    private final String _name;
    private final ItemTypeGroup _type1; // needed for item list (inventory)
    private final ItemTypeGroup _type2; // different lists for armor, weapon, etc
    private final int _weight;
    private final boolean _crystallizable;
    private final boolean _stackable;
    private final int _materialType = 0;
    private final CrystalType _crystalType; // default to none-grade
    private final int _duration;
    private final BodyPart _bodyPart;
    private final int _referencePrice;
    private final int _crystalCount;
    private final boolean _sellable;
    private final boolean _dropable;
    private final boolean _destroyable;
    private final boolean _tradeable;

    protected final Enum<?> _type;

    protected FuncTemplate[] _funcTemplates;
    protected EffectTemplate[] _effectTemplates;
    protected L2Skill[] _skills;

    private static final Func[] _emptyFunctionSet = new Func[0];
    protected static final L2Effect[] _emptyEffectSet = new L2Effect[0];

    /**
     * Constructor of the L2Item that fill class variables.<BR>
     * <BR>
     * <U><I>Variables filled :</I></U><BR>
     * <LI>type</LI> <LI>_itemId</LI> <LI>_name</LI> <LI>_type1 & _type2</LI> <LI>_weight</LI> <LI>_crystallizable</LI> <LI>_stackable</LI> <LI>_materialType & _crystalType & _crystlaCount</LI> <LI>_duration</LI> <LI>_bodypart</LI> <LI>_referencePrice</LI> <LI>_sellable</LI>
     *
     * @param type : Enum designating the type of the item
     * @param set  : StatsSet corresponding to a set of couples (key,value) for description of the item
     */
    protected L2Item(Enum<?> type, StatsSet set) {
        _type = type;
        _itemId = set.getInteger("item_id");
        _name = set.getString("name");
        _type1 = set.getEnum("type1", ItemTypeGroup.class); // needed for item list (inventory)
        _type2 = set.getEnum("type2", ItemTypeGroup.class); // different lists for armor, weapon, etc
        _weight = set.getInteger("weight");
        _crystallizable = set.getBool("crystallizable");
        _stackable = set.getBool("stackable", false);
        _crystalType = set.getEnum("crystal_type", CrystalType.class); // default to none-grade
        _duration = set.getInteger("duration");
        _bodyPart = set.getEnum("bodypart", BodyPart.class);
        _referencePrice = set.getInteger("price");
        _crystalCount = set.getInteger("crystal_count", 0);
        _sellable = set.getBool("sellable", true);
        _dropable = set.getBool("dropable", true);
        _destroyable = set.getBool("destroyable", true);
        _tradeable = set.getBool("tradeable", true);
    }

    /**
     * Returns the itemType.
     *
     * @return Enum
     */
    public Enum<?> getItemType() {
        return _type;
    }

    /**
     * Returns the duration of the item
     *
     * @return int
     */
    public final int getDuration() {
        return _duration;
    }

    /**
     * Returns the ID of the iden
     *
     * @return int
     */
    public final int getItemId() {
        return _itemId;
    }

    public abstract int getItemMask();

    /**
     * Return the type of material of the item
     *
     * @return int
     */
    public final int getMaterialType() {
        return _materialType;
    }

    /**
     * Returns the type 2 of the item
     *
     * @return int
     */
    public final ItemTypeGroup getType2() {
        return _type2;
    }

    /**
     * Returns the weight of the item
     *
     * @return int
     */
    public final int getWeight() {
        return _weight;
    }

    /**
     * Returns if the item is crystallizable
     *
     * @return boolean
     */
    public final boolean isCrystallizable() {
        return _crystallizable;
    }

    /**
     * Return the type of crystal if item is crystallizable
     *
     * @return int
     */
    public final CrystalType getCrystalType() {
        return _crystalType;
    }

    /**
     * Return the type of crystal if item is crystallizable
     *
     * @return int
     */
    public final int getCrystalItemId() {
        return _crystalType.getItemId();
    }


    /**
     * Returns the quantity of crystals for crystallization
     *
     * @return int
     */
    public final int getCrystalCount() {
        return _crystalCount;
    }

    /**
     * Returns the quantity of crystals for crystallization on specific enchant level
     *
     * @param enchantLevel
     * @return int
     */
    public final int getCrystalCount(int enchantLevel) {
        if (enchantLevel > 3) {
            switch (_type2) {
                case TYPE2_SHIELD_ARMOR:
                case TYPE2_ACCESSORY:
                    return _crystalCount + (_crystalType.getEnchantAddArmor() * ((3 * enchantLevel) - 6));
                case TYPE2_WEAPON:
                    return _crystalCount + (_crystalType.getEnchantAddWeapon() * ((2 * enchantLevel) - 3));
                default:
                    return _crystalCount;
            }
        } else if (enchantLevel > 0) {
            switch (_type2) {
                case TYPE2_SHIELD_ARMOR:
                case TYPE2_ACCESSORY:
                    return _crystalCount + (_crystalType.getEnchantAddArmor() * enchantLevel);
                case TYPE2_WEAPON:
                    return _crystalCount + (_crystalType.getEnchantAddWeapon() * enchantLevel);
                default:
                    return _crystalCount;
            }
        } else {
            return _crystalCount;
        }
    }

    /**
     * Returns the name of the item
     *
     * @return String
     */
    public final String getName() {
        return _name;
    }

    /**
     * Return the part of the body used with the item.
     *
     * @return int
     */
    public final BodyPart getBodyPart() {
        return _bodyPart;
    }

    /**
     * Returns the type 1 of the item
     *
     * @return int
     */
    public final ItemTypeGroup getType1() {
        return _type1;
    }

    /**
     * Returns if the item is stackable
     *
     * @return boolean
     */
    public final boolean isStackable() {
        return _stackable;
    }

    /**
     * Returns if the item is consumable
     *
     * @return boolean
     */
    public boolean isConsumable() {
        return false;
    }

    /**
     * Returns the price of reference of the item
     *
     * @return int
     */
    public final int getReferencePrice() {
        return (isConsumable() ? (int) (_referencePrice * Config.RATE_CONSUMABLE_COST) : _referencePrice);
    }

    /**
     * Returns if the item can be sold
     *
     * @return boolean
     */
    public final boolean isSellable() {
        return _sellable;
    }

    /**
     * Returns if the item can dropped
     *
     * @return boolean
     */
    public final boolean isDropable() {
        return _dropable;
    }

    /**
     * Returns if the item can destroy
     *
     * @return boolean
     */
    public final boolean isDestroyable() {
        return _destroyable;
    }

    /**
     * Returns if the item can add to trade
     *
     * @return boolean
     */
    public final boolean isTradeable() {
        return _tradeable;
    }

    /**
     * Returns if item is for hatchling
     *
     * @return boolean
     */
    public boolean isForHatchling() {
        return (_type2 == ItemTypeGroup.TYPE2_PET_HATCHLING);
    }

    /**
     * Returns if item is for strider
     *
     * @return boolean
     */
    public boolean isForStrider() {
        return (_type2 == ItemTypeGroup.TYPE2_PET_STRIDER);
    }

    /**
     * Returns if item is for wolf
     *
     * @return boolean
     */
    public boolean isForWolf() {
        return (_type2 == ItemTypeGroup.TYPE2_PET_WOLF);
    }

    /**
     * Returns if item is for wolf
     *
     * @return boolean
     */
    public boolean isForBabyPet() {
        return (_type2 == ItemTypeGroup.TYPE2_PET_BABY);
    }

    /**
     * Returns array of Func objects containing the list of functions used by the item
     *
     * @param instance : L2ItemInstance pointing out the item
     * @param player   : L2Character pointing out the player
     * @return Func[] : array of functions
     */
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

    /**
     * Returns the effects associated with the item.
     *
     * @param instance : L2ItemInstance pointing out the item
     * @param player   : L2Character pointing out the player
     * @return L2Effect[] : array of effects generated by the item
     */
    public L2Effect[] getEffects(L2ItemInstance instance, L2Character player) {
        if (_effectTemplates == null) {
            return _emptyEffectSet;
        }
        List<L2Effect> effects = new LinkedList<>();
        for (EffectTemplate et : _effectTemplates) {
            Env env = new Env();
            env.player = player;
            env.target = player;
            env.item = instance;
            L2Effect e = et.getEffect(env);
            if (e != null) {
                effects.add(e);
            }
        }
        if (effects.size() == 0) {
            return _emptyEffectSet;
        }
        return effects.toArray(new L2Effect[effects.size()]);
    }

    /**
     * Returns effects of skills associated with the item.
     *
     * @param caster : L2Character pointing out the caster
     * @param target : L2Character pointing out the target
     * @return L2Effect[] : array of effects generated by the skill
     */
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

    /**
     * Add the FuncTemplate f to the list of functions used with the item
     *
     * @param f : FuncTemplate to add
     */
    public void attach(FuncTemplate f) {
        // If _functTemplates is empty, create it and add the FuncTemplate f in it
        if (_funcTemplates == null) {
            _funcTemplates = new FuncTemplate[]
                    {
                            f
                    };
        } else {
            int len = _funcTemplates.length;
            FuncTemplate[] tmp = new FuncTemplate[len + 1];
            // Definition : arraycopy(array source, begins copy at this position of source, array destination, begins copy at this position in dest,
            // number of components to be copied)
            System.arraycopy(_funcTemplates, 0, tmp, 0, len);
            tmp[len] = f;
            _funcTemplates = tmp;
        }
    }

    /**
     * Add the EffectTemplate effect to the list of effects generated by the item
     *
     * @param effect : EffectTemplate
     */
    public void attach(EffectTemplate effect) {
        if (_effectTemplates == null) {
            _effectTemplates = new EffectTemplate[]
                    {
                            effect
                    };
        } else {
            int len = _effectTemplates.length;
            EffectTemplate[] tmp = new EffectTemplate[len + 1];
            // Definition : arraycopy(array source, begins copy at this position of source, array destination, begins copy at this position in dest,
            // number of components to be copied)
            System.arraycopy(_effectTemplates, 0, tmp, 0, len);
            tmp[len] = effect;
            _effectTemplates = tmp;
        }
    }

    /**
     * Add the L2Skill skill to the list of skills generated by the item
     *
     * @param skill : L2Skill
     */
    public void attach(L2Skill skill) {
        if (_skills == null) {
            _skills = new L2Skill[]
                    {
                            skill
                    };
        } else {
            int len = _skills.length;
            L2Skill[] tmp = new L2Skill[len + 1];
            // Definition : arraycopy(array source, begins copy at this position of source, array destination, begins copy at this position in dest,
            // number of components to be copied)
            System.arraycopy(_skills, 0, tmp, 0, len);
            tmp[len] = skill;
            _skills = tmp;
        }
    }

    /**
     * Returns the name of the item
     *
     * @return String
     */
    @Override
    public String toString() {
        return _name;
    }
}
