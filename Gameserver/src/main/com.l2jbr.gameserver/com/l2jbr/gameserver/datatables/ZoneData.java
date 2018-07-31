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
package com.l2jbr.gameserver.datatables;

import com.l2jbr.commons.Config;
import com.l2jbr.gameserver.instancemanager.ArenaManager;
import com.l2jbr.gameserver.instancemanager.FishingZoneManager;
import com.l2jbr.gameserver.instancemanager.OlympiadStadiaManager;
import com.l2jbr.gameserver.instancemanager.TownManager;
import com.l2jbr.gameserver.model.L2World;
import com.l2jbr.gameserver.model.L2WorldRegion;
import com.l2jbr.gameserver.model.entity.database.ZoneVertices;
import com.l2jbr.gameserver.model.entity.database.repository.ZoneVerticesRepository;
import com.l2jbr.gameserver.model.zone.L2ZoneType;
import com.l2jbr.gameserver.model.zone.form.ZoneCuboid;
import com.l2jbr.gameserver.model.zone.form.ZoneNPoly;
import com.l2jbr.gameserver.model.zone.type.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.List;

import static com.l2jbr.commons.database.DatabaseAccess.getRepository;
import static java.util.Objects.isNull;

/**
 *
 * @author durgus
 */
public class ZoneData {
    private static final Logger _log = LoggerFactory.getLogger(ZoneData.class.getName());

    private static ZoneData _instance;

    public static ZoneData getInstance() {
        if (isNull(_instance)) {
            _instance = new ZoneData();
        }
        return _instance;
    }

    private ZoneData() {
        _log.info("Loading zones...");
        load();
    }

    private void load() {
        int zoneCount = 0;

        L2WorldRegion[][] worldRegions = L2World.getInstance().getAllWorldRegions();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringComments(true);

            File file = new File(Config.DATAPACK_ROOT + "/data/zones/zone.xml");
            if (!file.exists()) {
                _log.warn("The {} file is missing.", file.getAbsolutePath());
                return;
            }

            Document doc = factory.newDocumentBuilder().parse(file);
            ZoneVerticesRepository repository = getRepository(ZoneVerticesRepository.class);

            for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling()) {
                if ("list".equalsIgnoreCase(n.getNodeName())) {
                    for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling()) {
                        if ("zone".equalsIgnoreCase(d.getNodeName())) {
                            NamedNodeMap attrs = d.getAttributes();
                            int zoneId = Integer.parseInt(attrs.getNamedItem("id").getNodeValue());
                            int minZ = Integer.parseInt(attrs.getNamedItem("minZ").getNodeValue());
                            int maxZ = Integer.parseInt(attrs.getNamedItem("maxZ").getNodeValue());
                            String zoneType = attrs.getNamedItem("type").getNodeValue();
                            String zoneShape = attrs.getNamedItem("shape").getNodeValue();

                            L2ZoneType temp = null;

                            if (zoneType.equals("FishingZone")) {
                                temp = new L2FishingZone();
                            } else if (zoneType.equals("ClanHallZone")) {
                                temp = new L2ClanHallZone();
                            } else if (zoneType.equals("PeaceZone")) {
                                temp = new L2PeaceZone();
                            } else if (zoneType.equals("Town")) {
                                temp = new L2TownZone();
                            } else if (zoneType.equals("OlympiadStadium")) {
                                temp = new L2OlympiadStadiumZone();
                            } else if (zoneType.equals("CastleZone")) {
                                temp = new L2CastleZone();
                            } else if (zoneType.equals("DamageZone")) {
                                temp = new L2DamageZone();
                            } else if (zoneType.equals("Arena")) {
                                temp = new L2ArenaZone();
                            } else if (zoneType.equals("MotherTree")) {
                                temp = new L2MotherTreeZone();
                            } else if (zoneType.equals("BigheadZone")) {
                                temp = new L2BigheadZone();
                            } else if (zoneType.equals("NoLandingZone")) {
                                temp = new L2NoLandingZone();
                            } else if (zoneType.equals("JailZone")) {
                                temp = new L2JailZone();
                            } else if (zoneType.equals("DerbyTrackZone")) {
                                temp = new L2DerbyTrackZone();
                            }

                            if (temp == null) {
                                _log.warn("ZoneData: No such zone type: {}", zoneType);
                                continue;
                            }

                            List<ZoneVertices> vertices = repository.findAllOrderedById(zoneId);

                            // Create this zone. Parsing for cuboids is a bit different than for other polygons
                            // cuboids need exactly 2 points to be defined. Other polygons need at least 3 (one per vertex)
                            if (zoneShape.equals("Cuboid")) {

                                if(vertices.size() < 2) {
                                    _log.warn("ZoneData: Missing cuboid vertex in sql data for zone: {}", zoneId);
                                    continue;
                                }

                                int[] x = vertices.stream().mapToInt(ZoneVertices::getX).toArray();
                                int[] y = vertices.stream().mapToInt(ZoneVertices::getY).toArray();

                                temp.setZone(new ZoneCuboid(x[0], x[1], y[0], y[1], minZ, maxZ));
                            } else if (zoneShape.equals("NPoly")) {
                                // An nPoly needs to have at least 3 vertices
                                if (vertices.size() > 2) {
                                    int[] aX = vertices.stream().mapToInt(ZoneVertices::getX).toArray();
                                    int[] aY = vertices.stream().mapToInt(ZoneVertices::getY).toArray();

                                    temp.setZone(new ZoneNPoly(aX, aY, minZ, maxZ));
                                } else {
                                    _log.warn("ZoneData: Bad sql data for zone: {}", zoneId);
                                    continue;
                                }
                            } else {
                                _log.warn("ZoneData: Unknown shape: {}", zoneShape);
                                continue;
                            }

                            // Check for aditional parameters
                            for (Node cd = d.getFirstChild(); cd != null; cd = cd.getNextSibling()) {
                                if ("stat".equalsIgnoreCase(cd.getNodeName())) {
                                    attrs = cd.getAttributes();
                                    String name = attrs.getNamedItem("name").getNodeValue();
                                    String val = attrs.getNamedItem("val").getNodeValue();

                                    temp.setParameter(name, val);
                                }
                            }

                            // Skip checks for fishing zones & add to fishing zone manager
                            if (temp instanceof L2FishingZone) {
                                FishingZoneManager.getInstance().addFishingZone((L2FishingZone) temp);
                                continue;
                            }

                            // Register the zone into any world region it intersects with...
                            // currently 11136 test for each zone :>
                            int ax, ay, bx, by;
                            for (int x = 0; x < worldRegions.length; x++) {
                                for (int y = 0; y < worldRegions[x].length; y++) {
                                    ax = (x - L2World.OFFSET_X) << L2World.SHIFT_BY;
                                    bx = ((x + 1) - L2World.OFFSET_X) << L2World.SHIFT_BY;
                                    ay = (y - L2World.OFFSET_Y) << L2World.SHIFT_BY;
                                    by = ((y + 1) - L2World.OFFSET_Y) << L2World.SHIFT_BY;

                                    if (temp.getZone().intersectsRectangle(ax, bx, ay, by)) {
                                        worldRegions[x][y].addZone(temp);
                                        _log.debug("Zone ( {} ) added to: {}, {}.", zoneId, x, y);
                                    }
                                }
                            }

                            if (temp instanceof L2ArenaZone) {
                                ArenaManager.getInstance().addArena((L2ArenaZone) temp);
                            } else if (temp instanceof L2TownZone) {
                                TownManager.getInstance().addTown((L2TownZone) temp);
                            } else if (temp instanceof L2OlympiadStadiumZone) {
                                OlympiadStadiaManager.getInstance().addStadium((L2OlympiadStadiumZone) temp);
                            }

                            zoneCount++;
                        }
                    }
                }
            }
        } catch (Exception e) {
            _log.error( "Error while loading zones.", e);
            return;
        }

        _log.info("Done: loaded {} zones.", zoneCount);
    }
}
