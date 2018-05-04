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
import com.l2jbr.gameserver.Item;
import com.l2jbr.gameserver.ThreadPoolManager;
import com.l2jbr.gameserver.idfactory.IdFactory;
import com.l2jbr.gameserver.model.*;
import com.l2jbr.gameserver.model.L2ItemInstance.ItemLocation;
import com.l2jbr.gameserver.model.actor.instance.L2BossInstance;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.model.actor.instance.L2RaidBossInstance;
import com.l2jbr.gameserver.model.database.Armor;
import com.l2jbr.gameserver.model.database.EtcItem;
import com.l2jbr.gameserver.model.database.Weapon;
import com.l2jbr.gameserver.model.database.repository.ArmorRepository;
import com.l2jbr.gameserver.model.database.repository.EtcItemRepository;
import com.l2jbr.gameserver.model.database.repository.PetsRepository;
import com.l2jbr.gameserver.model.database.repository.WeaponRepository;
import com.l2jbr.gameserver.skills.SkillsEngine;
import com.l2jbr.gameserver.templates.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;


/**
 * This class ...
 *
 * @version $Revision: 1.9.2.6.2.9 $ $Date: 2005/04/02 15:57:34 $
 */
public class ItemTable {
    private static Logger _log = LoggerFactory.getLogger(ItemTable.class.getName());
    private static Logger _logItems = LoggerFactory.getLogger("item");

    private static final Map<String, Integer> _materials = new LinkedHashMap<>();
    private static final Map<String, Integer> _crystalTypes = new LinkedHashMap<>();
    private static final Map<String, L2WeaponType> _weaponTypes = new LinkedHashMap<>();
    private static final Map<String, L2ArmorType> _armorTypes = new LinkedHashMap<>();
    private static final Map<String, Integer> _slots = new LinkedHashMap<>();

    private L2Item[] _allTemplates;
    private final Map<Integer, L2EtcItem> _etcItems;
    private final Map<Integer, L2Armor> _armors;
    private final Map<Integer, L2Weapon> _weapons;

    private final boolean _initialized = true;

    static {
        _materials.put("paper", L2Item.MATERIAL_PAPER);
        _materials.put("wood", L2Item.MATERIAL_WOOD);
        _materials.put("liquid", L2Item.MATERIAL_LIQUID);
        _materials.put("cloth", L2Item.MATERIAL_CLOTH);
        _materials.put("leather", L2Item.MATERIAL_LEATHER);
        _materials.put("horn", L2Item.MATERIAL_HORN);
        _materials.put("bone", L2Item.MATERIAL_BONE);
        _materials.put("bronze", L2Item.MATERIAL_BRONZE);
        _materials.put("fine_steel", L2Item.MATERIAL_FINE_STEEL);
        _materials.put("cotton", L2Item.MATERIAL_FINE_STEEL);
        _materials.put("mithril", L2Item.MATERIAL_MITHRIL);
        _materials.put("silver", L2Item.MATERIAL_SILVER);
        _materials.put("gold", L2Item.MATERIAL_GOLD);
        _materials.put("adamantaite", L2Item.MATERIAL_ADAMANTAITE);
        _materials.put("steel", L2Item.MATERIAL_STEEL);
        _materials.put("oriharukon", L2Item.MATERIAL_ORIHARUKON);
        _materials.put("blood_steel", L2Item.MATERIAL_BLOOD_STEEL);
        _materials.put("crystal", L2Item.MATERIAL_CRYSTAL);
        _materials.put("damascus", L2Item.MATERIAL_DAMASCUS);
        _materials.put("chrysolite", L2Item.MATERIAL_CHRYSOLITE);
        _materials.put("scale_of_dragon", L2Item.MATERIAL_SCALE_OF_DRAGON);
        _materials.put("dyestuff", L2Item.MATERIAL_DYESTUFF);
        _materials.put("cobweb", L2Item.MATERIAL_COBWEB);
        _materials.put("seed", L2Item.MATERIAL_SEED);

        _crystalTypes.put("s", L2Item.CRYSTAL_S);
        _crystalTypes.put("a", L2Item.CRYSTAL_A);
        _crystalTypes.put("b", L2Item.CRYSTAL_B);
        _crystalTypes.put("c", L2Item.CRYSTAL_C);
        _crystalTypes.put("d", L2Item.CRYSTAL_D);
        _crystalTypes.put("none", L2Item.CRYSTAL_NONE);

        _weaponTypes.put("blunt", L2WeaponType.BLUNT);
        _weaponTypes.put("bow", L2WeaponType.BOW);
        _weaponTypes.put("dagger", L2WeaponType.DAGGER);
        _weaponTypes.put("dual", L2WeaponType.DUAL);
        _weaponTypes.put("dualfist", L2WeaponType.DUALFIST);
        _weaponTypes.put("etc", L2WeaponType.ETC);
        _weaponTypes.put("fist", L2WeaponType.FIST);
        _weaponTypes.put("none", L2WeaponType.NONE); // these are shields !
        _weaponTypes.put("pole", L2WeaponType.POLE);
        _weaponTypes.put("sword", L2WeaponType.SWORD);
        _weaponTypes.put("bigsword", L2WeaponType.BIGSWORD); // Two-Handed Swords
        _weaponTypes.put("pet", L2WeaponType.PET); // Pet Weapon
        _weaponTypes.put("rod", L2WeaponType.ROD); // Fishing Rods
        _weaponTypes.put("bigblunt", L2WeaponType.BIGBLUNT); // Two handed blunt
        _armorTypes.put("none", L2ArmorType.NONE);
        _armorTypes.put("light", L2ArmorType.LIGHT);
        _armorTypes.put("heavy", L2ArmorType.HEAVY);
        _armorTypes.put("magic", L2ArmorType.MAGIC);
        _armorTypes.put("pet", L2ArmorType.PET);

        _slots.put("chest", L2Item.SLOT_CHEST);
        _slots.put("fullarmor", L2Item.SLOT_FULL_ARMOR);
        _slots.put("head", L2Item.SLOT_HEAD);
        _slots.put("hair", L2Item.SLOT_HAIR);
        _slots.put("face", L2Item.SLOT_FACE);
        _slots.put("dhair", L2Item.SLOT_DHAIR);
        _slots.put("underwear", L2Item.SLOT_UNDERWEAR);
        _slots.put("back", L2Item.SLOT_BACK);
        _slots.put("neck", L2Item.SLOT_NECK);
        _slots.put("legs", L2Item.SLOT_LEGS);
        _slots.put("feet", L2Item.SLOT_FEET);
        _slots.put("gloves", L2Item.SLOT_GLOVES);
        _slots.put("chest,legs", L2Item.SLOT_CHEST | L2Item.SLOT_LEGS);
        _slots.put("rhand", L2Item.SLOT_R_HAND);
        _slots.put("lhand", L2Item.SLOT_L_HAND);
        _slots.put("lrhand", L2Item.SLOT_LR_HAND);
        _slots.put("rear,lear", L2Item.SLOT_R_EAR | L2Item.SLOT_L_EAR);
        _slots.put("rfinger,lfinger", L2Item.SLOT_R_FINGER | L2Item.SLOT_L_FINGER);
        _slots.put("none", L2Item.SLOT_NONE);
        _slots.put("wolf", L2Item.SLOT_WOLF); // for wolf
        _slots.put("hatchling", L2Item.SLOT_HATCHLING); // for hatchling
        _slots.put("strider", L2Item.SLOT_STRIDER); // for strider
        _slots.put("babypet", L2Item.SLOT_BABYPET); // for babypet
    }

    private static ItemTable _instance;

    /**
     * List of etcItem
     */
    private static final Map<Integer, Item> itemData = new LinkedHashMap<>();
    /**
     * List of weapons
     */
    private static final Map<Integer, Item> weaponData = new LinkedHashMap<>();
    /**
     * List of armor
     */
    private static final Map<Integer, Item> armorData = new LinkedHashMap<>();

    /**
     * Returns instance of ItemTable
     *
     * @return ItemTable
     */
    public static ItemTable getInstance() {
        if (_instance == null) {
            _instance = new ItemTable();
        }
        return _instance;
    }

    /**
     * Returns a new object Item
     *
     * @return
     */
    public Item newItem() {
        return new Item();
    }

    /**
     * Constructor.
     */
    public ItemTable() {
        _etcItems = new LinkedHashMap<>();
        _armors = new LinkedHashMap<>();
        _weapons = new LinkedHashMap<>();

        EtcItemRepository etcItemRepository = DatabaseAccess.getRepository(EtcItemRepository.class);
        etcItemRepository.findAll().forEach(item -> {
            Item newItem = readItem(item);
            itemData.put(newItem.id, newItem);
        });

        ArmorRepository armorRepository = DatabaseAccess.getRepository(ArmorRepository.class);
        armorRepository.findAll().forEach( armor -> {
            Item newItem = readArmor(armor);
            armorData.put(newItem.id, newItem);
        });

        WeaponRepository weaponRepository = DatabaseAccess.getRepository(WeaponRepository.class);
        weaponRepository.findAll().forEach(weapon -> {
            Item newItem = readWeapon(weapon);
            weaponData.put(newItem.id, newItem);
        });


        for (L2Armor armor : SkillsEngine.getInstance().loadArmors(armorData)) {
            _armors.put(armor.getItemId(), armor);
        }
        _log.info("ItemTable: Loaded " + _armors.size() + " Armors.");

        for (L2EtcItem item : SkillsEngine.getInstance().loadItems(itemData)) {
            _etcItems.put(item.getItemId(), item);
        }
        _log.info("ItemTable: Loaded " + _etcItems.size() + " Items.");

        for (L2Weapon weapon : SkillsEngine.getInstance().loadWeapons(weaponData)) {
            _weapons.put(weapon.getItemId(), weapon);
        }
        _log.info("ItemTable: Loaded " + _weapons.size() + " Weapons.");

        buildFastLookupTable();
    }

    /**
     * Returns object Item from the record of the database
     *
     * @param weapon : ResultSet designating a record of the [weapon] table of database
     * @return Item : object created from the database record
     * @throws SQLException
     */
    private Item readWeapon(Weapon weapon) {
        Item item = new Item();
        item.set = new StatsSet();
        item.type = _weaponTypes.get(weapon.getType());
        item.id = weapon.getId();
        item.name = weapon.getName();

        item.set.set("item_id", item.id);
        item.set.set("name", item.name);

        // lets see if this is a shield
        if (item.type == L2WeaponType.NONE) {
            item.set.set("type1", L2Item.TYPE1_SHIELD_ARMOR);
            item.set.set("type2", L2Item.TYPE2_SHIELD_ARMOR);
        } else {
            item.set.set("type1", L2Item.TYPE1_WEAPON_RING_EARRING_NECKLACE);
            item.set.set("type2", L2Item.TYPE2_WEAPON);
        }

        item.set.set("bodypart", _slots.get(weapon.getBodyPart()));
        item.set.set("material", _materials.get(weapon.getMaterial()));
        item.set.set("crystal_type", _crystalTypes.get(weapon.getCrystalType()));
        item.set.set("crystallizable", Boolean.valueOf(weapon.getCrystallizable()));
        item.set.set("weight", weapon.getWeight());
        item.set.set("soulshots", weapon.getSoulshots());
        item.set.set("spiritshots", weapon.getSpiritshots());
        item.set.set("p_dam", weapon.getPDam());
        item.set.set("rnd_dam", weapon.getRndDam());
        item.set.set("critical", weapon.getCritical());
        item.set.set("hit_modify", weapon.getHitModify());
        item.set.set("avoid_modify", weapon.getAvoidModify());
        item.set.set("shield_def", weapon.getShieldDef());
        item.set.set("shield_def_rate", weapon.getShieldDefRate());
        item.set.set("atk_speed", weapon.getAtkSpeed());
        item.set.set("mp_consume", weapon.getMpConsume());
        item.set.set("m_dam", weapon.getMDam());
        item.set.set("duration", weapon.getDuration());
        item.set.set("price", weapon.getPrice());
        item.set.set("crystal_count", weapon.getCrystalCount());
        item.set.set("sellable", Boolean.valueOf(weapon.getSellable()));
        item.set.set("dropable", Boolean.valueOf(weapon.getDropable()));
        item.set.set("destroyable", Boolean.valueOf(weapon.getDestroyable()));
        item.set.set("tradeable", Boolean.valueOf(weapon.getTradeable()));

        item.set.set("item_skill_id", weapon.getItemSkillId());
        item.set.set("item_skill_lvl", weapon.getItemSkillLevel());

        item.set.set("enchant4_skill_id", weapon.getEnchant4SkillId());
        item.set.set("enchant4_skill_lvl", weapon.getEnchant4SkillLevel());

        item.set.set("onCast_skill_id", weapon.getOnCastSkillId());
        item.set.set("onCast_skill_lvl", weapon.getOnCastSkillLevel());
        item.set.set("onCast_skill_chance", weapon.getOnCastSkillChance());

        item.set.set("onCrit_skill_id", weapon.getOnCritSkillId());
        item.set.set("onCrit_skill_lvl", weapon.getOnCritSkillLevel());
        item.set.set("onCrit_skill_chance", weapon.getOnCritSkillChance());

        if (item.type == L2WeaponType.PET) {
            item.set.set("type1", L2Item.TYPE1_WEAPON_RING_EARRING_NECKLACE);
            if (item.set.getInteger("bodypart") == L2Item.SLOT_WOLF) {
                item.set.set("type2", L2Item.TYPE2_PET_WOLF);
            } else if (item.set.getInteger("bodypart") == L2Item.SLOT_HATCHLING) {
                item.set.set("type2", L2Item.TYPE2_PET_HATCHLING);
            } else if (item.set.getInteger("bodypart") == L2Item.SLOT_BABYPET) {
                item.set.set("type2", L2Item.TYPE2_PET_BABY);
            } else {
                item.set.set("type2", L2Item.TYPE2_PET_STRIDER);
            }

            item.set.set("bodypart", L2Item.SLOT_R_HAND);
        }

        return item;
    }

    /**
     * Returns object Item from the record of the database
     *
     * @param rset : ResultSet designating a record of the [armor] table of database
     * @return Item : object created from the database record
     * @throws SQLException
     */
    private Item readArmor(Armor rset) {
        Item item = new Item();
        item.set = new StatsSet();
        item.type = _armorTypes.get(rset.getArmorType());
        item.id = rset.getId();
        item.name = rset.getName();

        item.set.set("item_id", item.id);
        item.set.set("name", item.name);
        int bodypart = _slots.get(rset.getBodyPart());
        item.set.set("bodypart", bodypart);
        item.set.set("crystallizable", Boolean.valueOf(rset.getCrystallizable()));
        item.set.set("crystal_count", rset.getCrystalCount());
        item.set.set("sellable", Boolean.valueOf(rset.getSellable()));
        item.set.set("dropable", Boolean.valueOf(rset.getDropable()));
        item.set.set("destroyable", Boolean.valueOf(rset.getDestroyable()));
        item.set.set("tradeable", Boolean.valueOf(rset.getTradeable()));
        item.set.set("item_skill_id", rset.getItemSkillId());
        item.set.set("item_skill_lvl", rset.getItemSkillLevel());

        if ((bodypart == L2Item.SLOT_NECK) || (bodypart == L2Item.SLOT_HAIR) || (bodypart == L2Item.SLOT_FACE) || (bodypart == L2Item.SLOT_DHAIR) || ((bodypart & L2Item.SLOT_L_EAR) != 0) || ((bodypart & L2Item.SLOT_L_FINGER) != 0)) {
            item.set.set("type1", L2Item.TYPE1_WEAPON_RING_EARRING_NECKLACE);
            item.set.set("type2", L2Item.TYPE2_ACCESSORY);
        } else {
            item.set.set("type1", L2Item.TYPE1_SHIELD_ARMOR);
            item.set.set("type2", L2Item.TYPE2_SHIELD_ARMOR);
        }

        item.set.set("weight", rset.getWeight());
        item.set.set("material", _materials.get(rset.getMaterial()));
        item.set.set("crystal_type", _crystalTypes.get(rset.getCrystalType()));
        item.set.set("avoid_modify", rset.getAvoidModify());
        item.set.set("duration", rset.getDuration());
        item.set.set("p_def", rset.getPdef());
        item.set.set("m_def", rset.getMdef());
        item.set.set("mp_bonus", rset.getMpBonus());
        item.set.set("price", rset.getPrice());

        if (item.type == L2ArmorType.PET) {
            item.set.set("type1", L2Item.TYPE1_SHIELD_ARMOR);
            if (item.set.getInteger("bodypart") == L2Item.SLOT_WOLF) {
                item.set.set("type2", L2Item.TYPE2_PET_WOLF);
            } else if (item.set.getInteger("bodypart") == L2Item.SLOT_HATCHLING) {
                item.set.set("type2", L2Item.TYPE2_PET_HATCHLING);
            } else if (item.set.getInteger("bodypart") == L2Item.SLOT_BABYPET) {
                item.set.set("type2", L2Item.TYPE2_PET_BABY);
            } else {
                item.set.set("type2", L2Item.TYPE2_PET_STRIDER);
            }

            item.set.set("bodypart", L2Item.SLOT_CHEST);
        }

        return item;
    }

    private Item readItem(EtcItem etcItem) {
        Item item = new Item();
        item.set = new StatsSet();
        item.id = etcItem.getItemId();

        item.set.set("item_id", item.id);
        item.set.set("crystallizable", Boolean.valueOf(etcItem.getCrystallizable()));
        item.set.set("type1", L2Item.TYPE1_ITEM_QUESTITEM_ADENA);
        item.set.set("type2", L2Item.TYPE2_OTHER);
        item.set.set("bodypart", 0);
        item.set.set("crystal_count", etcItem.getCrystalCount());
        item.set.set("sellable", Boolean.valueOf(etcItem.getSellable()));
        item.set.set("dropable", Boolean.valueOf(etcItem.getDropable()));
        item.set.set("destroyable", Boolean.valueOf(etcItem.getDestroyable()));
        item.set.set("tradeable", Boolean.valueOf(etcItem.getTradeable()));
        String itemType = etcItem.getItemType();
        if (itemType.equals("none")) {
            item.type = L2EtcItemType.OTHER; // only for default
        } else if (itemType.equals("castle_guard")) {
            item.type = L2EtcItemType.SCROLL; // dummy
        } else if (itemType.equals("material")) {
            item.type = L2EtcItemType.MATERIAL;
        } else if (itemType.equals("pet_collar")) {
            item.type = L2EtcItemType.PET_COLLAR;
        } else if (itemType.equals("potion")) {
            item.type = L2EtcItemType.POTION;
        } else if (itemType.equals("recipe")) {
            item.type = L2EtcItemType.RECEIPE;
        } else if (itemType.equals("scroll")) {
            item.type = L2EtcItemType.SCROLL;
        } else if (itemType.equals("seed")) {
            item.type = L2EtcItemType.SEED;
        } else if (itemType.equals("shot")) {
            item.type = L2EtcItemType.SHOT;
        } else if (itemType.equals("spellbook")) {
            item.type = L2EtcItemType.SPELLBOOK; // Spellbook, Amulet, Blueprint
        } else if (itemType.equals("herb")) {
            item.type = L2EtcItemType.HERB;
        } else if (itemType.equals("arrow")) {
            item.type = L2EtcItemType.ARROW;
            item.set.set("bodypart", L2Item.SLOT_L_HAND);
        } else if (itemType.equals("quest")) {
            item.type = L2EtcItemType.QUEST;
            item.set.set("type2", L2Item.TYPE2_QUEST);
        } else if (itemType.equals("lure")) {
            item.type = L2EtcItemType.OTHER;
            item.set.set("bodypart", L2Item.SLOT_L_HAND);
        } else {
            _log.debug("unknown etcitem type:" + itemType);
            item.type = L2EtcItemType.OTHER;
        }

        String consume = etcItem.getConsumeType();
        if (consume.equals("asset")) {
            item.type = L2EtcItemType.MONEY;
            item.set.set("stackable", true);
            item.set.set("type2", L2Item.TYPE2_MONEY);
        } else if (consume.equals("stackable")) {
            item.set.set("stackable", true);
        } else {
            item.set.set("stackable", false);
        }

        int material = _materials.get(etcItem.getMaterial());
        item.set.set("material", material);

        int crystal = _crystalTypes.get(etcItem.getCrystalType());
        item.set.set("crystal_type", crystal);

        int weight = etcItem.getWeight();
        item.set.set("weight", weight);
        item.name = etcItem.getName();
        item.set.set("name", item.name);

        item.set.set("duration", etcItem.getDuration());
        item.set.set("price", etcItem.getPrice());

        return item;
    }

    /**
     * Returns if ItemTable initialized
     *
     * @return boolean
     */
    public boolean isInitialized() {
        return _initialized;
    }

    /*
     * private void fillEtcItemsTable() { for (Item itemInfo : itemData.values()) { L2EtcItem item = SkillsEngine.getInstance().loadEtcItem(itemInfo.id, itemInfo.type, itemInfo.name, itemInfo.set); if (item == null) { item = new L2EtcItem((L2EtcItemType)itemInfo.type, itemInfo.set); }
     * _etcItems.put(item.getItemId(), item); } } private void fillArmorsTable() { List<L2Armor> armorList = SkillsEngine.getInstance().loadArmors(armorData); /*for (Item itemInfo : armorData.values()) { L2Armor armor = SkillsEngine.getInstance().loadArmor(itemInfo.id, itemInfo.type, itemInfo.name,
     * itemInfo.set); if (armor == null) armor = new L2Armor((L2ArmorType)itemInfo.type, itemInfo.set); _armors.put(armor.getItemId(), armor); }* } private void FillWeaponsTable() { for (Item itemInfo : weaponData.values()) { L2Weapon weapon = SkillsEngine.getInstance().loadWeapon(itemInfo.id,
     * itemInfo.type, itemInfo.name, itemInfo.set); if (weapon == null) weapon = new L2Weapon((L2WeaponType)itemInfo.type, itemInfo.set); _weapons.put(weapon.getItemId(), weapon); } }
     */

    /**
     * Builds a variable in which all items are putting in in function of their ID.
     */
    private void buildFastLookupTable() {
        int highestId = 0;

        // Get highest ID of item in armor FastMap, then in weapon FastMap, and finally in etcitem FastMap
        for (Integer id : _armors.keySet()) {
            L2Armor item = _armors.get(id);
            if (item.getItemId() > highestId) {
                highestId = item.getItemId();
            }
        }
        for (Integer id : _weapons.keySet()) {

            L2Weapon item = _weapons.get(id);
            if (item.getItemId() > highestId) {
                highestId = item.getItemId();
            }
        }
        for (Integer id : _etcItems.keySet()) {
            L2EtcItem item = _etcItems.get(id);
            if (item.getItemId() > highestId) {
                highestId = item.getItemId();
            }
        }

        // Create a FastLookUp Table called _allTemplates of size : value of the highest item ID
        if (Config.DEBUG) {
            _log.debug("highest item id used:" + highestId);
        }
        _allTemplates = new L2Item[highestId + 1];

        // Insert armor item in Fast Look Up Table
        for (Integer id : _armors.keySet()) {
            L2Armor item = _armors.get(id);
            assert _allTemplates[id.intValue()] == null;
            _allTemplates[id.intValue()] = item;
        }

        // Insert weapon item in Fast Look Up Table
        for (Integer id : _weapons.keySet()) {
            L2Weapon item = _weapons.get(id);
            assert _allTemplates[id.intValue()] == null;
            _allTemplates[id.intValue()] = item;
        }

        // Insert etcItem item in Fast Look Up Table
        for (Integer id : _etcItems.keySet()) {
            L2EtcItem item = _etcItems.get(id);
            assert _allTemplates[id.intValue()] == null;
            _allTemplates[id.intValue()] = item;
        }
    }

    /**
     * Returns the item corresponding to the item ID
     *
     * @param id : int designating the item
     * @return L2Item
     */
    public L2Item getTemplate(int id) {
        if (id > _allTemplates.length) {
            return null;
        }
        return _allTemplates[id];
    }

    /**
     * Create the L2ItemInstance corresponding to the Item Identifier and quantitiy add logs the activity.<BR>
     * <BR>
     * <B><U> Actions</U> :</B><BR>
     * <BR>
     * <li>Create and Init the L2ItemInstance corresponding to the Item Identifier and quantity</li> <li>Add the L2ItemInstance object to _allObjects of L2world</li> <li>Logs Item creation according to log settings</li><BR>
     * <BR>
     *
     * @param process   : String Identifier of process triggering this action
     * @param itemId    : int Item Identifier of the item to be created
     * @param count     : int Quantity of items to be created for stackable items
     * @param actor     : L2PcInstance Player requesting the item creation
     * @param reference : L2Object Object referencing current action like NPC selling item or previous item in transformation
     * @return L2ItemInstance corresponding to the new item
     */
    public L2ItemInstance createItem(String process, int itemId, int count, L2PcInstance actor, L2Object reference) {
        // Create and Init the L2ItemInstance corresponding to the Item Identifier
        L2ItemInstance item = new L2ItemInstance(IdFactory.getInstance().getNextId(), itemId);

        if (process.equalsIgnoreCase("loot") && !Config.AUTO_LOOT) {
            ScheduledFuture<?> itemLootShedule;
            long delay = 0;
            // if in CommandChannel and was killing a World/RaidBoss
            if (((reference != null) && (reference instanceof L2BossInstance)) || (reference instanceof L2RaidBossInstance)) {
                if ((((L2Attackable) reference).getFirstCommandChannelAttacked() != null) && ((L2Attackable) reference).getFirstCommandChannelAttacked().meetRaidWarCondition(reference)) {
                    item.setOwnerId(((L2Attackable) reference).getFirstCommandChannelAttacked().getChannelLeader().getObjectId());
                    delay = 300000;
                } else {
                    delay = 15000;
                    item.setOwnerId(actor.getObjectId());
                }
            } else {
                item.setOwnerId(actor.getObjectId());
                delay = 15000;
            }
            itemLootShedule = ThreadPoolManager.getInstance().scheduleGeneral(new resetOwner(item), delay);
            item.setItemLootShedule(itemLootShedule);
        }

        if (Config.DEBUG) {
            _log.debug("ItemTable: Item created  oid:" + item.getObjectId() + " itemid:" + itemId);
        }

        // Add the L2ItemInstance object to _allObjects of L2world
        L2World.getInstance().storeObject(item);

        // Set Item parameters
        if (item.isStackable() && (count > 1)) {
            item.setCount(count);
        }

        if (Config.LOG_ITEMS) {
            _logItems.info("CREATE: {}", process);
        }

        return item;
    }

    public L2ItemInstance createItem(String process, int itemId, int count, L2PcInstance actor) {
        return createItem(process, itemId, count, actor, null);
    }

    /**
     * Returns a dummy (fr = factice) item.<BR>
     * <BR>
     * <U><I>Concept :</I></U><BR>
     * Dummy item is created by setting the ID of the object in the world at null value
     *
     * @param itemId : int designating the item
     * @return L2ItemInstance designating the dummy item created
     */
    public L2ItemInstance createDummyItem(int itemId) {
        L2Item item = getTemplate(itemId);
        if (item == null) {
            return null;
        }
        L2ItemInstance temp = new L2ItemInstance(0, item);
        try {
            temp = new L2ItemInstance(0, itemId);
        } catch (ArrayIndexOutOfBoundsException e) {
            // this can happen if the item templates were not initialized
        }

        if (temp.getItem() == null) {
            _log.warn("ItemTable: Item Template missing for Id: " + itemId);
        }

        return temp;
    }

    /**
     * Destroys the L2ItemInstance.<BR>
     * <BR>
     * <B><U> Actions</U> :</B><BR>
     * <BR>
     * <li>Sets L2ItemInstance parameters to be unusable</li> <li>Removes the L2ItemInstance object to _allObjects of L2world</li> <li>Logs Item delettion according to log settings</li><BR>
     * <BR>
     *
     * @param process   : String Identifier of process triggering this action
     * @param item
     * @param actor     : L2PcInstance Player requesting the item destroy
     * @param reference : L2Object Object referencing current action like NPC selling item or previous item in transformation
     */
    public void destroyItem(String process, L2ItemInstance item, L2PcInstance actor, L2Object reference) {
        synchronized (item) {
            item.setCount(0);
            item.setOwnerId(0);
            item.setLocation(ItemLocation.VOID);
            item.setLastChange(L2ItemInstance.REMOVED);

            L2World.getInstance().removeObject(item);
            IdFactory.getInstance().releaseId(item.getObjectId());

            if (Config.LOG_ITEMS) {
                _logItems.info("DELETE: {}", process);
            }

            // if it's a pet control item, delete the pet as well
            if (L2PetDataTable.isPetItem(item.getItemId())) {
                PetsRepository repository = DatabaseAccess.getRepository(PetsRepository.class);
                repository.deleteById(item.getObjectId());
            }
        }
    }

    public void reload() {
        synchronized (_instance) {
            _instance = null;
            _instance = new ItemTable();
        }
    }

    protected class resetOwner implements Runnable {
        L2ItemInstance _item;

        public resetOwner(L2ItemInstance item) {
            _item = item;
        }

        @Override
        public void run() {
            _item.setOwnerId(0);
            _item.setItemLootShedule(null);
        }
    }

}
