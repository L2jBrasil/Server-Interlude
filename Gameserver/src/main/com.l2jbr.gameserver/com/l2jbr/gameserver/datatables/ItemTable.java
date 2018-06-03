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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import static com.l2jbr.gameserver.templates.BodyPart.*;

public class ItemTable {
    private static Logger _log = LoggerFactory.getLogger(ItemTable.class);
    private static Logger _logItems = LoggerFactory.getLogger("item");

    private static final Map<String, L2WeaponType> _weaponTypes = new LinkedHashMap<>();
    private static final Map<String, L2ArmorType> _armorTypes = new LinkedHashMap<>();

    private L2Item[] _allTemplates;
    private final Map<Integer, L2EtcItem> _etcItems;
    private final Map<Integer, L2Armor> _armors;
    private final Map<Integer, L2Weapon> _weapons;

    private final boolean _initialized = true;

    static {

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
        _weaponTypes.put("petweapon", L2WeaponType.PET); // Pet Weapon
        _weaponTypes.put("rod", L2WeaponType.ROD); // Fishing Rods
        _weaponTypes.put("bigblunt", L2WeaponType.BIGBLUNT); // Two handed blunt
        _armorTypes.put("none", L2ArmorType.NONE);
        _armorTypes.put("light", L2ArmorType.LIGHT);
        _armorTypes.put("heavy", L2ArmorType.HEAVY);
        _armorTypes.put("magic", L2ArmorType.MAGIC);
        _armorTypes.put("petarmor", L2ArmorType.PET);
    }

    private static ItemTable _instance;

    private static final Map<Integer, Item> itemData = new LinkedHashMap<>();

    private static final Map<Integer, Item> weaponData = new LinkedHashMap<>();

    private static final Map<Integer, Item> armorData = new LinkedHashMap<>();

    public static ItemTable getInstance() {
        if (_instance == null) {
            _instance = new ItemTable();
        }
        return _instance;
    }

    public Item newItem() {
        return new Item();
    }

    private ItemTable() {

        _etcItems = new HashMap<>();
        _armors = new HashMap<>();
        _weapons = new HashMap<>();

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
        item.type = _weaponTypes.get(weapon.getType().getName());
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

        item.set.set("bodypart", weapon.getBodyPart());
        item.set.set("crystal_type", weapon.getCrystalType());
        item.set.set("crystallizable", Boolean.valueOf(weapon.isCrystallizable()));
        item.set.set("weight", weapon.getWeight());
        item.set.set("soulshots", weapon.getSoulshots());
        item.set.set("spiritshots", weapon.getSpiritshots());
        item.set.set("p_dam", weapon.getPDam());
        item.set.set("rnd_dam", weapon.getRandomDamage());
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
        item.set.set("sellable", Boolean.valueOf(weapon.isSellable()));
        item.set.set("dropable", Boolean.valueOf(weapon.isDropable()));
        item.set.set("destroyable", Boolean.valueOf(weapon.isDestroyable()));
        item.set.set("tradeable", Boolean.valueOf(weapon.isTradeable()));

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
            if (item.set.getEnum("bodypart", BodyPart.class) == BodyPart.WOLF) {
                item.set.set("type2", L2Item.TYPE2_PET_WOLF);
            } else if (item.set.getEnum("bodypart", BodyPart.class) == BodyPart.HATCHLING) {
                item.set.set("type2", L2Item.TYPE2_PET_HATCHLING);
            } else if (item.set.getEnum("bodypart", BodyPart.class) == BodyPart.BABYPET) {
                item.set.set("type2", L2Item.TYPE2_PET_BABY);
            } else {
                item.set.set("type2", L2Item.TYPE2_PET_STRIDER);
            }

            item.set.set("bodypart", BodyPart.RIGHT_HAND);
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
        item.type = _armorTypes.get(rset.getType().getName());
        item.id = rset.getId();
        item.name = rset.getName();

        item.set.set("item_id", item.id);
        item.set.set("name", item.name);
        BodyPart bodypart = rset.getBodyPart();
        item.set.set("bodypart", bodypart);
        item.set.set("crystallizable", Boolean.valueOf(rset.isCrystallizable()));
        item.set.set("crystal_count", rset.getCrystalCount());
        item.set.set("sellable", Boolean.valueOf(rset.isSellable()));
        item.set.set("dropable", Boolean.valueOf(rset.isDropable()));
        item.set.set("destroyable", Boolean.valueOf(rset.isDestroyable()));
        item.set.set("tradeable", Boolean.valueOf(rset.isTradeable()));
        item.set.set("item_skill_id", rset.getItemSkillId());
        item.set.set("item_skill_lvl", rset.getItemSkillLevel());

        if ((bodypart == NECK) || (bodypart ==  HAIR) || (bodypart == FACE) || (bodypart == DHAIR) || (bodypart == EAR) || (bodypart == FINGER)) {
            item.set.set("type1", L2Item.TYPE1_WEAPON_RING_EARRING_NECKLACE);
            item.set.set("type2", L2Item.TYPE2_ACCESSORY);
        } else {
            item.set.set("type1", L2Item.TYPE1_SHIELD_ARMOR);
            item.set.set("type2", L2Item.TYPE2_SHIELD_ARMOR);
        }

        item.set.set("weight", rset.getWeight());
        item.set.set("crystal_type", rset.getCrystalType());
        item.set.set("avoid_modify", rset.getAvoidModify());
        item.set.set("duration", rset.getDuration());
        item.set.set("p_def", rset.getPdef());
        item.set.set("m_def", rset.getMdef());
        item.set.set("mp_bonus", rset.getMpBonus());
        item.set.set("price", rset.getPrice());

        if (item.type == L2ArmorType.PET) {
            item.set.set("type1", L2Item.TYPE1_SHIELD_ARMOR);
            if (item.set.getEnum("bodypart", BodyPart.class) == WOLF) {
                item.set.set("type2", L2Item.TYPE2_PET_WOLF);
            } else if (item.set.getEnum("bodypart", BodyPart.class)== HATCHLING) {
                item.set.set("type2", L2Item.TYPE2_PET_HATCHLING);
            } else if (item.set.getEnum("bodypart", BodyPart.class) == BABYPET) {
                item.set.set("type2", L2Item.TYPE2_PET_BABY);
            } else {
                item.set.set("type2", L2Item.TYPE2_PET_STRIDER);
            }

            item.set.set("bodypart", BodyPart.CHEST);
        }

        return item;
    }

    private Item readItem(EtcItem etcItem) {
        Item item = new Item();
        item.set = new StatsSet();
        item.id = etcItem.getId();

        item.set.set("item_id", item.id);
        item.set.set("crystallizable", Boolean.valueOf(etcItem.isCrystallizable()));
        item.set.set("type1", L2Item.TYPE1_ITEM_QUESTITEM_ADENA);
        item.set.set("type2", L2Item.TYPE2_OTHER);
        item.set.set("bodypart", BodyPart.NONE);
        item.set.set("crystal_count", etcItem.getCrystalCount());
        item.set.set("sellable", Boolean.valueOf(etcItem.isSellable()));
        item.set.set("dropable", Boolean.valueOf(etcItem.isDropable()));
        item.set.set("destroyable", Boolean.valueOf(etcItem.isDestroyable()));
        item.set.set("tradeable", Boolean.valueOf(etcItem.isTradeable()));
        String itemType = etcItem.getType().name().toLowerCase();
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
            item.set.set("bodypart", BodyPart.LEFT_HAND);
        } else if (itemType.equals("quest")) {
            item.type = L2EtcItemType.QUEST;
            item.set.set("type2", L2Item.TYPE2_QUEST);
        } else if (itemType.equals("lure")) {
            item.type = L2EtcItemType.OTHER;
            item.set.set("bodypart", BodyPart.LEFT_HAND);
        } else {
            _log.debug("unknown etcitem type:" + itemType);
            item.type = L2EtcItemType.OTHER;
        }

        String consume = etcItem.getConsumeType().name().toLowerCase();
        if (consume.equals("asset")) {
            item.type = L2EtcItemType.MONEY;
            item.set.set("stackable", true);
            item.set.set("type2", L2Item.TYPE2_MONEY);
        } else if (consume.equals("stackable")) {
            item.set.set("stackable", true);
        } else {
            item.set.set("stackable", false);
        }

        item.set.set("crystal_type", etcItem.getCrystalType());

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
     * _etcItems.put(item.getId(), item); } } private void fillArmorsTable() { List<L2Armor> armorList = SkillsEngine.getInstance().loadArmors(armorData); /*for (Item itemInfo : armorData.values()) { L2Armor armor = SkillsEngine.getInstance().loadArmor(itemInfo.id, itemInfo.type, itemInfo.name,
     * itemInfo.set); if (armor == null) armor = new L2Armor((L2ArmorType)itemInfo.type, itemInfo.set); _armors.put(armor.getId(), armor); }* } private void FillWeaponsTable() { for (Item itemInfo : weaponData.values()) { L2Weapon weapon = SkillsEngine.getInstance().loadWeapon(itemInfo.id,
     * itemInfo.type, itemInfo.name, itemInfo.set); if (weapon == null) weapon = new L2Weapon((L2WeaponType)itemInfo.type, itemInfo.set); _weapons.put(weapon.getId(), weapon); } }
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
