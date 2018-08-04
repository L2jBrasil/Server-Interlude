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
import com.l2jbr.commons.util.Rnd;
import com.l2jbr.gameserver.ThreadPoolManager;
import com.l2jbr.gameserver.datatables.ClanTable;
import com.l2jbr.gameserver.model.*;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.model.entity.database.CropProcure;
import com.l2jbr.gameserver.model.entity.database.SeedProduction;
import com.l2jbr.gameserver.model.entity.database.repository.CastleManorProcureRepository;
import com.l2jbr.gameserver.model.entity.database.repository.CastleManorProductionRepository;
import com.l2jbr.gameserver.model.entity.Castle;
import com.l2jbr.gameserver.network.SystemMessageId;
import com.l2jbr.gameserver.serverpackets.SystemMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

/**
 * Class For Castle Manor Manager Load manor data from DB Update/Reload/Delete Handles all schedule for manor
 *
 * @author l3x
 */
public class CastleManorManager {
    protected static Logger _log = LoggerFactory.getLogger(CastleManorManager.class);

    private static CastleManorManager _instance;

    public static final int PERIOD_CURRENT = 0;
    public static final int PERIOD_NEXT = 1;

    private static final int NEXT_PERIOD_APPROVE = Config.ALT_MANOR_APPROVE_TIME; // 6:00
    private static final int NEXT_PERIOD_APPROVE_MIN = Config.ALT_MANOR_APPROVE_MIN; //
    private static final int MANOR_REFRESH = Config.ALT_MANOR_REFRESH_TIME; // 20:00
    private static final int MANOR_REFRESH_MIN = Config.ALT_MANOR_REFRESH_MIN; //
    private static final long MAINTENANCE_PERIOD = Config.ALT_MANOR_MAINTENANCE_PERIOD; // 6 mins

    private Calendar _manorRefresh;
    private Calendar _periodApprove;

    private boolean _underMaintenance;
    private boolean _disabled;

    private ScheduledFuture<?> _scheduledManorRefresh;
    private ScheduledFuture<?> _scheduledMaintenanceEnd;
    private ScheduledFuture<?> _scheduledNextPeriodapprove;

    public static CastleManorManager getInstance() {
        if (isNull(_instance)) {
            _log.info("Initializing CastleManorManager");
            _instance = new CastleManorManager();
        }
        return _instance;
    }

    private CastleManorManager() {
        load(); // load data from database
        init(); // schedule all manor related events
        _underMaintenance = false;
        _disabled = !Config.ALLOW_MANOR;
        boolean isApproved = ((_periodApprove.getTimeInMillis() < Calendar.getInstance().getTimeInMillis()) && (_manorRefresh.getTimeInMillis() > Calendar.getInstance().getTimeInMillis()));
        for (Castle c : CastleManager.getInstance().getCastles()) {
            c.setNextPeriodApproved(isApproved);
        }
    }

    private void load() {
        CastleManorProductionRepository productionRepository = DatabaseAccess.getRepository(CastleManorProductionRepository.class);
        CastleManorProcureRepository procureRepository = DatabaseAccess.getRepository(CastleManorProcureRepository.class);
        for (Castle castle : CastleManager.getInstance().getCastles()) {

            loadSeedProduction(productionRepository, castle);
            loadSeedProcure(procureRepository, castle);

            _log.info(castle.getName() + ": Data loaded");
        }
    }

    private void loadSeedProcure(CastleManorProcureRepository procureRepository, Castle castle) {
        List<CropProcure> procure = new ArrayList<>();
        List<CropProcure> procureNext = new ArrayList<>();
        procureRepository.findAllByCastleId(castle.getCastleId()).forEach(manorProcure -> {
            if (manorProcure.getPeriod() == PERIOD_CURRENT) {
                procure.add(manorProcure);
            } else {
                procureNext.add(manorProcure);
            }
        });

        castle.setCropProcure(procure, PERIOD_CURRENT);
        castle.setCropProcure(procureNext, PERIOD_NEXT);
    }

    private void loadSeedProduction(CastleManorProductionRepository productionRepository, Castle castle) {
        List<SeedProduction> production = new ArrayList<>();
        List<SeedProduction> productionNext = new ArrayList<>();
        productionRepository.findAllByCastle(castle.getCastleId()).forEach(manorProduction -> {
            if (manorProduction.getPeriod() == PERIOD_CURRENT) {
                production.add(manorProduction);
            } else {
                productionNext.add(manorProduction);
            }
        });
        castle.setSeedProduction(production, PERIOD_CURRENT);
        castle.setSeedProduction(productionNext, PERIOD_NEXT);
    }

    protected void init() {
        _manorRefresh = Calendar.getInstance();
        _manorRefresh.set(Calendar.HOUR_OF_DAY, MANOR_REFRESH);
        _manorRefresh.set(Calendar.MINUTE, MANOR_REFRESH_MIN);

        _periodApprove = Calendar.getInstance();
        _periodApprove.set(Calendar.HOUR_OF_DAY, NEXT_PERIOD_APPROVE);
        _periodApprove.set(Calendar.MINUTE, NEXT_PERIOD_APPROVE_MIN);

        updateManorRefresh();
        updatePeriodApprove();
    }

    public void updateManorRefresh() {
        _log.info("Manor System: Manor refresh updated");
        _scheduledManorRefresh = ThreadPoolManager.getInstance().scheduleGeneral(() ->
        {
            if (!isDisabled()) {
                setUnderMaintenance(true);
                _log.info("Manor System: Under maintenance mode started");

                _scheduledMaintenanceEnd = ThreadPoolManager.getInstance().scheduleGeneral(() ->
                {
                    _log.info("Manor System: Next period started");
                    setNextPeriod();
                    try {
                        save();
                    } catch (Exception e) {
                        _log.info("Manor System: Failed to save manor data: " + e);
                    }
                    setUnderMaintenance(false);
                }, MAINTENANCE_PERIOD);
            }
            updateManorRefresh();
        }, getMillisToManorRefresh());

    }

    public void updatePeriodApprove() {
        _log.info("Manor System: Manor period approve updated");
        _scheduledNextPeriodapprove = ThreadPoolManager.getInstance().scheduleGeneral(() ->
        {
            if (!isDisabled()) {
                approveNextPeriod();
                _log.info("Manor System: Next period approved");
            }
            updatePeriodApprove();
        }, getMillisToNextPeriodApprove());
    }

    public long getMillisToManorRefresh() {
        if (_manorRefresh.getTimeInMillis() > Calendar.getInstance().getTimeInMillis()) {
            return (_manorRefresh.getTimeInMillis() - Calendar.getInstance().getTimeInMillis());
        }

        return setNewManorRefresh();
    }

    public long setNewManorRefresh() {
        _manorRefresh = Calendar.getInstance();
        _manorRefresh.set(Calendar.HOUR_OF_DAY, MANOR_REFRESH);
        _manorRefresh.set(Calendar.MINUTE, MANOR_REFRESH_MIN);
        _manorRefresh.add(Calendar.HOUR_OF_DAY, 24);

        _log.info("Manor System: New Schedule for manor refresh @ " + _manorRefresh.getTime());

        return (_manorRefresh.getTimeInMillis() - Calendar.getInstance().getTimeInMillis());
    }

    public long getMillisToNextPeriodApprove() {
        if (_periodApprove.getTimeInMillis() > Calendar.getInstance().getTimeInMillis()) {
            return (_periodApprove.getTimeInMillis() - Calendar.getInstance().getTimeInMillis());
        }

        return setNewPeriodApprove();
    }

    public long setNewPeriodApprove() {
        _periodApprove = Calendar.getInstance();
        _periodApprove.set(Calendar.HOUR_OF_DAY, NEXT_PERIOD_APPROVE);
        _periodApprove.set(Calendar.MINUTE, NEXT_PERIOD_APPROVE_MIN);
        _periodApprove.add(Calendar.HOUR_OF_DAY, 24);

        _log.info("Manor System: New Schedule for period approve @ " + _periodApprove.getTime());

        return (_periodApprove.getTimeInMillis() - Calendar.getInstance().getTimeInMillis());
    }

    public void setNextPeriod() {
        for (Castle castle : CastleManager.getInstance().getCastles()) {
            if (castle.getOwnerId() <= 0) {
                continue;
            }
            L2Clan clan = ClanTable.getInstance().getClan(castle.getOwnerId());
            if (clan == null) {
                continue;
            }

            ItemContainer cwh = clan.getWarehouse();
            if (!(cwh instanceof ClanWarehouse)) {
                _log.info("Can't get clan warehouse for clan " + ClanTable.getInstance().getClan(castle.getOwnerId()));
                return;
            }

            for (CropProcure crop : castle.getCropProcure(PERIOD_CURRENT)) {
                if (crop.getStartAmount() == 0) {
                    continue;
                }
                // adding bought crops to clan warehouse
                if ((crop.getStartAmount() - crop.getAmount()) > 0) {
                    int count = crop.getStartAmount() - crop.getAmount();
                    count = (count * 90) / 100;
                    if (count < 1) {
                        if (Rnd.nextInt(99) < 90) {
                            count = 1;
                        }
                    }
                    if (count > 0) {
                        cwh.addItem("Manor", L2Manor.getInstance().getMatureCrop(crop.getCropId()), count, null, null);
                    }
                }
                // reserved and not used money giving back to treasury
                if (crop.getAmount() > 0) {
                    castle.addToTreasuryNoTax(crop.getAmount() * crop.getPrice());
                }
            }

            castle.setSeedProduction(castle.getSeedProduction(PERIOD_NEXT), PERIOD_CURRENT);
            castle.setCropProcure(castle.getCropProcure(PERIOD_NEXT), PERIOD_CURRENT);

            if (castle.getTreasury() < castle.getManorCost(PERIOD_CURRENT)) {
                castle.setSeedProduction(getNewSeedsList(castle.getCastleId()), PERIOD_NEXT);
                castle.setCropProcure(getNewCropsList(castle.getCastleId()), PERIOD_NEXT);
            } else {
                List<SeedProduction> production = new ArrayList<>();
                for (SeedProduction seedProduction : castle.getSeedProduction(PERIOD_CURRENT)) {
                    seedProduction.setAmount(seedProduction.getStartAmount());
                    production.add(seedProduction);
                }
                castle.setSeedProduction(production, PERIOD_NEXT);

                List<CropProcure> procure = new ArrayList<>();
                for (CropProcure cr : castle.getCropProcure(PERIOD_CURRENT)) {
                    cr.setAmount(cr.getStartAmount());
                    procure.add(cr);
                }
                castle.setCropProcure(procure, PERIOD_NEXT);
            }
            if (Config.ALT_MANOR_SAVE_ALL_ACTIONS) {
                castle.saveCropData();
                castle.saveSeedData();
            }

            // Sending notification to a clan leader
            L2PcInstance clanLeader = L2World.getInstance().getPlayer(clan.getLeader().getName());
            if (clanLeader != null) {
                clanLeader.sendPacket(new SystemMessage(SystemMessageId.THE_MANOR_INFORMATION_HAS_BEEN_UPDATED));
            }

            castle.setNextPeriodApproved(false);
        }
    }

    public void approveNextPeriod() {
        for (Castle c : CastleManager.getInstance().getCastles()) {
            boolean notFunc = false;

            if (c.getOwnerId() <= 0) { // Castle has no owner
                c.setCropProcure(new LinkedList<>(), PERIOD_NEXT);
                c.setSeedProduction(new LinkedList<>(), PERIOD_NEXT);
            } else if (c.getTreasury() < c.getManorCost(PERIOD_NEXT)) {
                notFunc = true;
                c.setSeedProduction(getNewSeedsList(c.getCastleId()), PERIOD_NEXT);
                c.setCropProcure(getNewCropsList(c.getCastleId()), PERIOD_NEXT);
            } else {
                ItemContainer cwh = ClanTable.getInstance().getClan(c.getOwnerId()).getWarehouse();
                if (!(cwh instanceof ClanWarehouse)) {
                    _log.info("Can't get clan warehouse for clan " + ClanTable.getInstance().getClan(c.getOwnerId()));
                    return;
                }
                int slots = 0;
                for (CropProcure crop : c.getCropProcure(PERIOD_NEXT)) {
                    if (crop.getStartAmount() > 0) {
                        slots++;
                    }
                }
                if (!cwh.validateCapacity(slots)) {
                    notFunc = true;
                    c.setSeedProduction(getNewSeedsList(c.getCastleId()), PERIOD_NEXT);
                    c.setCropProcure(getNewCropsList(c.getCastleId()), PERIOD_NEXT);
                }
            }
            c.setNextPeriodApproved(true);
            c.addToTreasuryNoTax((-1) * c.getManorCost(PERIOD_NEXT));

            if (notFunc) {
                L2Clan clan = ClanTable.getInstance().getClan(c.getOwnerId());
                L2PcInstance clanLeader = null;
                if (clan != null) {
                    clanLeader = L2World.getInstance().getPlayer(clan.getLeader().getName());
                }
                if (clanLeader != null) {
                    clanLeader.sendPacket(new SystemMessage(SystemMessageId.THE_AMOUNT_IS_NOT_SUFFICIENT_AND_SO_THE_MANOR_IS_NOT_IN_OPERATION));
                }
            }
        }

    }

    private List<SeedProduction> getNewSeedsList(int castleId) {
        return L2Manor.getInstance().getSeedsForCastle(castleId).stream().map(seedId -> new SeedProduction(seedId, castleId)).collect(Collectors.toList());
    }

    private List<CropProcure> getNewCropsList(int castleId) {
        return L2Manor.getInstance().getCropsForCastle(castleId).stream().map(cropId -> new CropProcure(cropId, castleId)).collect(Collectors.toList());
    }

    public boolean isUnderMaintenance() {
        return _underMaintenance;
    }

    public void setUnderMaintenance(boolean mode) {
        _underMaintenance = mode;
    }

    public boolean isDisabled() {
        return _disabled;
    }

    public void setDisabled(boolean mode) {
        _disabled = mode;
    }

    public SeedProduction getNewSeedProduction(int id, int amount, int price, int sales) {
        return new SeedProduction(id, amount, price, sales);
    }

    public CropProcure getNewCropProcure(int id, int amount, int type, int price, int buy) {
        return new CropProcure(id, amount, type, buy, price);
    }

    public void save() {
        for (Castle c : CastleManager.getInstance().getCastles()) {
            c.saveSeedData();
            c.saveCropData();
        }
    }
}