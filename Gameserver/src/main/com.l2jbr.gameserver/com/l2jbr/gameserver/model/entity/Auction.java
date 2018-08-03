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
package com.l2jbr.gameserver.model.entity;

import com.l2jbr.gameserver.ThreadPoolManager;
import com.l2jbr.gameserver.datatables.ClanTable;
import com.l2jbr.gameserver.idfactory.IdFactory;
import com.l2jbr.gameserver.instancemanager.AuctionManager;
import com.l2jbr.gameserver.instancemanager.ClanHallManager;
import com.l2jbr.gameserver.model.L2Clan;
import com.l2jbr.gameserver.model.L2World;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.model.entity.database.AuctionBid;
import com.l2jbr.gameserver.model.entity.database.AuctionEntity;
import com.l2jbr.gameserver.model.entity.database.repository.AuctionBidRepository;
import com.l2jbr.gameserver.model.entity.database.repository.AuctionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.l2jbr.commons.database.DatabaseAccess.getRepository;
import static java.util.Objects.nonNull;

public class Auction {
    protected static final Logger _log = LoggerFactory.getLogger(Auction.class.getName());
    private AuctionEntity entity;
    private int _id = 0;
    private final int _adenaId = 57;
    private int _highestBidderId = 0;
    private String _highestBidderName = "";
    private int _highestBidderMaxBid = 0;
    private final int _itemQuantity = 0;

    private final Map<Integer, AuctionBid> _bidders = new HashMap<>();


    public Auction(int auctionId) {
        _id = auctionId;
        load();
        startAutoTask();
    }

    public Auction(int itemId, L2Clan clan, long delay, int bid, String name) {
        entity = new AuctionEntity();
        _id = itemId;
        entity.setEndDate(System.currentTimeMillis() + delay);
        entity.setItemId(itemId);
        entity.setItemName(name);
        entity.setItemType("ClanHall");
        entity.setSellerId(clan.getLeaderId());
        entity.setSellerName(clan.getLeaderName());
        entity.setSellerClanName(clan.getName());
        entity.setStartingBid(bid);
    }

    public Auction(AuctionEntity auctionEntity) {
        this.entity = auctionEntity;
        loadBid();
        startAutoTask();
    }

    public class AutoEndTask implements Runnable {

        AutoEndTask() {
        }
        @Override
        public void run() {
            try {
                endAuction();
            } catch (Throwable t) {
            }
        }
    }

    private void load() {
        entity = getRepository(AuctionRepository.class).findById(getId()).orElse(new AuctionEntity());
        loadBid();
    }

    private void loadBid() {
        entity.getBids().forEach(bid -> {
            if(_highestBidderId == 0) {
                _highestBidderId = bid.getBidderId();
                _highestBidderName = bid.getBidderName();
                _highestBidderMaxBid = bid.getMaxBid();
            }
            _bidders.put(bid.getBidderId(), bid);
        });
    }

    private void startAutoTask() {
        long currentTime = System.currentTimeMillis();
        long taskDelay = 0;
        if (entity.getEndDate() <= currentTime) {
            entity.setEndDate(currentTime + (7 * 24 * 60 * 60 * 1000));
            saveAuctionDate();
        } else {
            taskDelay = entity.getEndDate() - currentTime;
        }
        ThreadPoolManager.getInstance().scheduleGeneral(new AutoEndTask(), taskDelay);
    }

    private void saveAuctionDate() {
        AuctionRepository repository = getRepository(AuctionRepository.class);
        repository.updateEndDateById(_id, entity.getEndDate());
    }

    public void setBid(L2PcInstance bidder, int bid) {
        int requiredAdena = bid;
        if (getHighestBidderName().equals(bidder.getClan().getLeaderName())) {
            requiredAdena = bid - getHighestBidderMaxBid();
        }
        if (((getHighestBidderId() > 0) && (bid > getHighestBidderMaxBid())) || ((getHighestBidderId() == 0) && (bid >= getStartingBid()))) {
            if (takeItem(bidder, 57, requiredAdena)) {
                updateInDB(bidder, bid);
                bidder.getClan().setAuctionBiddedAt(_id, true);
                return;
            }
        }
        bidder.sendMessage("Invalid bid!");
    }

    private void returnItem(String Clan, int itemId, int quantity, boolean penalty) {
        if (penalty) {
            quantity *= 0.9; // take 10% tax fee if needed
        }
        ClanTable.getInstance().getClanByName(Clan).getWarehouse().addItem("Outbidded", _adenaId, quantity, null, null);
    }

    private boolean takeItem(L2PcInstance bidder, int itemId, int quantity) {
        if ((bidder.getClan() != null) && (bidder.getClan().getWarehouse().getAdena() >= quantity)) {
            bidder.getClan().getWarehouse().destroyItemByItemId("Buy", _adenaId, quantity, bidder, bidder);
            return true;
        }
        bidder.sendMessage("You do not have enough adena");
        return false;
    }

    private void updateInDB(L2PcInstance bidder, int bid) {
        AuctionBidRepository repository = getRepository(AuctionBidRepository.class);
        AuctionBid auctionBid = getBidders().get(bidder.getClanId());

        if (nonNull(auctionBid)) {
            repository.updateByAuctionAndBidder(getId(), bidder.getClanId(), bidder.getClan().getLeaderName(), bid, System.currentTimeMillis());
        } else {
            auctionBid = new AuctionBid();
            auctionBid.setId(IdFactory.getInstance().getNextId());
            auctionBid.setAuctionId(getId());
            auctionBid.setBidderId(bidder.getClanId());
            auctionBid.setBidderName(bidder.getName());
            auctionBid.setMaxBid(bid);
            auctionBid.setClanName(bidder.getClan().getName());
            auctionBid.setTimeBid(System.currentTimeMillis());
            repository.save(auctionBid);

            if (L2World.getInstance().getPlayer(_highestBidderName) != null) {
                L2World.getInstance().getPlayer(_highestBidderName).sendMessage("You have been out bidded");
            }
        }
        _highestBidderId = bidder.getClanId();
        _highestBidderMaxBid = bid;
        _highestBidderName = bidder.getClan().getLeaderName();
        if (_bidders.containsKey(_highestBidderId)) {
            _bidders.put(_highestBidderId, auctionBid);
        } else {
            _bidders.get(_highestBidderId).setMaxBid(bid);
            _bidders.get(_highestBidderId).setTimeBid(Calendar.getInstance().getTimeInMillis());
        }
        bidder.sendMessage("You have bidded successfully");
    }

    private void removeBids() {
        AuctionBidRepository repository = getRepository(AuctionBidRepository.class);
        repository.deleteByAuction(getId());
        for (AuctionBid b : _bidders.values()) {
            L2Clan clan = ClanTable.getInstance().getClanByName(b.getClanName());
            if (clan.getHasHideout() == 0) {
                returnItem(b.getClanName(), 57, (9 * b.getMaxBid()) / 10, false); // 10 % tax
            } else {
                L2PcInstance player = L2World.getInstance().getPlayer(b.getBidderName());
                if (player != null) {
                    player.sendMessage("Congratulation you have won ClanHall!");
                }
            }
            clan.setAuctionBiddedAt(0, true);
        }
        _bidders.clear();
    }

    public void deleteAuctionFromDB() {
        AuctionManager.getInstance().getAuctions().remove(this);
        AuctionRepository repository = getRepository(AuctionRepository.class);
        repository.deleteByItemId(entity.getItemId());
    }

    private void endAuction() {
        if (ClanHallManager.getInstance().loaded()) {
            if ((_highestBidderId == 0) && (entity.getSellerId() == 0)) {
                startAutoTask();
                return;
            }
            if ((_highestBidderId == 0) && (entity.getSellerId() > 0)) {
                int aucId = AuctionManager.getInstance().getAuctionIndex(_id);
                AuctionManager.getInstance().getAuctions().remove(aucId);
                return;
            }
            if (entity.getSellerId() > 0) {
                returnItem(entity.getSellerClanName(), 57, _highestBidderMaxBid, true);
                returnItem(entity.getSellerClanName(), 57, ClanHallManager.getInstance().getClanHallById(entity.getItemId()).getLease(), false);
            }
            deleteAuctionFromDB();
            L2Clan Clan = ClanTable.getInstance().getClanByName(_bidders.get(_highestBidderId).getClanName());
            _bidders.remove(_highestBidderId);
            Clan.setAuctionBiddedAt(0, true);
            removeBids();
            ClanHallManager.getInstance().setOwner(entity.getItemId(), Clan);
        } else {
            ThreadPoolManager.getInstance().scheduleGeneral(new AutoEndTask(), 3000);
        }
    }

    public void cancelBid(int bidder) {
        AuctionBidRepository repository = getRepository(AuctionBidRepository.class);
        repository.deleteByAuctionAndBidder(getId(), bidder);

        returnItem(_bidders.get(bidder).getClanName(), 57, _bidders.get(bidder).getMaxBid(), true);
        ClanTable.getInstance().getClanByName(_bidders.get(bidder).getClanName()).setAuctionBiddedAt(0, true);
        _bidders.clear();
        loadBid();
    }

    public void cancelAuction() {
        deleteAuctionFromDB();
        removeBids();
    }

    public void confirmAuction() {
        AuctionManager.getInstance().getAuctions().add(this);
        AuctionEntity auctionEntity = new AuctionEntity();
        auctionEntity.setId(getId());
        auctionEntity.setItemQuantity(_itemQuantity);

        AuctionRepository repository = getRepository(AuctionRepository.class);
        repository.save(auctionEntity);
        loadBid();
    }

    public final int getId() {
        return _id;
    }

    public final long getEndDate() {
        return entity.getEndDate();
    }

    private int getHighestBidderId() {
        return _highestBidderId;
    }

    private String getHighestBidderName() {
        return _highestBidderName;
    }

    public final int getHighestBidderMaxBid() {
        return _highestBidderMaxBid;
    }

    public final int getItemId() {
        return entity.getItemId();
    }

    public final String getItemName() {
        return entity.getItemName();
    }

    public final String getItemType() {
        return entity.getItemType();
    }

    public final String getSellerName() {
        return entity.getSellerName();
    }

    public final String getSellerClanName() {
        return entity.getSellerClanName();
    }

    public final int getStartingBid() {
        return entity.getStartingBid();
    }

    public final Map<Integer, AuctionBid> getBidders() {
        return _bidders;
    }
}