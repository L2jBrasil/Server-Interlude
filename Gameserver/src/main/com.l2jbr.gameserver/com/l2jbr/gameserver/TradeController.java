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
package com.l2jbr.gameserver;

import com.l2jbr.commons.database.DatabaseAccess;
import com.l2jbr.gameserver.datatables.ItemTable;
import com.l2jbr.gameserver.model.L2ItemInstance;
import com.l2jbr.gameserver.model.L2TradeList;
import com.l2jbr.gameserver.model.entity.database.MerchantItem;
import com.l2jbr.gameserver.model.entity.database.MerchantShop;
import com.l2jbr.gameserver.model.entity.database.repository.MerchantBuyListRepository;
import com.l2jbr.gameserver.model.entity.database.repository.MerchantShopRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static java.util.Objects.isNull;

public class TradeController {
    private static Logger _log = LoggerFactory.getLogger(TradeController.class.getName());
    private static TradeController INSTANCE;

    private int _nextListId;
    private final Map<Integer, MerchantShop> shopList;

    /**
     * Task launching the function for restore count of Item (Clan Hall)
     */
    public class RestoreCount implements Runnable {
        private final int _timer;

        public RestoreCount(int time) {
            _timer = time;
        }

        public RestoreCount(Integer time, RestoreInfo restoreInfo) {
            _timer =time;
        }

        @Override
        public void run() {
            try {
                restoreCount(_timer);
                dataTimerSave(_timer);
                ThreadPoolManager.getInstance().scheduleGeneral(new RestoreCount(_timer), (long) _timer * 60 * 60 * 1000);
            } catch (Throwable t) {
            }
        }
    }


    private class RestoreInfo {
        Map<Integer, Set<Integer>> shopIds;
        long saveTimer;

        RestoreInfo(int shopId, int itemId, long saveTimer) {
            shopIds = new HashMap<>();
            this.saveTimer = saveTimer;
            add(shopId, itemId);
        }

        public void add(int shopId, int itemId) {
            if(!shopIds.containsKey(shopId)) {
                shopIds.put(shopId, new HashSet<>());
            }
            shopIds.get(shopId).add(itemId);
        }
    }

    public static TradeController getInstance() {
        if (isNull(INSTANCE)) {
            INSTANCE = new TradeController();
        }
        return INSTANCE;
    }

    private TradeController() {
        shopList = new HashMap<>();
        Map<Integer, RestoreInfo> timers = new HashMap<>();

        DatabaseAccess.getRepository(MerchantShopRepository.class).findAll().forEach(shop -> {
            shopList.put(shop.getId(), shop);

            shop.getItems().stream().filter(MerchantItem::isLimited).forEach(item -> {
                if(!timers.containsKey(item.getTime())) {
                    timers.put(item.getTime(), new RestoreInfo(shop.getId(), item.getItemId(), item.getSavetimer()));
                } else {
                    timers.get(item.getCount()).add(shop.getId(), item.getItemId());
                }
            });
        });

        long currentMillis = System.currentTimeMillis();
        timers.forEach((time, restoreInfo) -> {
            if((restoreInfo.saveTimer - currentMillis)  > 0) {
                ThreadPoolManager.getInstance().scheduleGeneral(new RestoreCount(time, restoreInfo), restoreInfo.saveTimer - currentMillis);
            } else {
                ThreadPoolManager.getInstance().scheduleGeneral(new RestoreCount(time, restoreInfo), 0);
            }
        });

        _log.info("TradeController: Loaded {} Buylists.", shopList.size());
    }

    private int parseList(String line) {
        int itemCreated = 0;
        StringTokenizer st = new StringTokenizer(line, ";");

        int listId = Integer.parseInt(st.nextToken());
        L2TradeList buy1 = new L2TradeList(listId);
        while (st.hasMoreTokens()) {
            int itemId = Integer.parseInt(st.nextToken());
            int price = Integer.parseInt(st.nextToken());
            L2ItemInstance item = ItemTable.getInstance().createDummyItem(itemId);
            item.setPriceToSell(price);
            buy1.addItem(item);
            itemCreated++;
        }

        shopList.put(buy1.getListId(), buy1);
        return itemCreated;
    }

    public L2TradeList getBuyList(int listId) {
        if (shopList.get(listId) != null) {
            return shopList.get(listId);
        }
        return _listsTaskItem.get(listId);
    }

    public List<L2TradeList> getBuyListByNpcId(int npcId) {
        List<L2TradeList> lists = new LinkedList<>();

        for (L2TradeList list : shopList.values()) {
            if (list.getNpcId().startsWith("gm")) {
                continue;
            }
            if (npcId == Integer.parseInt(list.getNpcId())) {
                lists.add(list);
            }
        }
        for (L2TradeList list : _listsTaskItem.values()) {
            if (list.getNpcId().startsWith("gm")) {
                continue;
            }
            if (npcId == Integer.parseInt(list.getNpcId())) {
                lists.add(list);
            }
        }
        return lists;
    }

    protected void restoreCount(int time) {
        if (_listsTaskItem == null) {
            return;
        }
        for (L2TradeList list : _listsTaskItem.values()) {
            list.restoreCount(time);
        }
    }

    protected void dataTimerSave(int time) {
        long saveTimer = System.currentTimeMillis() + ((long) time * 60 * 60 * 1000);
        MerchantBuyListRepository repository = DatabaseAccess.getRepository(MerchantBuyListRepository.class);
        repository.updateSaveTimerByTime(time, saveTimer);
    }

    public void dataCountStore() {
        if (_listsTaskItem == null) {
            return;
        }

        MerchantBuyListRepository repository = DatabaseAccess.getRepository(MerchantBuyListRepository.class);
        for (L2TradeList list : _listsTaskItem.values()) {
            if (list == null) {
                continue;
            }

            for (L2ItemInstance item : list.getItems()) {
                if (item.getCount() < item.getInitCount()){
                    repository.updateCurrentCountByItem(list.getListId(), item.getItemId(), item.getCount());
                }
            }
        }
    }

    public synchronized int getNextId() {
        return _nextListId++;
    }
}
