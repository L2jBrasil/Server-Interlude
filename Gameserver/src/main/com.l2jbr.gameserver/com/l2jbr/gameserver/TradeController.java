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
import com.l2jbr.gameserver.model.entity.database.MerchantBuyList;
import com.l2jbr.gameserver.model.entity.database.MerchantShopIds;
import com.l2jbr.gameserver.model.entity.database.repository.MerchantBuyListRepository;
import com.l2jbr.gameserver.model.entity.database.repository.MerchantShopRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * This class ...
 *
 * @version $Revision: 1.5.4.13 $ $Date: 2005/04/06 16:13:38 $
 */
public class TradeController {
    private static Logger _log = LoggerFactory.getLogger(TradeController.class.getName());
    private static TradeController _instance;

    private int _nextListId;
    private final Map<Integer, L2TradeList> _lists;
    private final Map<Integer, L2TradeList> _listsTaskItem;

    /**
     * Task launching the function for restore count of Item (Clan Hall)
     */
    public class RestoreCount implements Runnable {
        private final int _timer;

        public RestoreCount(int time) {
            _timer = time;
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

    public static TradeController getInstance() {
        if (_instance == null) {
            _instance = new TradeController();
        }
        return _instance;
    }

    private TradeController() {
        _lists = new LinkedHashMap<>();
        _listsTaskItem = new LinkedHashMap<>();
        LinkedHashMap<Integer, Long> timers = new LinkedHashMap<>();
        int dummyItemCount = 0;
        boolean LimitedItem = false;
        MerchantShopRepository repository = DatabaseAccess.getRepository(MerchantShopRepository.class);
        for (MerchantShopIds merchantShopIds : repository.findAll()) {

            L2TradeList tradeList = new L2TradeList(merchantShopIds.getShopId());
            tradeList.setNpcId(merchantShopIds.getNpcId());

            for (MerchantBuyList merchantBuyList : merchantShopIds.getBuyLists()) {
                LimitedItem = false;
                dummyItemCount++;
                L2ItemInstance item = ItemTable.getInstance().createDummyItem(merchantBuyList.getItemId());
                if (item == null) {
                    continue;
                }

                if (merchantBuyList.getCount() > -1) {
                    item.setCountDecrease(true);
                    LimitedItem = true;
                    if(!timers.containsKey(merchantBuyList.getTime())) {
                        timers.put(merchantBuyList.getTime(), merchantBuyList.getSavetimer());
                    }
                }

                item.setPriceToSell(merchantBuyList.getPrice());
                item.setTime(merchantBuyList.getTime());
                item.setInitCount(merchantBuyList.getCount());

                if (merchantBuyList.getCurrentCount() > -1) {
                    item.setCount(merchantBuyList.getCurrentCount());
                } else {
                    item.setCount(merchantBuyList.getCount());
                }

                tradeList.addItem(item);
            }

            if (LimitedItem) {
                _listsTaskItem.put(tradeList.getListId(), tradeList);
            } else {
                _lists.put(tradeList.getListId(), tradeList);
            }
            _nextListId = Math.max(_nextListId, tradeList.getListId() + 1);
        }

        _log.debug("created {} Dummy-Items for buylists", dummyItemCount);

        _log.info("TradeController: Loaded {} Buylists.", _lists.size());
        _log.info("TradeController: Loaded {} Limited Buylists.", _listsTaskItem.size());


        long currentMillis = System.currentTimeMillis();
        timers.forEach((time, saveTimer) -> {
            if((saveTimer - currentMillis)  > 0) {
                ThreadPoolManager.getInstance().scheduleGeneral(new RestoreCount(time), saveTimer - System.currentTimeMillis());
            } else {
                ThreadPoolManager.getInstance().scheduleGeneral(new RestoreCount(time), 0);
            }
        });

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

        _lists.put(buy1.getListId(), buy1);
        return itemCreated;
    }

    public L2TradeList getBuyList(int listId) {
        if (_lists.get(listId) != null) {
            return _lists.get(listId);
        }
        return _listsTaskItem.get(listId);
    }

    public List<L2TradeList> getBuyListByNpcId(int npcId) {
        List<L2TradeList> lists = new LinkedList<>();

        for (L2TradeList list : _lists.values()) {
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
