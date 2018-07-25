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
import com.l2jbr.gameserver.ThreadPoolManager;
import com.l2jbr.gameserver.idfactory.IdFactory;
import com.l2jbr.gameserver.model.*;
import com.l2jbr.gameserver.model.L2ItemInstance.ItemLocation;
import com.l2jbr.gameserver.model.actor.instance.L2BossInstance;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.model.actor.instance.L2RaidBossInstance;
import com.l2jbr.gameserver.model.entity.database.ItemTemplate;
import com.l2jbr.gameserver.model.entity.database.repository.ArmorRepository;
import com.l2jbr.gameserver.model.entity.database.repository.EtcItemRepository;
import com.l2jbr.gameserver.model.entity.database.repository.PetsRepository;
import com.l2jbr.gameserver.model.entity.database.repository.WeaponRepository;
import com.l2jbr.gameserver.model.entity.xml.ItemStatsReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import static com.l2jbr.gameserver.util.GameserverMessages.getMessage;
import static java.util.Objects.isNull;

public class ItemTable {
    private static Logger _log = LoggerFactory.getLogger(ItemTable.class);
    private static Logger _logItems = LoggerFactory.getLogger("item");

    private static ItemTable INSTANCE;

    private Map<Integer, ItemTemplate> items;
    private ItemStatsReader statsReader;

    public static ItemTable getInstance() {
        if (isNull(INSTANCE)) {
            INSTANCE = new ItemTable();
        }
        return INSTANCE;
    }

    private ItemTable() {
        items = new HashMap<>();
        loadItemsStat();
        DatabaseAccess.getRepository(EtcItemRepository.class).findAll().forEach(this::addToItems);
        DatabaseAccess.getRepository(ArmorRepository.class).findAll().forEach(this::addToItems);
        DatabaseAccess.getRepository(WeaponRepository.class).findAll().forEach(this::addToItems);
        _log.info(getMessage("info.items.loaded"), items.size());
    }

    private void loadItemsStat() {
        try {
            statsReader = new ItemStatsReader();
            statsReader.readAll();
        } catch (JAXBException e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void addToItems(ItemTemplate item) {
        if(!isNull(statsReader)) {
            statsReader.attach(item);
        }
        items.put(item.getId(), item);
    }

    /**
     * Returns the item corresponding to the item ID
     *
     * @param id : int designating the item
     * @return ItemTemplate
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
            _log.warn("ItemTable: Item Template missing for Id: " + itemId);
            return null;
        }
        return new L2ItemInstance(0, item);
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
        //FIXME  The player must relogin to get new item status
        synchronized (INSTANCE) {
            INSTANCE = null;
            INSTANCE = new ItemTable();
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
