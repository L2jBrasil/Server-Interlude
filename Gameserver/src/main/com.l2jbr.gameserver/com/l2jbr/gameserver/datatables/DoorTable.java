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
package com.l2jbr.gameserver.datatables;

import com.l2jbr.commons.Config;
import com.l2jbr.gameserver.idfactory.IdFactory;
import com.l2jbr.gameserver.instancemanager.ClanHallManager;
import com.l2jbr.gameserver.model.actor.instance.L2DoorInstance;
import com.l2jbr.gameserver.model.entity.ClanHall;
import com.l2jbr.gameserver.model.entity.database.CastleDoor;
import com.l2jbr.gameserver.model.entity.database.CharTemplate;
import com.l2jbr.gameserver.pathfinding.AbstractNodeLoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import static com.l2jbr.gameserver.util.GameserverMessages.getMessage;

public class DoorTable {
    private static Logger _log = LoggerFactory.getLogger(DoorTable.class);

    private Map<Integer, L2DoorInstance> _staticItems;

    private static DoorTable _instance;

    public static DoorTable getInstance() {
        if (_instance == null) {
            _instance = new DoorTable();
            _instance.parseData();
        }
        return _instance;
    }

    private DoorTable() {
        _staticItems = new HashMap<>();
    }

    public void reloadAll() {
        respawn();
    }

    public void respawn() {
        // L2DoorInstance[] currentDoors = getDoors();
        _staticItems = null;
        _instance = null;
        _instance = new DoorTable();
    }

    public void parseData() {
        File doorData = new File(Config.DATAPACK_ROOT, "data/door.csv");
        try (LineNumberReader lnr = new LineNumberReader(new BufferedReader(new FileReader(doorData)))) {

            _log.debug("Searching clan halls doors:");
            String line;
            while ((line = lnr.readLine()) != null) {
                if ((line.trim().length() == 0) || line.startsWith("#")) {
                    continue;
                }

                L2DoorInstance door = parseList(line);
                _staticItems.put(door.getDoorId(), door);
                door.spawnMe(door.getX(), door.getY(), door.getZ());
                ClanHall clanhall = ClanHallManager.getInstance().getNearbyClanHall(door.getX(), door.getY(), 500);
                if (clanhall != null) {
                    clanhall.getDoors().add(door);
                    door.setClanHall(clanhall);
                    if (Config.DEBUG) {
                        _log.warn("door " + door.getDoorName() + " attached to ch " + clanhall.getName());
                    }
                }
            }

            _log.info("DoorTable: Loaded " + _staticItems.size() + " Door Templates.");
        } catch (FileNotFoundException e) {
            _initialized = false;
            _log.warn("door.csv is missing in data folder");
        } catch (IOException e) {
            _initialized = false;
            _log.warn("error while creating door table " + e);
        }
        try {
            getDoor(24190001).openMe();
            getDoor(24190002).openMe();
            getDoor(24190003).openMe();
            getDoor(24190004).openMe();
            getDoor(23180001).openMe();
            getDoor(23180002).openMe();
            getDoor(23180003).openMe();
            getDoor(23180004).openMe();
            getDoor(23180005).openMe();
            getDoor(23180006).openMe();

            checkAutoOpen();
        } catch (NullPointerException e) {
            _log.warn(getMessage("error.door.file"));
            if (Config.DEBUG) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    public static L2DoorInstance parseDoor(CastleDoor castleDoor) {
        int rangeXMin = castleDoor.getRangeXmin();
        int rangeXMax = castleDoor.getRangeXmax();
        int rangeYMin = castleDoor.getRangeYmin();
        int rangeYMax = castleDoor.getRangeYmax();
        int rangeZMin = castleDoor.getRangeZmin();
        int rangeZMax = castleDoor.getRangeZmax();
        int x = castleDoor.getX();
        int y = castleDoor.getY();
        int z = castleDoor.getZ();

        if (rangeXMin > rangeXMax) {
            _log.error("Error in door data, ID: {} rangeXMin greater than rangeXMax", castleDoor.getId() );
        }
        if (rangeYMin > rangeYMax) {
            _log.error("Error in door data, ID: {} rangeYMin greater than rangeYMax", castleDoor.getId());
        }
        if (rangeZMin > rangeZMax) {
            _log.error("Error in door data, ID: {} rangeZMin greater than rangeZMax", castleDoor.getId());
        }

        float collisionRadius; // (max) radius for movement checks

        if ((rangeXMax - rangeXMin) > (rangeYMax - rangeYMin)) {
            collisionRadius = rangeYMax - rangeYMin;
        } else {
            collisionRadius = rangeXMax - rangeXMin;
        }

        float collisionHeight = rangeZMax - rangeZMin;

        int id = castleDoor.getId();

        String name = castleDoor.getName();
        CharTemplate template =  CharTemplate.objectTemplate(castleDoor.getHp(), castleDoor.getpDef(), castleDoor.getmDef(), collisionRadius, collisionHeight);

        L2DoorInstance door = new L2DoorInstance(IdFactory.getInstance().getNextId(), template, id, name, false);
        initDoor(rangeXMin, rangeXMax, rangeYMin, rangeYMax, rangeZMin, rangeZMax, x, y, z, id, door);

        return door;
    }

    private static void initDoor(int rangeXMin, int rangeXMax, int rangeYMin, int rangeYMax, int rangeZMin, int rangeZMax, int x, int y, int z, int id, L2DoorInstance door) {
        door.setRange(rangeXMin, rangeYMin, rangeZMin, rangeXMax, rangeYMax, rangeZMax);
        try {
            door.setMapRegion(MapRegionTable.getInstance().getMapRegion(x, y));
        } catch (Exception e) {
            _log.error("Error in door data, ID: {}", id);
            _log.error(e.getLocalizedMessage(), e);
        }
        door.setCurrentHpMp(door.getMaxHp(), door.getMaxMp());
        door.setOpen(1);
        door.setPositionInvisible(x, y, z);
    }

    public static L2DoorInstance parseList(String line) {
        StringTokenizer st = new StringTokenizer(line, ";");

        String name = st.nextToken();
        int id = Integer.parseInt(st.nextToken());
        int x = Integer.parseInt(st.nextToken());
        int y = Integer.parseInt(st.nextToken());
        int z = Integer.parseInt(st.nextToken());
        int rangeXMin = Integer.parseInt(st.nextToken());
        int rangeYMin = Integer.parseInt(st.nextToken());
        int rangeZMin = Integer.parseInt(st.nextToken());
        int rangeXMax = Integer.parseInt(st.nextToken());
        int rangeYMax = Integer.parseInt(st.nextToken());
        int rangeZMax = Integer.parseInt(st.nextToken());
        int hp = Integer.parseInt(st.nextToken());
        int pdef = Integer.parseInt(st.nextToken());
        int mdef = Integer.parseInt(st.nextToken());
        boolean unlockable = false;

        if (st.hasMoreTokens()) {
            unlockable = Boolean.parseBoolean(st.nextToken());
        }

        if (rangeXMin > rangeXMax) {
            _log.error("Error in door data, ID:" + id);
        }
        if (rangeYMin > rangeYMax) {
            _log.error("Error in door data, ID:" + id);
        }
        if (rangeZMin > rangeZMax) {
            _log.error("Error in door data, ID:" + id);
        }
        float collisionRadius; // (max) radius for movement checks
        if ((rangeXMax - rangeXMin) > (rangeYMax - rangeYMin)) {
            collisionRadius = rangeYMax - rangeYMin;
        } else {
            collisionRadius = rangeXMax - rangeXMin;
        }
        float collisionHeight = rangeZMax - rangeZMin;


        CharTemplate template = CharTemplate.objectTemplate(hp, pdef, mdef, collisionRadius, collisionHeight);
        L2DoorInstance door = new L2DoorInstance(IdFactory.getInstance().getNextId(), template, id, name, unlockable);
        initDoor(rangeXMin, rangeXMax, rangeXMin, rangeYMax, rangeZMin, rangeZMax, x, y, z, id, door);
        return door;
    }

    public boolean isInitialized() {
        return _initialized;
    }

    private boolean _initialized = true;

    public L2DoorInstance getDoor(Integer id) {
        return _staticItems.get(id);
    }

    public void putDoor(L2DoorInstance door) {
        _staticItems.put(door.getDoorId(), door);
    }

    public L2DoorInstance[] getDoors() {
        return _staticItems.values().toArray(new L2DoorInstance[0]);
    }

    /**
     * Performs a check and sets up a scheduled task for those doors that require auto opening/closing.
     */
    public void checkAutoOpen() {
        for (L2DoorInstance doorInst : getDoors()) {
            // Garden of Eva (every 7 minutes)
            if (doorInst.getDoorName().startsWith("goe")) {
                doorInst.setAutoActionDelay(420000);
            } else if (doorInst.getDoorName().startsWith("aden_tower")) {
                doorInst.setAutoActionDelay(300000);
            }
        }
    }

    public boolean checkIfDoorsBetween(AbstractNodeLoc start, AbstractNodeLoc end) {
        return checkIfDoorsBetween(start.getX(), start.getY(), start.getZ(), end.getX(), end.getY(), end.getZ());
    }

    public boolean checkIfDoorsBetween(int x, int y, int z, int tx, int ty, int tz) {
        int region;
        try {
            region = MapRegionTable.getInstance().getMapRegion(x, y);
        } catch (Exception e) {
            return false;
        }

        for (L2DoorInstance doorInst : getDoors()) {
            if (doorInst.getMapRegion() != region) {
                continue;
            }
            if (doorInst.getXMax() == 0) {
                continue;
            }

            // line segment goes through box
            // heavy approximation disabling some shooting angles especially near 2-piece doors
            // but most calculations should stop short
            // phase 1, x
            if (((x <= doorInst.getXMax()) && (tx >= doorInst.getXMin())) || ((tx <= doorInst.getXMax()) && (x >= doorInst.getXMin()))) {
                // phase 2, y
                if (((y <= doorInst.getYMax()) && (ty >= doorInst.getYMin())) || ((ty <= doorInst.getYMax()) && (y >= doorInst.getYMin()))) {
                    // phase 3, z (there's a small problem when the other is above/under door level..)
                    if ((z >= doorInst.getZMin()) && (z <= doorInst.getZMax()) && (tz >= doorInst.getZMin()) && (tz <= doorInst.getZMax())) {
                        if (!((doorInst.getCurrentHp() <= 0) || (doorInst.getOpen() == 0))) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
