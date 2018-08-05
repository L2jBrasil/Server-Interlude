/* This program is free software; you can redistribute it and/or modify
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
package com.l2jbr.gameserver.instancemanager;

import com.l2jbr.commons.Config;
import com.l2jbr.commons.database.DatabaseAccess;
import com.l2jbr.gameserver.ItemsAutoDestroy;
import com.l2jbr.gameserver.ThreadPoolManager;
import com.l2jbr.gameserver.model.L2ItemInstance;
import com.l2jbr.gameserver.model.L2Object;
import com.l2jbr.gameserver.model.L2World;
import com.l2jbr.gameserver.model.entity.database.ItemsOnGround;
import com.l2jbr.gameserver.model.entity.database.repository.ItemsOnGroundRepository;
import com.l2jbr.gameserver.templates.ItemType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

import static java.util.Objects.isNull;

/**
 * This class manage all items on ground
 *
 * @author DiezelMax - original ideea
 * @author Enforcer - actual build
 */
public class ItemsOnGroundManager {
    static final Logger _log = LoggerFactory.getLogger(ItemsOnGroundManager.class);
    private static ItemsOnGroundManager _instance;
    protected List<L2ItemInstance> _items;

    private ItemsOnGroundManager() {
        if (!Config.SAVE_DROPPED_ITEM) {
            return;
        }
        _items = new LinkedList<>();
        if (Config.SAVE_DROPPED_ITEM_INTERVAL > 0) {
            ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new storeInDb(), Config.SAVE_DROPPED_ITEM_INTERVAL, Config.SAVE_DROPPED_ITEM_INTERVAL);
        }
    }

    public static ItemsOnGroundManager getInstance() {
        if (isNull(_instance)) {
            _instance = new ItemsOnGroundManager();
            _instance.load();
        }
        return _instance;
    }

    private void load() {
        // If SaveDroppedItem is false, may want to delete all items previously stored to avoid add old items on reactivate
        if (!Config.SAVE_DROPPED_ITEM && Config.CLEAR_DROPPED_ITEM_TABLE) {
            emptyTable();
        }

        if (!Config.SAVE_DROPPED_ITEM) {
            return;
        }

        ItemsOnGroundRepository repository = DatabaseAccess.getRepository(ItemsOnGroundRepository.class);
        // if DestroyPlayerDroppedItem was previously false, items curently protected will be added to ItemsAutoDestroy
        if (Config.DESTROY_DROPPED_PLAYER_ITEM) {
            if (!Config.DESTROY_EQUIPABLE_PLAYER_ITEM) {
                repository.updateDropTimeNotEquipable(System.currentTimeMillis());
            } else if (Config.DESTROY_EQUIPABLE_PLAYER_ITEM) {
                repository.updateDropTime(System.currentTimeMillis());
            }
        }

        repository.findAll().forEach(itemsOnGround -> {
            L2ItemInstance item = new L2ItemInstance(itemsOnGround.getId(), itemsOnGround.getItemId());
            L2World.getInstance().storeObject(item);
            if (item.isStackable() && (itemsOnGround.getCount() > 1)) {
                item.setCount(itemsOnGround.getCount());
            }
            if (itemsOnGround.getEnchantLevel() > 0) {
                item.setEnchantLevel(itemsOnGround.getEnchantLevel());
            }
            item.getPosition().setWorldPosition(itemsOnGround.getX(), itemsOnGround.getY(), itemsOnGround.getZ());
            item.getPosition().setWorldRegion(L2World.getInstance().getRegion(item.getPosition().getWorldPosition()));
            item.getPosition().getWorldRegion().addVisibleObject(item);
            item.setDropTime(itemsOnGround.getDropTime());
            if (itemsOnGround.getDropTime() == -1) {
                item.setProtected(true);
            } else {
                item.setProtected(false);
            }
            item.setIsVisible(true);
            L2World.getInstance().addVisibleObject(item, item.getPosition().getWorldRegion(), null);
            _items.add(item);
            // add to ItemsAutoDestroy only items not protected
            if (!Config.LIST_PROTECTED_ITEMS.contains(item.getItemId())) {
                if (itemsOnGround.getDropTime() > -1) {
                    if (((Config.AUTODESTROY_ITEM_AFTER > 0) && (item.getItemType() != ItemType.HERB)) ||
                            ((Config.HERB_AUTO_DESTROY_TIME > 0) && (item.getItemType() == ItemType.HERB))) {
                        ItemsAutoDestroy.getInstance().addItem(item);
                    }
                }
            }
        });

        _log.info("ItemsOnGroundManager: restored {} items.", _items.size());

        if (Config.EMPTY_DROPPED_ITEM_TABLE_AFTER_LOAD) {
            emptyTable();
        }
    }

    public void save(L2ItemInstance item) {
        if (!Config.SAVE_DROPPED_ITEM) {
            return;
        }
        _items.add(item);
    }

    public void removeObject(L2Object item) {
        if (!Config.SAVE_DROPPED_ITEM) {
            return;
        }
        _items.remove(item);
    }

    public void saveInDb() {
        new storeInDb().run();
    }

    public void cleanUp() {
        _items.clear();
    }

    public void emptyTable() {
        ItemsOnGroundRepository repository = DatabaseAccess.getRepository(ItemsOnGroundRepository.class);
        repository.deleteAll();
    }

    protected class storeInDb extends Thread {
        @Override
        public void run() {
            if (!Config.SAVE_DROPPED_ITEM) {
                return;
            }

            emptyTable();

            if (_items.isEmpty()) {
                if (Config.DEBUG) {
                    _log.warn("ItemsOnGroundManager: nothing to save...");
                }
                return;
            }

            ItemsOnGroundRepository repository = DatabaseAccess.getRepository(ItemsOnGroundRepository.class);
            for (L2ItemInstance item : _items) {

                if (CursedWeaponsManager.getInstance().isCursed(item.getItemId())) {
                    continue; // Cursed Items not saved to ground, prevent double save
                }

                ItemsOnGround itemsOnGround = new ItemsOnGround(item);
                repository.save(itemsOnGround);
            }
            _log.debug("ItemsOnGroundManager: {} items on ground saved.", _items.size());

        }
    }
}
