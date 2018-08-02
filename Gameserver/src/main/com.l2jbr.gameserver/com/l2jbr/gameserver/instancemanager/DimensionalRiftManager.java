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
import com.l2jbr.commons.database.DatabaseAccess;
import com.l2jbr.commons.util.Rnd;
import com.l2jbr.gameserver.datatables.NpcTable;
import com.l2jbr.gameserver.datatables.SpawnTable;
import com.l2jbr.gameserver.model.L2ItemInstance;
import com.l2jbr.gameserver.model.L2Spawn;
import com.l2jbr.gameserver.model.actor.instance.L2NpcInstance;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.model.entity.DimensionalRift;
import com.l2jbr.gameserver.model.entity.database.DimensionalRiftRoom;
import com.l2jbr.gameserver.model.entity.database.NpcTemplate;
import com.l2jbr.gameserver.model.entity.database.repository.DimensionalRiftRepository;
import com.l2jbr.gameserver.serverpackets.NpcHtmlMessage;
import com.l2jbr.gameserver.templates.ISpawn;
import com.l2jbr.gameserver.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.isNull;

/**
 * Thanks to L2Fortress and balancer.ru - kombat
 */
public class DimensionalRiftManager {

    private static Logger _log = LoggerFactory.getLogger(DimensionalRiftManager.class);
    private static DimensionalRiftManager _instance;
    private final HashMap<Byte, Map<Byte, DimensionalRiftRoom>> _rooms = new HashMap<>();
    private static final short DIMENSIONAL_FRAGMENT_ITEM_ID = 7079;

    public static DimensionalRiftManager getInstance() {
        if (isNull(_instance)) {
            _instance = new DimensionalRiftManager();
        }
        return _instance;
    }

    private DimensionalRiftManager() {
        loadRooms();
        loadSpawns();
    }

    public DimensionalRiftRoom getRoom(byte type, byte room) {
        return _rooms.get(type) == null ? null : _rooms.get(type).get(room);
    }

    private void loadRooms() {
        DimensionalRiftRepository repository = DatabaseAccess.getRepository(DimensionalRiftRepository.class);
        repository.findAll().forEach(dimensionalRiftRoom -> {
            // 0 waiting room, 1 recruit, 2 soldier, 3 officer, 4 captain , 5 commander, 6 hero
            var type = dimensionalRiftRoom.getType();
            var room_id = dimensionalRiftRoom.getRoomId();

            if (!_rooms.containsKey(type)) {
                _rooms.put(type, new HashMap<>());
            }
            _rooms.get(type).put(room_id, dimensionalRiftRoom);

        });

        var typeSize = _rooms.keySet().size();
        var roomSize = 0;

        for (Byte b : _rooms.keySet()) {
            roomSize += _rooms.get(b).keySet().size();
        }

        _log.info("DimensionalRiftManager: Loaded  {} room types with {} rooms.", typeSize, roomSize);
    }

    private void loadSpawns() {
        int countGood = 0, countBad = 0;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringComments(true);

            File file = new File(Config.DATAPACK_ROOT + "/data/dimensionalRift.xml");
            if (!file.exists()) {
                throw new IOException();
            }

            Document doc = factory.newDocumentBuilder().parse(file);
            NamedNodeMap attrs;
            byte type, roomId;
            int mobId, x, y, z, delay, count;
            L2Spawn spawnDat;
            NpcTemplate template;

            for (Node rift = doc.getFirstChild(); rift != null; rift = rift.getNextSibling()) {
                if ("rift".equalsIgnoreCase(rift.getNodeName())) {
                    for (Node area = rift.getFirstChild(); area != null; area = area.getNextSibling()) {
                        if ("area".equalsIgnoreCase(area.getNodeName())) {
                            attrs = area.getAttributes();
                            type = Byte.parseByte(attrs.getNamedItem("type").getNodeValue());

                            for (Node room = area.getFirstChild(); room != null; room = room.getNextSibling()) {
                                if ("room".equalsIgnoreCase(room.getNodeName())) {
                                    attrs = room.getAttributes();
                                    roomId = Byte.parseByte(attrs.getNamedItem("id").getNodeValue());

                                    for (Node spawn = room.getFirstChild(); spawn != null; spawn = spawn.getNextSibling()) {
                                        if ("spawn".equalsIgnoreCase(spawn.getNodeName())) {
                                            attrs = spawn.getAttributes();
                                            mobId = Integer.parseInt(attrs.getNamedItem("mobId").getNodeValue());
                                            delay = Integer.parseInt(attrs.getNamedItem("delay").getNodeValue());
                                            count = Integer.parseInt(attrs.getNamedItem("count").getNodeValue());

                                            template = NpcTable.getInstance().getTemplate(mobId);
                                            if (template == null) {
                                                _log.warn("Template {} not found!", mobId);
                                            }
                                            if (!_rooms.containsKey(type)) {
                                                _log.warn("Type {} not found!", type);
                                            } else if (!_rooms.get(type).containsKey(roomId)) {
                                                _log.warn("Room {} in Type {} not found!", roomId, type);
                                            }

                                            for (int i = 0; i < count; i++) {
                                                DimensionalRiftRoom riftRoom = _rooms.get(type).get(roomId);
                                                x = riftRoom.getRandomX();
                                                y = riftRoom.getRandomY();
                                                z = riftRoom.getTeleportZ();

                                                if ((Objects.nonNull(template)) && _rooms.containsKey(type) && _rooms.get(type).containsKey(roomId)) {
                                                    spawnDat = new L2Spawn(new DimensionalSpawn(template, x, y, z, delay));
                                                    SpawnTable.getInstance().addNewSpawn(spawnDat, false);
                                                    _rooms.get(type).get(roomId).getSpawns().add(spawnDat);
                                                    countGood++;
                                                } else {
                                                    countBad++;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            _log.warn("Error on loading dimensional rift spawns", e);
        }
        _log.info("DimensionalRiftManager: Loaded {} dimensional rift spawns {} errors.", countGood , countBad);
    }

    public void reload() {
        for (Byte b : _rooms.keySet()) {
            for (byte i : _rooms.get(b).keySet()) {
                _rooms.get(b).get(i).getSpawns().clear();
            }
            _rooms.get(b).clear();
        }
        _rooms.clear();
        loadRooms();
        loadSpawns();
    }

    public boolean checkIfInRiftZone(int x, int y, int z, boolean ignorePeaceZone) {
        if (ignorePeaceZone) {
            return _rooms.get((byte) 0).get((byte) 1).checkIfInZone(x, y, z);
        }
        return _rooms.get((byte) 0).get((byte) 1).checkIfInZone(x, y, z) && !_rooms.get((byte) 0).get((byte) 0).checkIfInZone(x, y, z);
    }

    public boolean checkIfInPeaceZone(int x, int y, int z) {
        return _rooms.get((byte) 0).get((byte) 0).checkIfInZone(x, y, z);
    }

    public void teleportToWaitingRoom(L2PcInstance player) {
        var room = getRoom((byte) 0, (byte) 0);
        player.teleToLocation(room.getTeleportX(), room.getTeleportY(), room.getTeleportZ());
    }

    public void start(L2PcInstance player, byte type, L2NpcInstance npc) {
        boolean canPass = true;
        if (!player.isInParty()) {
            showHtmlFile(player, "data/html/seven_signs/rift/NoParty.htm", npc);
            return;
        }

        if (player.getParty().getPartyLeaderOID() != player.getObjectId()) {
            showHtmlFile(player, "data/html/seven_signs/rift/NotPartyLeader.htm", npc);
            return;
        }

        if (player.getParty().isInDimensionalRift()) {
            handleCheat(player, npc);
            return;
        }

        if (player.getParty().getMemberCount() < Config.RIFT_MIN_PARTY_SIZE) {
            NpcHtmlMessage html = new NpcHtmlMessage(npc.getObjectId());
            html.setFile("data/html/seven_signs/rift/SmallParty.htm");
            html.replace("%npc_name%", npc.getName());
            html.replace("%count%", String.valueOf(Config.RIFT_MIN_PARTY_SIZE));
            player.sendPacket(html);
            return;
        }

        for (L2PcInstance p : player.getParty().getPartyMembers()) {
            if (!checkIfInPeaceZone(p.getX(), p.getY(), p.getZ())) {
                canPass = false;
            }
        }

        if (!canPass) {
            showHtmlFile(player, "data/html/seven_signs/rift/NotInWaitingRoom.htm", npc);
            return;
        }

        L2ItemInstance i;
        for (L2PcInstance p : player.getParty().getPartyMembers()) {
            i = p.getInventory().getItemByItemId(DIMENSIONAL_FRAGMENT_ITEM_ID);

            if (i == null) {
                canPass = false;
                break;
            }

            if (i.getCount() > 0) {
                if (i.getCount() < getNeededItems(type)) {
                    canPass = false;
                }
            }
        }

        if (!canPass) {
            NpcHtmlMessage html = new NpcHtmlMessage(npc.getObjectId());
            html.setFile("data/html/seven_signs/rift/NoFragments.htm");
            html.replace("%npc_name%", npc.getName());
            html.replace("%count%", String.valueOf(getNeededItems(type)));
            player.sendPacket(html);
            return;
        }

        for (L2PcInstance p : player.getParty().getPartyMembers()) {
            i = p.getInventory().getItemByItemId(DIMENSIONAL_FRAGMENT_ITEM_ID);
            p.destroyItem("RiftEntrance", i.getObjectId(), getNeededItems(type), null, false);
        }

        new DimensionalRift(player.getParty(), type, (byte) Rnd.get(1, 9));
    }

    public void killRift(DimensionalRift d) {
        if (d.getTeleportTimerTask() != null) {
            d.getTeleportTimerTask().cancel();
        }
        d.setTeleportTimerTask(null);

        if (d.getTeleportTimer() != null) {
            d.getTeleportTimer().cancel();
        }
        d.setTeleportTimer(null);

        if (d.getSpawnTimerTask() != null) {
            d.getSpawnTimerTask().cancel();
        }
        d.setSpawnTimerTask(null);

        if (d.getSpawnTimer() != null) {
            d.getSpawnTimer().cancel();
        }
        d.setSpawnTimer(null);
    }

    class DimensionalSpawn implements ISpawn {

        private final NpcTemplate template;
        private int x;
        private int y;
        private int z;
        private int respawnDelay;
        private int amount;
        private int heading;
        private int id;

        DimensionalSpawn(NpcTemplate template, int x, int y, int z, int delay) {
            this.template = template;
            this.x = x;
            this.y = y;
            this.z = z;
            this.respawnDelay = delay;
            amount = 1;
            id = 0;
        }

        @Override
        public Integer getId() {
            return id;
        }

        @Override
        public void setId(int id) {
            this.id = id;
        }

        @Override
        public int getX() {
            return x;
        }

        @Override
        public void setX(int locx) {
            x = locx;
        }

        @Override
        public int getY() {
            return y;
        }

        @Override
        public void setY(int locy) {
            y = locy;
        }

        @Override
        public int getZ() {
            return z;
        }

        @Override
        public void setZ(int locz) {
            z = locz;
        }

        @Override
        public int getCount() {
            return amount;
        }

        @Override
        public void setCount(int amount) {
            this.amount = amount;
        }

        @Override
        public int getRespawnDelay() {
            return respawnDelay;
        }

        @Override
        public void setRespawnDelay(int delay) {
            this.respawnDelay = delay;
        }

        @Override
        public int getHeading() {
            return heading;
        }

        @Override
        public void setHeading(int heading) {
            this.heading = heading;
        }

        @Override
        public NpcTemplate getNpcTemplate() {
            return template;
        }
    }

    private int getNeededItems(byte type) {
        switch (type) {
            case 1:
                return Config.RIFT_ENTER_COST_RECRUIT;
            case 2:
                return Config.RIFT_ENTER_COST_SOLDIER;
            case 3:
                return Config.RIFT_ENTER_COST_OFFICER;
            case 4:
                return Config.RIFT_ENTER_COST_CAPTAIN;
            case 5:
                return Config.RIFT_ENTER_COST_COMMANDER;
            case 6:
                return Config.RIFT_ENTER_COST_HERO;
            default:
                return 999999;
        }
    }

    public void showHtmlFile(L2PcInstance player, String file, L2NpcInstance npc) {
        NpcHtmlMessage html = new NpcHtmlMessage(npc.getObjectId());
        html.setFile(file);
        html.replace("%npc_name%", npc.getName());
        player.sendPacket(html);
    }

    public void handleCheat(L2PcInstance player, L2NpcInstance npc) {
        showHtmlFile(player, "data/html/seven_signs/rift/Cheater.htm", npc);
        if (!player.isGM()) {
            _log.warn("Player " + player.getName() + "(" + player.getObjectId() + ") was cheating in dimension rift area!");
            Util.handleIllegalPlayerAction(player, "Warning!! Character " + player.getName() + " tried to cheat in dimensional rift.", Config.DEFAULT_PUNISH);
        }
    }
}
