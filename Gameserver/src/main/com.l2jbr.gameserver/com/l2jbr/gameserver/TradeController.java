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

    private final Map<Integer, MerchantShop> shopList;
    Map<Integer, RestoreInfo> timers;

    public static TradeController getInstance() {
        if (isNull(INSTANCE)) {
            INSTANCE = new TradeController();
        }
        return INSTANCE;
    }

    private TradeController() {
        shopList = new HashMap<>();
        timers = new HashMap<>();

        DatabaseAccess.getRepository(MerchantShopRepository.class).findAll().forEach(shop -> {
            shopList.put(shop.getId(), shop);

            shop.getItems().stream().filter(MerchantItem::isLimited).forEach(item -> {
                if(!timers.containsKey(item.getTime())) {
                    timers.put(item.getTime(), new RestoreInfo(item));
                } else {
                    timers.get(item.getTime()).add(item);
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

    public MerchantShop getBuyList(int listId) {
        return shopList.get(listId);
    }

    public List<MerchantShop> getBuyListByNpcId(int npcId) {
        List<MerchantShop> lists = new LinkedList<>();

        for (MerchantShop list : shopList.values()) {
            if (list.getNpcId().startsWith("gm")) {
                continue;
            }
            if (npcId == Integer.parseInt(list.getNpcId())) {
                lists.add(list);
            }
        }
        return lists;
    }

    public void dataCountStore() {

        MerchantBuyListRepository repository = DatabaseAccess.getRepository(MerchantBuyListRepository.class);
        for (RestoreInfo value : timers.values()) {
            value.items.stream().filter(item -> item.getCurrentCount() < item.getCount()).forEach(item -> {
                repository.updateCurrentCountByItem(item.getId(), item.getItemId(), item.getCount());
            });
        }
    }

    private void restoreCount(RestoreInfo info) {
        info.items.forEach(MerchantItem::restoreCount);
    }

    private void dataTimerSave(int time) {
        long saveTimer = System.currentTimeMillis() + ((long) time * 60 * 60 * 1000);
        MerchantBuyListRepository repository = DatabaseAccess.getRepository(MerchantBuyListRepository.class);
        repository.updateSaveTimerByTime(time, saveTimer);
    }

    private class RestoreCount implements Runnable {
        private final int time;
        private final RestoreInfo info;

        private RestoreCount(Integer time, RestoreInfo restoreInfo) {
            this.time =time;
            info = restoreInfo;
        }

        @Override
        public void run() {
            try {
                restoreCount(info);
                dataTimerSave(time);
                ThreadPoolManager.getInstance().scheduleGeneral(new RestoreCount(time, info), (long) time * 60 * 60 * 1000);
            } catch (Throwable t) {
                _log.error(t.getLocalizedMessage(), t);
            }
        }
    }

    private class RestoreInfo {
        Set<MerchantItem> items;
        long saveTimer;

        RestoreInfo(MerchantItem item) {
            items = new HashSet<>();
            saveTimer = item.getSavetimer();
            add(item);
        }

        public void add(MerchantItem item) {
            items.add(item);
        }
    }
}
