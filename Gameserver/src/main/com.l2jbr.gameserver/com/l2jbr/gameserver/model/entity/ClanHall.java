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
import com.l2jbr.gameserver.ThreadPoolManager;
import com.l2jbr.gameserver.datatables.ClanTable;
import com.l2jbr.gameserver.datatables.DoorTable;
import com.l2jbr.gameserver.instancemanager.AuctionManager;
import com.l2jbr.gameserver.instancemanager.ClanHallManager;
import com.l2jbr.gameserver.model.L2Clan;
import com.l2jbr.gameserver.model.actor.instance.L2DoorInstance;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.model.entity.database.ClanHallEntity;
import com.l2jbr.gameserver.model.entity.database.ClanHallFunctions;
import com.l2jbr.gameserver.model.entity.database.repository.ClanHallFunctionRepository;
import com.l2jbr.gameserver.model.entity.database.repository.ClanHallRepository;
import com.l2jbr.gameserver.model.zone.type.L2ClanHallZone;
import com.l2jbr.gameserver.network.SystemMessageId;
import com.l2jbr.gameserver.serverpackets.PledgeShowInfoUpdate;
import com.l2jbr.gameserver.serverpackets.SystemMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static java.util.Objects.isNull;


public class ClanHall {
    protected static final Logger _log = LoggerFactory.getLogger(ClanHall.class.getName());

    private List<L2DoorInstance> _doors;
    private final List<String> _doorDefault;
    private L2ClanHallZone _zone;
    protected final int _chRate = 604800000;
    protected boolean isFree = true;
    private final Map<Integer, ClanHallFunction> _functions;
    private ClanHallEntity entity;

    public static final int FUNC_TELEPORT = 1;
    public static final int FUNC_ITEM_CREATE = 2;
    public static final int FUNC_RESTORE_HP = 3;
    public static final int FUNC_RESTORE_MP = 4;
    public static final int FUNC_RESTORE_EXP = 5;
    public static final int FUNC_SUPPORT = 6;
    public static final int FUNC_DECO_FRONTPLATEFORM = 7;
    public static final int FUNC_DECO_CURTAINS = 8;

    public class ClanHallFunction {
        private final int _type;
        private int _lvl;
        protected int _fee;
        protected int _tempFee;
        private final long _rate;
        private long _endDate;
        protected boolean _inDebt;

        public ClanHallFunction(int type, int lvl, int lease, int tempLease, long rate, long time) {
            _type = type;
            _lvl = lvl;
            _fee = lease;
            _tempFee = tempLease;
            _rate = rate;
            _endDate = time;
            initializeTask();
        }

        public int getType() {
            return _type;
        }

        public int getLvl() {
            return _lvl;
        }

        public int getLease() {
            return _fee;
        }

        public long getRate() {
            return _rate;
        }

        public long getEndTime() {
            return _endDate;
        }

        public void setLvl(int lvl) {
            _lvl = lvl;
        }

        public void setLease(int lease) {
            _fee = lease;
        }

        public void setEndTime(long time) {
            _endDate = time;
        }

        private void initializeTask() {
            if (isFree) {
                return;
            }
            long currentTime = System.currentTimeMillis();
            if (_endDate > currentTime) {
                ThreadPoolManager.getInstance().scheduleGeneral(new FunctionTask(), _endDate - currentTime);
            } else {
                ThreadPoolManager.getInstance().scheduleGeneral(new FunctionTask(), 0);
            }
        }

        private class FunctionTask implements Runnable {
            public FunctionTask() {
            }

            @Override
            public void run() {
                try {
                    if (isFree) {
                        return;
                    }
                    if (ClanTable.getInstance().getClan(getOwnerId()).getWarehouse().getAdena() >= _fee) {
                        int fee = _fee;
                        boolean newfc = true;
                        if ((getEndTime() == 0) || (getEndTime() == -1)) {
                            if (getEndTime() == -1) {
                                newfc = false;
                                fee = _tempFee;
                            }
                        } else {
                            newfc = false;
                        }
                        setEndTime(System.currentTimeMillis() + getRate());
                        dbSave(newfc);
                        ClanTable.getInstance().getClan(getOwnerId()).getWarehouse().destroyItemByItemId("CH_function_fee", 57, fee, null, null);
                        if (Config.DEBUG) {
                            _log.warn("deducted " + fee + " adena from " + getName() + " owner's cwh for function id : " + getType());
                        }
                        ThreadPoolManager.getInstance().scheduleGeneral(new FunctionTask(), getRate());
                    } else {
                        removeFunction(getType());
                    }
                } catch (Throwable t) {
                }
            }
        }

        public void dbSave(boolean newFunction) {
            ClanHallFunctionRepository repository = DatabaseAccess.getRepository(ClanHallFunctionRepository.class);
            if (newFunction) {
                ClanHallFunctions function = new ClanHallFunctions(getId(), getType(), getLvl(), getLease(), getRate(), getEndTime());
                repository.save(function);
            } else {
                repository.updateByType(getId(), getType(), getLvl(), getLease(), getEndTime());
            }
        }
    }

    public ClanHall(ClanHallEntity clanHall) {
        entity = clanHall;
        _doorDefault = new ArrayList<>();
        _functions = new HashMap<>();

        if (entity.getOwnerId() != 0) {
            isFree = false;
            initialyzeTask(false);
            loadFunctions();
        }
    }

    public final boolean isPaid() {
        return entity.isPaid();
    }

    public final int  getId() {
        return entity.getId();
    }

    public final String getName() {
        return entity.getName();
    }

    public final int getOwnerId() {
        return entity.getOwnerId();
    }

    public final String getDesc() {
        return entity.getDescription();
    }

    public final int getLease() {
        return entity.getLease();
    }

    public final String getLocation() {
        return entity.getLocation();
    }

    public final long getPaidUntil() {
        return entity.getPaidUntil();
    }

    public final int getGrade() {
        return entity.getGrade();
    }

    /**
     * Return all DoorInstance
     *
     * @return
     */
    public final List<L2DoorInstance> getDoors() {
        if (_doors == null) {
            _doors = new LinkedList<>();
        }
        return _doors;
    }

    /**
     * Return Door
     *
     * @param doorId
     * @return
     */
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

    /**
     * Return function with id
     *
     * @param type
     * @return
     */
    public ClanHallFunction getFunction(int type) {
        if (_functions.get(type) != null) {
            return _functions.get(type);
        }
        return null;
    }

    /**
     * Sets this clan halls zone
     *
     * @param zone
     */
    public void setZone(L2ClanHallZone zone) {
        _zone = zone;
    }

    /**
     * Returns the zone of this clan hall
     *
     * @return
     */
    public L2ClanHallZone getZone() {
        return _zone;
    }



    public void free() {
        entity.setOwnerId(0);
        isFree = true;
        for (Map.Entry<Integer, ClanHallFunction> fc : _functions.entrySet()) {
            removeFunction(fc.getKey());
        }
        _functions.clear();
        entity.setPaidUntil(0);
        entity.setPaid(false);
        updateDb();
    }

    /**
     * Set owner if clan hall is free
     *
     * @param clan
     */
    public void setOwner(L2Clan clan) {
        // Verify that this ClanHall is Free and Clan isn't null
        if ((entity.getOwnerId() > 0) || (isNull(clan))) {
            return;
        }
        entity.setOwnerId(clan.getClanId());
        isFree = false;
        entity.setPaidUntil(System.currentTimeMillis());
        initialyzeTask(true);
        // Announce to Online member new ClanHall
        clan.broadcastToOnlineMembers(new PledgeShowInfoUpdate(clan));
        updateDb();
    }

    /**
     * Respawn all doors
     */
    public void spawnDoor() {
        spawnDoor(false);
    }

    /**
     * Respawn all doors
     *
     * @param isDoorWeak
     */
    public void spawnDoor(boolean isDoorWeak) {
        for (int i = 0; i < getDoors().size(); i++) {
            L2DoorInstance door = getDoors().get(i);
            if (door.getCurrentHp() <= 0) {
                door.decayMe(); // Kill current if not killed already
                door = DoorTable.parseList(_doorDefault.get(i));
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

    /**
     * Open or Close Door
     *
     * @param activeChar
     * @param doorId
     * @param open
     */
    public void openCloseDoor(L2PcInstance activeChar, int doorId, boolean open) {
        if ((activeChar != null) && (activeChar.getClanId() == getOwnerId())) {
            openCloseDoor(doorId, open);
        }
    }

    public void openCloseDoor(int doorId, boolean open) {
        openCloseDoor(getDoor(doorId), open);
    }

    public void openCloseDoor(L2DoorInstance door, boolean open) {
        if (door != null) {
            if (open) {
                door.openMe();
            } else {
                door.closeMe();
            }
        }
    }

    public void openCloseDoors(L2PcInstance activeChar, boolean open) {
        if ((activeChar != null) && (activeChar.getClanId() == getOwnerId())) {
            openCloseDoors(open);
        }
    }

    public void openCloseDoors(boolean open) {
        for (L2DoorInstance door : getDoors()) {
            if (door != null) {
                if (open) {
                    door.openMe();
                } else {
                    door.closeMe();
                }
            }
        }
    }

    /**
     * Banish Foreigner
     */
    public void banishForeigners() {
        _zone.banishForeigners(getOwnerId());
    }

    private void loadFunctions() {
        ClanHallFunctionRepository repository = DatabaseAccess.getRepository(ClanHallFunctionRepository.class);
        repository.findAllByHall(getId()).forEach( function -> {
            ClanHallFunction clanHallFunction = new ClanHallFunction(function.getType(), function.getLvl(), function.getLease(),
                0, function.getRate(), function.getEndTime());
            _functions.put(function.getType(), clanHallFunction);
        });
    }


    public void removeFunction(int functionType) {
        _functions.remove(functionType);
        ClanHallFunctionRepository repository = DatabaseAccess.getRepository(ClanHallFunctionRepository.class);
        repository.deleteByType(getId(), functionType);
    }

    /**
     * Update Function
     *
     * @param type
     * @param lvl
     * @param lease
     * @param rate
     * @param addNew
     * @return
     */
    public boolean updateFunctions(int type, int lvl, int lease, long rate, boolean addNew) {
        if (Config.DEBUG) {
            _log.warn("Called ClanHall.updateFunctions(int type, int lvl, int lease, long rate, boolean addNew) Owner : " + getOwnerId());
        }
        if (addNew) {
            if (ClanTable.getInstance().getClan(getOwnerId()).getWarehouse().getAdena() < lease) {
                return false;
            }
            _functions.put(type, new ClanHallFunction(type, lvl, lease, 0, rate, 0));
        } else {
            if ((lvl == 0) && (lease == 0)) {
                removeFunction(type);
            } else {
                int diffLease = lease - _functions.get(type).getLease();
                if (Config.DEBUG) {
                    _log.warn("Called ClanHall.updateFunctions diffLease : " + diffLease);
                }
                if (diffLease > 0) {
                    if (ClanTable.getInstance().getClan(entity.getOwnerId()).getWarehouse().getAdena() < diffLease) {
                        return false;
                    }
                    _functions.remove(type);
                    _functions.put(type, new ClanHallFunction(type, lvl, lease, diffLease, rate, -1));
                } else {
                    _functions.get(type).setLease(lease);
                    _functions.get(type).setLvl(lvl);
                    _functions.get(type).dbSave(false);
                }
            }
        }
        return true;
    }


    public void updateDb() {
        ClanHallRepository repository = DatabaseAccess.getRepository(ClanHallRepository.class);
        repository.save(entity);
    }


    private void initialyzeTask(boolean forced) {
        long currentTime = System.currentTimeMillis();
        if (getPaidUntil() > currentTime) {
            ThreadPoolManager.getInstance().scheduleGeneral(new FeeTask(), getPaidUntil() - currentTime);
        } else if (!isPaid() && !forced) {
            if ((System.currentTimeMillis() + (1000 * 60 * 60 * 24)) <= (getPaidUntil() + _chRate)) {
                ThreadPoolManager.getInstance().scheduleGeneral(new FeeTask(), System.currentTimeMillis() + (1000 * 60 * 60 * 24));
            } else {
                ThreadPoolManager.getInstance().scheduleGeneral(new FeeTask(), (getPaidUntil() + _chRate) - System.currentTimeMillis());
            }
        } else {
            ThreadPoolManager.getInstance().scheduleGeneral(new FeeTask(), 0);
        }
    }

    /**
     * Fee Task
     */
    private class FeeTask implements Runnable {
        public FeeTask() {
        }

        @Override
        public void run() {
            try {
                if (isFree) {
                    return;
                }
                L2Clan Clan = ClanTable.getInstance().getClan(getOwnerId());
                if (ClanTable.getInstance().getClan(getOwnerId()).getWarehouse().getAdena() >= getLease()) {
                    if (getPaidUntil() != 0) {
                        while (getPaidUntil() < System.currentTimeMillis()) {
                            entity.setPaidUntil(getPaidUntil() + _chRate);
                        }
                    } else {
                        entity.setPaidUntil(System.currentTimeMillis() + _chRate);
                    }
                    ClanTable.getInstance().getClan(getOwnerId()).getWarehouse().destroyItemByItemId("CH_rental_fee", 57, getLease(), null, null);
                    if (Config.DEBUG) {
                        _log.warn("deducted {} adena from {} owner's cwh for ClanHall _paidUntil", + getLease(), getName(), getPaidUntil());
                    }
                    ThreadPoolManager.getInstance().scheduleGeneral(new FeeTask(), getPaidUntil() - System.currentTimeMillis());
                    entity.setPaid(true);
                    updateDb();
                } else {
                    entity.setPaid(false);
                    if (System.currentTimeMillis() > (getPaidUntil() + _chRate)) {
                        if (ClanHallManager.getInstance().loaded()) {
                            AuctionManager.getInstance().initNPC(getId());
                            ClanHallManager.getInstance().setFree(getId());
                            Clan.broadcastToOnlineMembers(new SystemMessage(SystemMessageId.THE_CLAN_HALL_FEE_IS_ONE_WEEK_OVERDUE_THEREFORE_THE_CLAN_HALL_OWNERSHIP_HAS_BEEN_REVOKED));
                        } else {
                            ThreadPoolManager.getInstance().scheduleGeneral(new FeeTask(), 3000);
                        }
                    } else {
                        updateDb();
                        SystemMessage sm = new SystemMessage(SystemMessageId.PAYMENT_FOR_YOUR_CLAN_HALL_HAS_NOT_BEEN_MADE_PLEASE_MAKE_PAYMENT_TO_YOUR_CLAN_WAREHOUSE_BY_S1_TOMORROW);
                        sm.addNumber(getLease());
                        Clan.broadcastToOnlineMembers(sm);
                        if ((System.currentTimeMillis() + (1000 * 60 * 60 * 24)) <= (getPaidUntil() + _chRate)) {
                            ThreadPoolManager.getInstance().scheduleGeneral(new FeeTask(), System.currentTimeMillis() + (1000 * 60 * 60 * 24));
                        } else {
                            ThreadPoolManager.getInstance().scheduleGeneral(new FeeTask(), (getPaidUntil() + _chRate) - System.currentTimeMillis());
                        }

                    }
                }
            } catch (Throwable t) {
            }
        }
    }
}