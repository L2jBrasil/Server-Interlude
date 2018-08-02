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

import com.l2jbr.commons.database.DatabaseAccess;
import com.l2jbr.gameserver.model.entity.Auction;
import com.l2jbr.gameserver.model.entity.database.repository.AuctionRepository;
import com.l2jbr.gameserver.model.entity.database.repository.ClanHallRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class AuctionManager {
    protected static final Logger _log = LoggerFactory.getLogger(AuctionManager.class);
    private static AuctionManager _instance;
    private final List<Auction> _auctions;

    public static AuctionManager getInstance() {
        if (_instance == null) {
            _log.info("Initializing AuctionManager");
            _instance = new AuctionManager();
        }
        return _instance;
    }

    private AuctionManager() {
        _auctions = new ArrayList<>();
        load();
    }

    public void reload() {
        _auctions.clear();
        load();
    }

    private final void load() {
        AuctionRepository repository = DatabaseAccess.getRepository(AuctionRepository.class);
        repository.findAll().forEach(auctionData -> {
            _auctions.add(new Auction(auctionData));
        });
    }

    public final Auction getAuction(int auctionId) {
        int index = getAuctionIndex(auctionId);
        if (index >= 0) {
            return getAuctions().get(index);
        }
        return null;
    }

    public final int getAuctionIndex(int auctionId) {
        Auction auction;
        for (int i = 0; i < getAuctions().size(); i++) {
            auction = getAuctions().get(i);
            if ((auction != null) && (auction.getId() == auctionId)) {
                return i;
            }
        }
        return -1;
    }

    public final List<Auction> getAuctions() {
        return _auctions;
    }

    public void initNPC(int id) {
        int startingBid = 0;

        if(id >= 23 && id <= 30) {
            startingBid = 20000000;
        } else if(id >= 31 && id <= 33) {
            startingBid = 8000000;
        } else if(id >= 36 && id <= 61) {
            startingBid = 50000000;
        } else {
            _log.warn("Clan Hall auction not found for Id: {}", id);
            return;
        }

        ClanHallRepository repository = DatabaseAccess.getRepository(ClanHallRepository.class);
        repository.updateWithInitialData(id, startingBid);
        _auctions.add(new Auction(id));
    }
}
