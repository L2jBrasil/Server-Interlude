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
package com.l2jbr.gameserver.instancemanager;

import com.l2jbr.commons.Config;
import com.l2jbr.gameserver.idfactory.IdFactory;
import com.l2jbr.gameserver.model.actor.instance.L2BoatInstance;
import com.l2jbr.gameserver.model.entity.database.CharTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

import static java.util.Objects.isNull;

public class BoatManager {
    private static final Logger _log = LoggerFactory.getLogger(BoatManager.class.getName());

    private static BoatManager _instance;

    public static BoatManager getInstance() {
        if (isNull(_instance)) {
            _log.info("Initializing BoatManager");
            _instance = new BoatManager();
            _instance.load();
        }
        return _instance;
    }

    private Map<Integer, L2BoatInstance> _staticItems = new LinkedHashMap<>();

    private BoatManager() {
    }

    private void load() {
        if (!Config.ALLOW_BOAT) {
            return;
        }
        LineNumberReader lnr = null;
        try {
            File doorData = new File(Config.DATAPACK_ROOT, "data/boat.csv");
            lnr = new LineNumberReader(new BufferedReader(new FileReader(doorData)));

            String line = null;
            while ((line = lnr.readLine()) != null) {
                if ((line.trim().length() == 0) || line.startsWith("#")) {
                    continue;
                }
                L2BoatInstance boat = parseLine(line);
                boat.spawn();
                _staticItems.put(boat.getObjectId(), boat);
                _log.debug("Boat ID : {}", boat.getObjectId());
            }
        } catch (FileNotFoundException e) {
            _log.warn("boat.csv is missing in data folder");
        } catch (Exception e) {
            _log.warn("error while creating boat table " + e);
            e.printStackTrace();
        } finally {
            try {
                lnr.close();
            } catch (Exception e1) { /* ignore problems */
            }
        }
    }

    private L2BoatInstance parseLine(String line) {
        L2BoatInstance boat;
        StringTokenizer st = new StringTokenizer(line, ";");

        String name = st.nextToken();
        int xspawn = Integer.parseInt(st.nextToken());
        int yspawn = Integer.parseInt(st.nextToken());
        int zspawn = Integer.parseInt(st.nextToken());
        int heading = Integer.parseInt(st.nextToken());

        CharTemplate template = CharTemplate.objectTemplate(50000, (short)100, (short)100, 0f, 0f);
        boat = new L2BoatInstance(IdFactory.getInstance().getNextId(), template, name);
        boat.getPosition().setHeading(heading);
        boat.setPosition(xspawn, yspawn, zspawn);

        int IdWaypoint1 = Integer.parseInt(st.nextToken());
        int IdWTicket1 = Integer.parseInt(st.nextToken());
        int ntx1 = Integer.parseInt(st.nextToken());
        int nty1 = Integer.parseInt(st.nextToken());
        int ntz1 = Integer.parseInt(st.nextToken());
        String npc1 = st.nextToken();
        String mess10_1 = st.nextToken();
        String mess5_1 = st.nextToken();
        String mess1_1 = st.nextToken();
        String mess0_1 = st.nextToken();
        String messb_1 = st.nextToken();
        boat.setTrajet1(IdWaypoint1, IdWTicket1, ntx1, nty1, ntz1, npc1, mess10_1, mess5_1, mess1_1, mess0_1, messb_1);
        IdWaypoint1 = Integer.parseInt(st.nextToken());
        IdWTicket1 = Integer.parseInt(st.nextToken());
        ntx1 = Integer.parseInt(st.nextToken());
        nty1 = Integer.parseInt(st.nextToken());
        ntz1 = Integer.parseInt(st.nextToken());
        npc1 = st.nextToken();
        mess10_1 = st.nextToken();
        mess5_1 = st.nextToken();
        mess1_1 = st.nextToken();
        mess0_1 = st.nextToken();
        messb_1 = st.nextToken();
        boat.setTrajet2(IdWaypoint1, IdWTicket1, ntx1, nty1, ntz1, npc1, mess10_1, mess5_1, mess1_1, mess0_1, messb_1);
        return boat;
    }

    public L2BoatInstance GetBoat(int boatId) {
        if (_staticItems == null) {
            _staticItems = new LinkedHashMap<>();
        }
        return _staticItems.get(boatId);
    }
}