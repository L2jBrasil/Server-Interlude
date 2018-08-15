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
import com.l2jbr.gameserver.model.*;
import com.l2jbr.gameserver.model.actor.instance.L2FestivalMonsterInstance;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.model.actor.instance.L2RiftInvaderInstance;
import com.l2jbr.gameserver.model.actor.instance.L2SiegeGuardInstance;
import com.l2jbr.gameserver.model.entity.database.repository.CharacterRepository;
import com.l2jbr.gameserver.model.entity.database.repository.CursedWeaponRepository;
import com.l2jbr.gameserver.model.entity.database.repository.ItemRepository;
import com.l2jbr.gameserver.network.SystemMessageId;
import com.l2jbr.gameserver.serverpackets.SystemMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;

import static java.util.Objects.isNull;

/**
 * @author Micht
 */
public class CursedWeaponsManager {
    private static final Logger _log = LoggerFactory.getLogger(CursedWeaponsManager.class.getName());

    private static CursedWeaponsManager _instance;

    public static CursedWeaponsManager getInstance() {
        if (isNull(_instance)) {
            _instance = new CursedWeaponsManager();
        }
        return _instance;
    }


    private Map<Integer, CursedWeapon> _cursedWeapons;

    public CursedWeaponsManager() {
        _log.info("Initializing CursedWeaponsManager");
        _cursedWeapons = new HashMap<>();

        if (!Config.ALLOW_CURSED_WEAPONS) {
            return;
        }

        load();
        restore();
        controlPlayers();
        _log.info("Loaded : " + _cursedWeapons.size() + " cursed weapon(s).");
    }

    public final void reload() {
        _instance = new CursedWeaponsManager();
    }

    private void load() {
        _log.debug(" Parsing ... ");
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringComments(true);

            File file = new File(Config.DATAPACK_ROOT + "/data/cursedWeapons.xml");
            if (!file.exists()) {
                _log.debug("NO FILE");
                return;
            }

            Document doc = factory.newDocumentBuilder().parse(file);

            for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling()) {
                if ("list".equalsIgnoreCase(n.getNodeName())) {
                    for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling()) {
                        if ("item".equalsIgnoreCase(d.getNodeName())) {
                            NamedNodeMap attrs = d.getAttributes();
                            int id = Integer.parseInt(attrs.getNamedItem("id").getNodeValue());
                            int skillId = Integer.parseInt(attrs.getNamedItem("skillId").getNodeValue());
                            String name = attrs.getNamedItem("name").getNodeValue();

                            CursedWeapon cw = new CursedWeapon(id, skillId, name);

                            int val;
                            for (Node cd = d.getFirstChild(); cd != null; cd = cd.getNextSibling()) {
                                if ("dropRate".equalsIgnoreCase(cd.getNodeName())) {
                                    attrs = cd.getAttributes();
                                    val = Integer.parseInt(attrs.getNamedItem("val").getNodeValue());
                                    cw.setDropRate(val);
                                } else if ("duration".equalsIgnoreCase(cd.getNodeName())) {
                                    attrs = cd.getAttributes();
                                    val = Integer.parseInt(attrs.getNamedItem("val").getNodeValue());
                                    cw.setDuration(val);
                                } else if ("durationLost".equalsIgnoreCase(cd.getNodeName())) {
                                    attrs = cd.getAttributes();
                                    val = Integer.parseInt(attrs.getNamedItem("val").getNodeValue());
                                    cw.setDurationLost(val);
                                } else if ("disapearChance".equalsIgnoreCase(cd.getNodeName())) {
                                    attrs = cd.getAttributes();
                                    val = Integer.parseInt(attrs.getNamedItem("val").getNodeValue());
                                    cw.setDisapearChance(val);
                                } else if ("stageKills".equalsIgnoreCase(cd.getNodeName())) {
                                    attrs = cd.getAttributes();
                                    val = Integer.parseInt(attrs.getNamedItem("val").getNodeValue());
                                    cw.setStageKills(val);
                                }
                            }

                            // Store cursed weapon
                            _cursedWeapons.put(id, cw);
                        }
                    }
                }
            }
            _log.debug("OK");
        } catch (Exception e) {
            _log.error( "Error parsing cursed weapons file.", e);
        }
    }

    private void restore() {
        CursedWeaponRepository repository = DatabaseAccess.getRepository(CursedWeaponRepository.class);
        repository.findAll().forEach(cursedWeapon -> {
            CursedWeapon cw = _cursedWeapons.get(cursedWeapon.getId());
            cw.setPlayerId(cursedWeapon.getPlayerId());
            cw.setPlayerKarma(cursedWeapon.getPlayerKarma());
            cw.setPlayerPkKills(cursedWeapon.getPlayerPkKills());
            cw.setNbKills(cursedWeapon.getNbKills());
            cw.setEndTime(cursedWeapon.getEndTime());
            cw.reActivate();
        });
    }

    private void controlPlayers() {
        CharacterRepository characterRepository = DatabaseAccess.getRepository(CharacterRepository.class);
        ItemRepository itemRepository = DatabaseAccess.getRepository(ItemRepository.class);
        // TODO: See comments below...
        // This entire for loop should NOT be necessary, since it is already handled by
        // CursedWeapon.endOfLife(). However, if we indeed *need* to duplicate it for safety,
        // then we'd better make sure that it FULLY cleans up inactive cursed weapons!
        // Undesired effects result otherwise, such as player with no zariche but with karma
        // or a lost-child entry in the cursedweapons table, without a corresponding one in items...
        for (CursedWeapon cw : _cursedWeapons.values()) {
            if (cw.isActivated()) {
                continue;
            }

            // Do an item check to be sure that the cursed weapon isn't hold by someone
            int itemId = cw.getItemId();
            itemRepository.findOwnerIdByItem(itemId).ifPresent(owner -> {
                _log.warn("PROBLEM : Player {} owns the cursed weapon {} but he shouldn't.", owner, itemId);

                if(itemRepository.deleteByOwnerAndItem(owner, itemId) == 0) {
                    _log.warn("Error while deleting cursed weapon {} from userId {}", itemId, owner);
                }

                if(characterRepository.updatePKAndKarma(owner, cw.getPlayerPkKills(), cw.getPlayerKarma()) < 1){
                    _log.warn("Error while updating karma & pkkills for userId {}", cw.getPlayerId());
                }

                removeFromDb(itemId);
            });
        }
    }

    // =========================================================
    // Properties - Public
    public synchronized void checkDrop(L2Attackable attackable, L2PcInstance player) {
        if ((attackable instanceof L2SiegeGuardInstance) || (attackable instanceof L2RiftInvaderInstance) || (attackable instanceof L2FestivalMonsterInstance)) {
            return;
        }

        if (player.isCursedWeaponEquiped()) {
            return;
        }

        for (CursedWeapon cw : _cursedWeapons.values()) {
            if (cw.isActive()) {
                continue;
            }

            if (cw.checkDrop(attackable, player)) {
                break;
            }
        }
    }

    public void activate(L2PcInstance player, L2ItemInstance item) {
        CursedWeapon cw = _cursedWeapons.get(item.getItemId());
        if (player.isCursedWeaponEquiped()) // cannot own 2 cursed swords
        {
            CursedWeapon cw2 = _cursedWeapons.get(player.getCursedWeaponEquipedId());
            /*
             * TODO: give the bonus level in a more appropriate manner. The following code adds "_stageKills" levels. This will also show in the char status. I do not have enough info to know if the bonus should be shown in the pk count, or if it should be a full "_stageKills" bonus or just the
             * remaining from the current count till the of the current stage... This code is a TEMP fix, so that the cursed weapon's bonus level can be observed with as little change in the code as possible, until proper info arises.
             */
            cw2.setNbKills(cw2.getStageKills() - 1);
            cw2.increaseKills();

            // erase the newly obtained cursed weapon
            cw.setPlayer(player); // NECESSARY in order to find which inventory the weapon is in!
            cw.endOfLife(); // expire the weapon and clean up.
        } else {
            cw.activate(player, item);
        }
    }

    public void drop(int itemId, L2Character killer) {
        CursedWeapon cw = _cursedWeapons.get(itemId);

        cw.dropIt(killer);
    }

    public void increaseKills(int itemId) {
        CursedWeapon cw = _cursedWeapons.get(itemId);

        cw.increaseKills();
    }

    public int getLevel(int itemId) {
        CursedWeapon cw = _cursedWeapons.get(itemId);

        return cw.getLevel();
    }

    public static void announce(SystemMessage sm) {
        for (L2PcInstance player : L2World.getInstance().getAllPlayers()) {
            if (player == null) {
                continue;
            }

            player.sendPacket(sm);
        }
        if (Config.DEBUG) {
            _log.info("MessageID: " + sm.getMessageID());
        }
    }

    public void checkPlayer(L2PcInstance player) {
        if (player == null) {
            return;
        }

        for (CursedWeapon cw : _cursedWeapons.values()) {
            if (cw.isActivated() && (player.getObjectId() == cw.getPlayerId())) {
                cw.setPlayer(player);
                cw.setItem(player.getInventory().getItemByItemId(cw.getItemId()));
                cw.giveSkill();
                player.setCursedWeaponEquipedId(cw.getItemId());

                SystemMessage sm = new SystemMessage(SystemMessageId.S2_MINUTE_OF_USAGE_TIME_ARE_LEFT_FOR_S1);
                sm.addString(cw.getName());
                // sm.addItemName(cw.getId());
                sm.addNumber((int) ((cw.getEndTime() - System.currentTimeMillis()) / 60000));
                player.sendPacket(sm);
            }
        }
    }

    public static void removeFromDb(int itemId) {
        CursedWeaponRepository repository = DatabaseAccess.getRepository(CursedWeaponRepository.class);
        repository.deleteById(itemId);
    }

    public void saveData() {
        for (CursedWeapon cw : _cursedWeapons.values()) {
            cw.saveData();
        }
    }

    // =========================================================
    public boolean isCursed(int itemId) {
        return _cursedWeapons.containsKey(itemId);
    }

    public Collection<CursedWeapon> getCursedWeapons() {
        return _cursedWeapons.values();
    }

    public Set<Integer> getCursedWeaponsIds() {
        return _cursedWeapons.keySet();
    }

    public CursedWeapon getCursedWeapon(int itemId) {
        return _cursedWeapons.get(itemId);
    }

    public void givePassive(int itemId) {
        try {
            _cursedWeapons.get(itemId).giveSkill();
        } catch (Exception e) {
            /***/
        }
    }
}
