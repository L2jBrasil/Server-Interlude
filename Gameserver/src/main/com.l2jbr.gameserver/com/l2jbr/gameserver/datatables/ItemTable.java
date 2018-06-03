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
import com.l2jbr.gameserver.model.database.ItemTemplate;
import com.l2jbr.gameserver.model.database.repository.ArmorRepository;
import com.l2jbr.gameserver.model.database.repository.EtcItemRepository;
import com.l2jbr.gameserver.model.database.repository.PetsRepository;
import com.l2jbr.gameserver.model.database.repository.WeaponRepository;
import com.l2jbr.gameserver.templates.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

public class ItemTable {
    private static Logger _log = LoggerFactory.getLogger(ItemTable.class);
    private static Logger _logItems = LoggerFactory.getLogger("item");

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

    private static final Map<Integer, Item> itemData = new LinkedHashMap<>();

    private static final Map<Integer, Item> weaponData = new LinkedHashMap<>();

    private static final Map<Integer, Item> armorData = new LinkedHashMap<>();

    private Map<Integer, ItemTemplate> items;

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

        items = new HashMap<>();

        DatabaseAccess.getRepository(EtcItemRepository.class).findAll().forEach(this::addItem);
        DatabaseAccess.getRepository(ArmorRepository.class).findAll().forEach(this::addItem);
        DatabaseAccess.getRepository(WeaponRepository.class).findAll().forEach(this::addItem);


       /* for (L2Armor armor : SkillsEngine.getInstance().loadArmors(armorData)) {
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

        buildFastLookupTable();*/
    }

    private void addItem(ItemTemplate item) {
        items.put(item.getId(), item);
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
    public ItemTemplate getTemplate(int id) {
        return items.get(id);
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
        ItemTemplate item = getTemplate(itemId);
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
