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

import com.l2jbr.commons.Config;
import com.l2jbr.commons.database.DatabaseAccess;
import com.l2jbr.commons.util.Util;
import com.l2jbr.gameserver.Announcements;
import com.l2jbr.gameserver.CastleUpdater;
import com.l2jbr.gameserver.SevenSigns;
import com.l2jbr.gameserver.ThreadPoolManager;
import com.l2jbr.gameserver.datatables.ClanTable;
import com.l2jbr.gameserver.datatables.DoorTable;
import com.l2jbr.gameserver.instancemanager.CastleManager;
import com.l2jbr.gameserver.instancemanager.CastleManorManager;
import com.l2jbr.gameserver.model.L2Clan;
import com.l2jbr.gameserver.model.L2Manor;
import com.l2jbr.gameserver.model.L2Object;
import com.l2jbr.gameserver.model.actor.instance.L2DoorInstance;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.model.entity.database.CastleDoor;
import com.l2jbr.gameserver.model.entity.database.CropProcure;
import com.l2jbr.gameserver.model.entity.database.SeedProduction;
import com.l2jbr.gameserver.model.entity.database.castleEntity;
import com.l2jbr.gameserver.model.entity.database.repository.CastleManorProcureRepository;
import com.l2jbr.gameserver.model.entity.database.repository.CastleManorProductionRepository;
import com.l2jbr.gameserver.model.entity.database.repository.CastleRepository;
import com.l2jbr.gameserver.model.entity.database.repository.ClanRepository;
import com.l2jbr.gameserver.model.zone.type.L2CastleZone;
import com.l2jbr.gameserver.serverpackets.PledgeShowInfoUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Castle {
    protected static Logger _log = LoggerFactory.getLogger(Castle.class.getName());

    private List<CropProcure> _procure = new ArrayList<>();
    private List<SeedProduction> _production = new ArrayList<>();
    private List<CropProcure> _procureNext = new ArrayList<>();
    private List<SeedProduction> _productionNext = new ArrayList<>();
    private boolean _isNextPeriodApproved = false;

    private final List<L2DoorInstance> _doors = new ArrayList<>();
    private final List<CastleDoor> _doorDefault = new ArrayList<>();
    private int _ownerId = 0;
    private Siege _siege = null;
    private Calendar _siegeDate;
    private double _taxRate;
    private L2CastleZone _zone;
    private L2Clan _formerOwner = null;
    private int _nbArtifact = 1;
    private final Map<Integer, Integer> _engrave = new HashMap<>();
    private castleEntity entity;

    public Castle(castleEntity castleEntity) {
        entity = castleEntity;

        if (entity.getId() == 7 || entity.getId() == 9) {
            _nbArtifact = 2;
        }

        _siegeDate = Calendar.getInstance();
        _siegeDate.setTimeInMillis(castleEntity.getSiegeDate());

        _taxRate = entity.getTaxPercent() / 100.0;

        ClanRepository clanRepository = DatabaseAccess.getRepository(ClanRepository.class);
        clanRepository.findClanIdByCastle(entity.getId()).ifPresent(clanId -> {
            _ownerId = clanId;
            L2Clan clan = ClanTable.getInstance().getClan(getOwnerId()); // Try to find clan instance
            ThreadPoolManager.getInstance().scheduleGeneral(new CastleUpdater(clan, 1), 3600000); // Schedule owner tasks to start running
        });

        for (CastleDoor castleDoor : castleEntity.getDoors()) {

            _doorDefault.add(castleDoor);

            L2DoorInstance door = DoorTable.parseDoor(castleDoor);
            door.spawnMe(door.getX(), door.getY(), door.getZ());
            _doors.add(door);
            DoorTable.getInstance().putDoor(door);
        }
    }

    public void Engrave(L2Clan clan, int objId) {
        _engrave.put(objId, clan.getClanId());
        if (_engrave.size() == _nbArtifact) {
            boolean rst = true;
            for (int id : _engrave.values()) {
                if (id != clan.getClanId()) {
                    rst = false;
                }
            }
            if (rst) {
                _engrave.clear();
                setOwner(clan);
            } else {
                getSiege().announceToPlayer("Clan " + clan.getName() + " has finished to engrave one of the rulers.", true);
            }
        } else {
            getSiege().announceToPlayer("Clan " + clan.getName() + " has finished to engrave one of the rulers.", true);
        }
    }

    // This method add to the treasury

    /**
     * Add amount to castle instance's treasury (warehouse).
     *
     * @param amount
     */
    public void addToTreasury(int amount) {
        if (getOwnerId() <= 0) {
            return;
        }

        if (entity.getName().equalsIgnoreCase("Schuttgart") || entity.getName().equalsIgnoreCase("Goddard")) {
            Castle rune = CastleManager.getInstance().getCastle("rune");
            if (rune != null) {
                int runeTax = (int) (amount * rune.getTaxRate());
                if (rune.getOwnerId() > 0) {
                    rune.addToTreasury(runeTax);
                }
                amount -= runeTax;
            }
        }
        if (!entity.getName().equalsIgnoreCase("aden") && !entity.getName().equalsIgnoreCase("Rune") && !entity.getName().equalsIgnoreCase("Schuttgart") && !entity.getName().equalsIgnoreCase("Goddard")) // If current castle instance is not Aden, Rune, Goddard or Schuttgart.
        {
            Castle aden = CastleManager.getInstance().getCastle("aden");
            if (aden != null) {
                int adenTax = (int) (amount * aden.getTaxRate()); // Find out what Aden gets from the current castle instance's income
                if (aden.getOwnerId() > 0) {
                    aden.addToTreasury(adenTax); // Only bother to really add the tax to the treasury if not npc owned
                }

                amount -= adenTax; // Subtract Aden's income from current castle instance's income
            }
        }

        addToTreasuryNoTax(amount);
    }

    public boolean addToTreasuryNoTax(int amount) {
        if (getOwnerId() <= 0) {
            return false;
        }

        if (amount < 0) {
            amount *= -1;
            if (getTreasury() < amount) {
                return false;
            }
            entity.setTreasury(getTreasury() - amount);
        } else {
            if (((long) getTreasury() + amount) > Integer.MAX_VALUE) {
                entity.setTreasury(Integer.MAX_VALUE);
            } else {
                entity.setTreasury(getTreasury() + amount);
            }
        }

        CastleRepository repository = DatabaseAccess.getRepository(CastleRepository.class);
        repository.save(entity);
        return true;
    }

    /**
     * Move non clan members off castle area and to nearest town.<BR>
     * <BR>
     */
    public void banishForeigners() {
        _zone.banishForeigners(getOwnerId());
    }

    /**
     * @param x
     * @param y
     * @param z
     * @return true if object is inside the zone
     */
    public boolean checkIfInZone(int x, int y, int z) {
        return _zone.isInsideZone(x, y, z);
    }

    /**
     * Sets this castles zone
     *
     * @param zone
     */
    public void setZone(L2CastleZone zone) {
        _zone = zone;
    }

    public L2CastleZone getZone() {
        return _zone;
    }

    /**
     * Get the objects distance to this castle
     *
     * @param obj
     * @return
     */
    public double getDistance(L2Object obj) {
        return _zone.getDistanceToZone(obj);
    }

    public void closeDoor(L2PcInstance activeChar, int doorId) {
        openCloseDoor(activeChar, doorId, false);
    }

    public void openDoor(L2PcInstance activeChar, int doorId) {
        openCloseDoor(activeChar, doorId, true);
    }

    public void openCloseDoor(L2PcInstance activeChar, int doorId, boolean open) {
        if (activeChar.getClanId() != getOwnerId()) {
            return;
        }

        L2DoorInstance door = getDoor(doorId);
        if (door != null) {
            if (open) {
                door.openMe();
            } else {
                door.closeMe();
            }
        }
    }

    // This method updates the castle tax rate
    public void setOwner(L2Clan clan) {
        // Remove old owner
        if ((getOwnerId() > 0) && ((clan == null) || (clan.getClanId() != getOwnerId()))) {
            L2Clan oldOwner = ClanTable.getInstance().getClan(getOwnerId()); // Try to find clan instance
            if (oldOwner != null) {
                if (_formerOwner == null) {
                    _formerOwner = oldOwner;
                    if (Config.REMOVE_CASTLE_CIRCLETS) {
                        CastleManager.getInstance().removeCirclet(_formerOwner, getCastleId());
                    }
                }
                oldOwner.setCastle(0); // Unset has castle flag for old owner
                Announcements.getInstance().announceToAll(oldOwner.getName() + " has lost " + getName() + " castle!");
            }
        }

        updateOwnerInDB(clan); // Update in database

        if (getSiege().getIsInProgress()) {
            getSiege().midVictory(); // Mid victory phase of siege
        }

        updateClansReputation();
    }

    public void removeOwner(L2Clan clan) {
        if (clan != null) {
            _formerOwner = clan;
            if (Config.REMOVE_CASTLE_CIRCLETS) {
                CastleManager.getInstance().removeCirclet(_formerOwner, getCastleId());
            }
            clan.setCastle(0);
            Announcements.getInstance().announceToAll(clan.getName() + " has lost " + getName() + " castle");
            clan.broadcastToOnlineMembers(new PledgeShowInfoUpdate(clan));
        }

        updateOwnerInDB(null);
        if (getSiege().getIsInProgress()) {
            getSiege().midVictory();
        }

        updateClansReputation();
    }

    // This method updates the castle tax rate
    public void setTaxPercent(L2PcInstance activeChar, int taxPercent) {
        int maxTax;
        switch (SevenSigns.getInstance().getSealOwner(SevenSigns.SEAL_STRIFE)) {
            case SevenSigns.CABAL_DAWN:
                maxTax = 25;
                break;
            case SevenSigns.CABAL_DUSK:
                maxTax = 5;
                break;
            default: // no owner
                maxTax = 15;
        }

        if ((taxPercent < 0) || (taxPercent > maxTax)) {
            activeChar.sendMessage("Tax value must be between 0 and " + maxTax + ".");
            return;
        }

        setTaxPercent(taxPercent);
        activeChar.sendMessage(getName() + " castle tax changed to " + taxPercent + "%.");
    }

    public void setTaxPercent(int taxPercent) {
        entity.setTaxPercent(taxPercent);
        _taxRate = taxPercent / 100.0;

        CastleRepository repository = DatabaseAccess.getRepository(CastleRepository.class);
        repository.save(entity);
    }

    /**
     * Respawn all doors on castle grounds<BR>
     * <BR>
     */
    public void spawnDoor() {
        spawnDoor(false);
    }

    /**
     * Respawn all doors on castle grounds
     *
     * @param isDoorWeak
     */
    public void spawnDoor(boolean isDoorWeak) {
        for (int i = 0; i < getDoors().size(); i++) {
            L2DoorInstance door = getDoors().get(i);
            if (door.getCurrentHp() <= 0) {
                door.decayMe(); // Kill current if not killed already
                door = DoorTable.parseDoor(_doorDefault.get(i));
                if (isDoorWeak) {
                    door.setCurrentHp(door.getMaxHp() / 2);
                }
                door.spawnMe(door.getX(), door.getY(), door.getZ());
                getDoors().set(i, door);
            } else if (door.getOpen() == 0) {
                door.closeMe();
            }
        }
    }

    private void updateOwnerInDB(L2Clan clan) {
        if (clan != null) {
            _ownerId = clan.getClanId(); // Update owner id property
        } else {
            _ownerId = 0; // Remove owner
        }

        ClanRepository repository = DatabaseAccess.getRepository(ClanRepository.class);
        repository.removeClastle(getCastleId());
        repository.updateCastleById(getOwnerId(), getCastleId());

        // Announce to clan memebers
        if (clan != null) {
            clan.setCastle(getCastleId()); // Set has castle flag for new owner
            Announcements.getInstance().announceToAll(clan.getName() + " has taken " + getName() + " castle!");
            clan.broadcastToOnlineMembers(new PledgeShowInfoUpdate(clan));

            ThreadPoolManager.getInstance().scheduleGeneral(new CastleUpdater(clan, 1), 3600000); // Schedule owner tasks to start running
        }
    }


    public final int getCastleId() {
        return Objects.requireNonNullElse(entity.getId(), 0);
    }

    public final L2DoorInstance getDoor(int doorId) {
        if (doorId <= 0) {
            return null;
        }

        for (int i = 0; i < getDoors().size(); i++) {
            L2DoorInstance door = getDoors().get(i);
            if (door.getDoorId() == doorId) {
                return door;
            }
        }
        return null;
    }

    public final List<L2DoorInstance> getDoors() {
        return _doors;
    }

    public final String getName() {
        return entity.getName();
    }

    public final int getOwnerId() {
        return _ownerId;
    }

    public final Siege getSiege() {
        if (_siege == null) {
            _siege = new Siege(new Castle[]
                    {
                            this
                    });
        }
        return _siege;
    }

    public final Calendar getSiegeDate() {
        return _siegeDate;
    }

    public final int getSiegeDayOfWeek() {
        return entity.getSiegeDayOfWeek();
    }

    public final int getSiegeHourOfDay() {
        return entity.getSiegeHourOfDay();
    }

    public final int getTaxPercent() {
        return entity.getTaxPercent();
    }

    public final double getTaxRate() {
        return _taxRate;
    }

    public final int getTreasury() {
        return entity.getTreasury();
    }

    public List<SeedProduction> getSeedProduction(int period) {
        return (period == CastleManorManager.PERIOD_CURRENT ? _production : _productionNext);
    }

    public List<CropProcure> getCropProcure(int period) {
        return (period == CastleManorManager.PERIOD_CURRENT ? _procure : _procureNext);
    }

    public void setSeedProduction(List<SeedProduction> seed, int period) {
        if (period == CastleManorManager.PERIOD_CURRENT) {
            _production = seed;
        } else {
            _productionNext = seed;
        }
    }

    public void setCropProcure(List<CropProcure> crop, int period) {
        if (period == CastleManorManager.PERIOD_CURRENT) {
            _procure = crop;
        } else {
            _procureNext = crop;
        }
    }

    public synchronized SeedProduction getSeed(int seedId, int period) {
        for (SeedProduction seed : getSeedProduction(period)) {
            if (seed.getSeedId() == seedId) {
                return seed;
            }
        }
        return null;
    }

    public synchronized CropProcure getCrop(int cropId, int period) {
        for (CropProcure crop : getCropProcure(period)) {
            if (crop.getCropId() == cropId) {
                return crop;
            }
        }
        return null;
    }

    public int getManorCost(int period) {
        List<CropProcure> procure;
        List<SeedProduction> production;

        if (period == CastleManorManager.PERIOD_CURRENT) {
            procure = _procure;
            production = _production;
        } else {
            procure = _procureNext;
            production = _productionNext;
        }

        int total = 0;
        if (production != null) {
            for (SeedProduction seed : production) {
                total += L2Manor.getInstance().getSeedBuyPrice(seed.getSeedId()) * seed.getStartAmount();
            }
        }
        if (procure != null) {
            for (CropProcure crop : procure) {
                total += crop.getPrice() * crop.getStartAmount();
            }
        }
        return total;
    }

    public void saveSeedData() {
        saveSeedData(CastleManorManager.PERIOD_CURRENT);
        saveSeedData(CastleManorManager.PERIOD_NEXT);
    }

    public void saveSeedData(int period) {
        CastleManorProductionRepository repository = DatabaseAccess.getRepository(CastleManorProductionRepository.class);
        repository.deleteByIdAndPeriod(getCastleId(), period);

        List<SeedProduction> seedProductions = getSeedProduction(period);

        if (!Util.isNullOrEmpty(seedProductions)) {
            for (SeedProduction s : seedProductions) {
                SeedProduction production = new SeedProduction(getCastleId(), s.getSeedId(), s.getAmount(), s.getStartAmount(), s.getPrice(), period);
                repository.save(production);
            }
        }
    }

    public void saveCropData() {
        saveCropData(CastleManorManager.PERIOD_CURRENT);
        saveCropData(CastleManorManager.PERIOD_NEXT);
    }

    public void saveCropData(int period) {
        CastleManorProcureRepository repository = DatabaseAccess.getRepository(CastleManorProcureRepository.class);
        repository.deleteByIdAndPeriod(getCastleId(), period);

        List<CropProcure> procures = getCropProcure(period);

        if (!Util.isNullOrEmpty(procures)) {
            for (CropProcure cp : procures) {
                CropProcure procure = new CropProcure(getCastleId(), cp.getCropId(), cp.getAmount(),
                        cp.getStartAmount(), cp.getPrice(), cp.getReward(), period);
                repository.save(procure);
            }
        }
    }

    public void updateCrop(int cropId, int amount, int period) {
        CastleManorProcureRepository repository = DatabaseAccess.getRepository(CastleManorProcureRepository.class);
        repository.updateCanBuyCrop(getCastleId(), cropId, period, amount);
    }

    public void updateSeed(int seedId, int amount, int period) {
        CastleManorProductionRepository repository = DatabaseAccess.getRepository(CastleManorProductionRepository.class);
        repository.updateSeedAmountInPeriod(getCastleId(), seedId, amount, period);
    }

    public boolean isNextPeriodApproved() {
        return _isNextPeriodApproved;
    }

    public void setNextPeriodApproved(boolean val) {
        _isNextPeriodApproved = val;
    }

    public void updateClansReputation() {
        if (_formerOwner != null) {
            if (_formerOwner != ClanTable.getInstance().getClan(getOwnerId())) {
                int maxreward = Math.max(0, _formerOwner.getReputationScore());
                _formerOwner.setReputationScore(_formerOwner.getReputationScore() - 1000, true);
                L2Clan owner = ClanTable.getInstance().getClan(getOwnerId());
                if (owner != null) {
                    owner.setReputationScore(owner.getReputationScore() + Math.min(1000, maxreward), true);
                    owner.broadcastToOnlineMembers(new PledgeShowInfoUpdate(owner));
                }
            } else {
                _formerOwner.setReputationScore(_formerOwner.getReputationScore() + 500, true);
            }

            _formerOwner.broadcastToOnlineMembers(new PledgeShowInfoUpdate(_formerOwner));
        } else {
            L2Clan owner = ClanTable.getInstance().getClan(getOwnerId());
            if (owner != null) {
                owner.setReputationScore(owner.getReputationScore() + 1000, true);
                owner.broadcastToOnlineMembers(new PledgeShowInfoUpdate(owner));
            }
        }
    }
}