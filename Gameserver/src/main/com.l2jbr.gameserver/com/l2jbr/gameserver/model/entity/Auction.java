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

import com.l2jbr.commons.database.DatabaseAccess;
import com.l2jbr.gameserver.GameServer;
import com.l2jbr.gameserver.ThreadPoolManager;
import com.l2jbr.gameserver.datatables.ClanTable;
import com.l2jbr.gameserver.idfactory.IdFactory;
import com.l2jbr.gameserver.instancemanager.AuctionManager;
import com.l2jbr.gameserver.instancemanager.ClanHallManager;
import com.l2jbr.gameserver.model.L2Clan;
import com.l2jbr.gameserver.model.L2World;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.model.database.AuctionBid;
import com.l2jbr.gameserver.model.database.AuctionData;
import com.l2jbr.gameserver.model.database.repository.AuctionBidRepository;
import com.l2jbr.gameserver.model.database.repository.AuctionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

public class Auction {
    protected static final Logger _log = LoggerFactory.getLogger(Auction.class.getName());
    private int _id = 0;
    private final int _adenaId = 57;
    private long _endDate;
    private int _highestBidderId = 0;
    private String _highestBidderName = "";
    private int _highestBidderMaxBid = 0;
    private int _itemId = 0;
    private String _itemName = "";
    private int _itemObjectId = 0;
    private final int _itemQuantity = 0;
    private String _itemType = "";
    private int _sellerId = 0;
    private String _sellerClanName = "";
    private String _sellerName = "";
    private int _currentBid = 0;
    private int _startingBid = 0;

    private final Map<Integer, Bidder> _bidders = new LinkedHashMap<>();
    private static final String[] ItemTypeName =  { "ClanHall" };


    public Auction(int auctionId) {
        _id = auctionId;
        load();
        startAutoTask();
    }

    public Auction(int itemId, L2Clan Clan, long delay, int bid, String name) {
        _id = itemId;
        _endDate = System.currentTimeMillis() + delay;
        _itemId = itemId;
        _itemName = name;
        _itemType = "ClanHall";
        _sellerId = Clan.getLeaderId();
        _sellerName = Clan.getLeaderName();
        _sellerClanName = Clan.getName();
        _startingBid = bid;
    }

    public Auction(AuctionData auctionData) {
        load(auctionData);
        startAutoTask();
    }
    public static enum ItemTypeEnum {
        ClanHall

    }
    public class Bidder {
        private final String _name;
        private final String _clanName;
        private int _bid;

        private final Calendar _timeBid;

        public Bidder(String name, String clanName, int bid, long timeBid) {
            _name = name;
            _clanName = clanName;
            _bid = bid;
            _timeBid = Calendar.getInstance();
            _timeBid.setTimeInMillis(timeBid);
        }

        public String getName() {
            return _name;
        }

        public String getClanName() {
            return _clanName;
        }

        public int getBid() {
            return _bid;
        }

        public Calendar getTimeBid() {
            return _timeBid;
        }

        public void setTimeBid(long timeBid) {
            _timeBid.setTimeInMillis(timeBid);
        }
        public void setBid(int bid) {
            _bid = bid;
        }

    }
    /**
     * Task Sheduler for endAuction
     */
    public class AutoEndTask implements Runnable {

        public AutoEndTask() {
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
        AuctionRepository repository = DatabaseAccess.getRepository(AuctionRepository.class);
        repository.findById(getId()).ifPresent(this::load);
    }

    private void load(AuctionData auctionData) {
        _currentBid = auctionData.getCurrentBid();
        _endDate = auctionData.getEndDate();
        _itemId = auctionData.getItemId();
        _itemName = auctionData.getItemName();
        _itemObjectId = auctionData.getItemObjectId();
        _itemType = auctionData.getItemType();
        _sellerId = auctionData.getSellerId();
        _sellerClanName = auctionData.getSellerClanName();
        _sellerName = auctionData.getSellerName();
        _startingBid = auctionData.getStartingBid();
        loadBid();
    }


    private void loadBid() {
        AuctionBidRepository repository = DatabaseAccess.getRepository(AuctionBidRepository.class);
        repository.findByAuctionId(getId()).forEach(auctionBid -> {
            if(_highestBidderId == 0) {
                _highestBidderId = auctionBid.getBidderId();
                _highestBidderName = auctionBid.getBidderName();
                _highestBidderMaxBid = auctionBid.getMaxBid();
            }
            Bidder bidder = new Bidder(auctionBid.getBidderName(), auctionBid.getClanName(), auctionBid.getMaxBid(), auctionBid.getTimeBid());
            _bidders.put(auctionBid.getBidderId(), bidder);

        });
    }


    private void startAutoTask() {
        long currentTime = System.currentTimeMillis();
        long taskDelay = 0;
        if (_endDate <= currentTime) {
            _endDate = currentTime + (7 * 24 * 60 * 60 * 1000);
            saveAuctionDate();
        } else {
            taskDelay = _endDate - currentTime;
        }
        ThreadPoolManager.getInstance().scheduleGeneral(new AutoEndTask(), taskDelay);
    }

    public static String getItemTypeName(ItemTypeEnum value) {
        return ItemTypeName[value.ordinal()];
    }

    private void saveAuctionDate() {
        AuctionRepository repository = DatabaseAccess.getRepository(AuctionRepository.class);
        repository.updateEndDateById(_id, _endDate);
    }

    /**
     * Set a bid
     *
     * @param bidder
     * @param bid
     */
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

    /**
     * Return Item in WHC
     *
     * @param Clan
     * @param itemId
     * @param quantity
     * @param penalty
     */
    private void returnItem(String Clan, int itemId, int quantity, boolean penalty) {
        if (penalty) {
            quantity *= 0.9; // take 10% tax fee if needed
        }
        ClanTable.getInstance().getClanByName(Clan).getWarehouse().addItem("Outbidded", _adenaId, quantity, null, null);
    }

    /**
     * Take Item in WHC
     *
     * @param bidder
     * @param itemId
     * @param quantity
     * @return
     */
    private boolean takeItem(L2PcInstance bidder, int itemId, int quantity) {
        if ((bidder.getClan() != null) && (bidder.getClan().getWarehouse().getAdena() >= quantity)) {
            bidder.getClan().getWarehouse().destroyItemByItemId("Buy", _adenaId, quantity, bidder, bidder);
            return true;
        }
        bidder.sendMessage("You do not have enough adena");
        return false;
    }

    /**
     * Update auction in DB
     *
     * @param bidder
     * @param bid
     */
    private void updateInDB(L2PcInstance bidder, int bid) {
        AuctionBidRepository repository = DatabaseAccess.getRepository(AuctionBidRepository.class);
        if (getBidders().get(bidder.getClanId()) != null) {
            repository.updateByAuctionAndBidder(getId(), bidder.getClanId(), bidder.getClan().getLeaderName(), bid, System.currentTimeMillis());
        } else {
            AuctionBid auctionBid = new AuctionBid();
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
        if (_bidders.get(_highestBidderId) == null) {
            _bidders.put(_highestBidderId, new Bidder(_highestBidderName, bidder.getClan().getName(), bid, Calendar.getInstance().getTimeInMillis()));
        } else {
            _bidders.get(_highestBidderId).setBid(bid);
            _bidders.get(_highestBidderId).setTimeBid(Calendar.getInstance().getTimeInMillis());
        }
        bidder.sendMessage("You have bidded successfully");
    }

    private void removeBids() {
        AuctionBidRepository repository = DatabaseAccess.getRepository(AuctionBidRepository.class);
        repository.deleteByAuction(getId());
        for (Bidder b : _bidders.values()) {
            L2Clan clan = ClanTable.getInstance().getClanByName(b.getClanName());
            if (clan.getHasHideout() == 0) {
                returnItem(b.getClanName(), 57, (9 * b.getBid()) / 10, false); // 10 % tax
            } else {
                L2PcInstance player = L2World.getInstance().getPlayer(b.getName());
                if (player != null) {
                    player.sendMessage("Congratulation you have won ClanHall!");
                }
            }
            clan.setAuctionBiddedAt(0, true);
        }
        _bidders.clear();
    }

    /**
     * Remove auction
     */
    public void deleteAuctionFromDB() {
        AuctionManager.getInstance().getAuctions().remove(this);
        AuctionRepository repository = DatabaseAccess.getRepository(AuctionRepository.class);
        repository.deleteByItemId(_itemId);
    }

    /**
     * End of auction
     */
    public void endAuction() {
        if ((GameServer.gameServer.getCHManager() != null) && GameServer.gameServer.getCHManager().loaded()) {
            if ((_highestBidderId == 0) && (_sellerId == 0)) {
                startAutoTask();
                return;
            }
            if ((_highestBidderId == 0) && (_sellerId > 0)) {
                /**
                 * If seller haven't sell ClanHall, auction removed, THIS MUST BE CONFIRMED
                 */
                int aucId = AuctionManager.getInstance().getAuctionIndex(_id);
                AuctionManager.getInstance().getAuctions().remove(aucId);
                return;
            }
            if (_sellerId > 0) {
                returnItem(_sellerClanName, 57, _highestBidderMaxBid, true);
                returnItem(_sellerClanName, 57, ClanHallManager.getInstance().getClanHallById(_itemId).getLease(), false);
            }
            deleteAuctionFromDB();
            L2Clan Clan = ClanTable.getInstance().getClanByName(_bidders.get(_highestBidderId).getClanName());
            _bidders.remove(_highestBidderId);
            Clan.setAuctionBiddedAt(0, true);
            removeBids();
            ClanHallManager.getInstance().setOwner(_itemId, Clan);
        } else {
            /** Task waiting ClanHallManager is loaded every 3s */
            ThreadPoolManager.getInstance().scheduleGeneral(new AutoEndTask(), 3000);
        }
    }

    public void cancelBid(int bidder) {
        AuctionBidRepository repository = DatabaseAccess.getRepository(AuctionBidRepository.class);
        repository.deleteByAuctionAndBidder(getId(), bidder);

        returnItem(_bidders.get(bidder).getClanName(), 57, _bidders.get(bidder).getBid(), true);
        ClanTable.getInstance().getClanByName(_bidders.get(bidder).getClanName()).setAuctionBiddedAt(0, true);
        _bidders.clear();
        loadBid();
    }

    /**
     * Cancel auction
     */
    public void cancelAuction() {
        deleteAuctionFromDB();
        removeBids();
    }

    public void confirmAuction() {
        AuctionManager.getInstance().getAuctions().add(this);
        AuctionData auctionData = new AuctionData();
        auctionData.setId(getId());
        auctionData.setSellerId(_sellerId);
        auctionData.setSellerName(_sellerName);
        auctionData.setSellerClanName(_sellerClanName);
        auctionData.setItemType(_itemType);
        auctionData.setItemId(_itemId);
        auctionData.setItemObjectId(_itemObjectId);
        auctionData.setItemName(_itemName);
        auctionData.setItemQuantity(_itemQuantity);
        auctionData.setStartingBid(_startingBid);
        auctionData.setCurrentBid(_currentBid);
        auctionData.setEndDate(_endDate);

        AuctionRepository repository = DatabaseAccess.getRepository(AuctionRepository.class);
        repository.save(auctionData);
        loadBid();
    }

    public final int getId() {
        return _id;
    }

    public final int getCurrentBid() {
        return _currentBid;
    }

    public final long getEndDate() {
        return _endDate;
    }

    public final int getHighestBidderId() {
        return _highestBidderId;
    }

    public final String getHighestBidderName() {
        return _highestBidderName;
    }

    public final int getHighestBidderMaxBid() {
        return _highestBidderMaxBid;
    }

    public final int getItemId() {
        return _itemId;
    }

    public final String getItemName() {
        return _itemName;
    }

    public final int getItemObjectId() {
        return _itemObjectId;
    }

    public final int getItemQuantity() {
        return _itemQuantity;
    }

    public final String getItemType() {
        return _itemType;
    }

    public final int getSellerId() {
        return _sellerId;
    }

    public final String getSellerName() {
        return _sellerName;
    }

    public final String getSellerClanName() {
        return _sellerClanName;
    }

    public final int getStartingBid() {
        return _startingBid;
    }

    public final Map<Integer, Bidder> getBidders() {
        return _bidders;
    }
}