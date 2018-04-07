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
package com.l2jbr.gameserver.model.zone.type;

import com.l2jbr.gameserver.datatables.MapRegionTable;
import com.l2jbr.gameserver.instancemanager.CastleManager;
import com.l2jbr.gameserver.model.L2Character;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.model.actor.instance.L2SiegeSummonInstance;
import com.l2jbr.gameserver.model.entity.Castle;
import com.l2jbr.gameserver.model.zone.L2ZoneType;
import com.l2jbr.gameserver.network.SystemMessageId;
import com.l2jbr.gameserver.serverpackets.SystemMessage;

import java.util.LinkedList;
import java.util.List;


/**
 * A castle zone
 *
 * @author durgus
 */
public class L2CastleZone extends L2ZoneType {
    private int _castleId;
    private Castle _castle;
    private final int[] _spawnLoc;

    public L2CastleZone() {
        super();

        _spawnLoc = new int[3];
    }

    @Override
    public void setParameter(String name, String value) {
        if (name.equals("castleId")) {
            _castleId = Integer.parseInt(value);

            // Register self to the correct castle
            _castle = CastleManager.getInstance().getCastleById(_castleId);
            _castle.setZone(this);
        } else if (name.equals("spawnX")) {
            _spawnLoc[0] = Integer.parseInt(value);
        } else if (name.equals("spawnY")) {
            _spawnLoc[1] = Integer.parseInt(value);
        } else if (name.equals("spawnZ")) {
            _spawnLoc[2] = Integer.parseInt(value);
        } else {
            super.setParameter(name, value);
        }
    }

    @Override
    protected void onEnter(L2Character character) {
        if (_castle.getSiege().getIsInProgress()) {
            character.setInsideZone(L2Character.ZONE_PVP, true);
            character.setInsideZone(L2Character.ZONE_SIEGE, true);

            if (character instanceof L2PcInstance) {
                ((L2PcInstance) character).sendPacket(new SystemMessage(SystemMessageId.ENTERED_COMBAT_ZONE));
            }
        }
    }

    @Override
    protected void onExit(L2Character character) {
        if (_castle.getSiege().getIsInProgress()) {
            character.setInsideZone(L2Character.ZONE_PVP, false);
            character.setInsideZone(L2Character.ZONE_SIEGE, false);

            if (character instanceof L2PcInstance) {
                ((L2PcInstance) character).sendPacket(new SystemMessage(SystemMessageId.LEFT_COMBAT_ZONE));

                // Set pvp flag
                if (((L2PcInstance) character).getPvpFlag() == 0) {
                    ((L2PcInstance) character).startPvPFlag();
                }
            }
        }
        if (character instanceof L2SiegeSummonInstance) {
            ((L2SiegeSummonInstance) character).unSummon(((L2SiegeSummonInstance) character).getOwner());
        }
    }

    @Override
    protected void onDieInside(L2Character character) {
    }

    @Override
    protected void onReviveInside(L2Character character) {
    }

    public void updateZoneStatusForCharactersInside() {
        if (_castle.getSiege().getIsInProgress()) {
            for (L2Character character : _characterList.values()) {
                try {
                    onEnter(character);
                } catch (NullPointerException e) {
                }
            }
        } else {
            for (L2Character character : _characterList.values()) {
                try {
                    character.setInsideZone(L2Character.ZONE_PVP, false);
                    character.setInsideZone(L2Character.ZONE_SIEGE, false);

                    if (character instanceof L2PcInstance) {
                        ((L2PcInstance) character).sendPacket(new SystemMessage(SystemMessageId.LEFT_COMBAT_ZONE));
                    }
                    if (character instanceof L2SiegeSummonInstance) {
                        ((L2SiegeSummonInstance) character).unSummon(((L2SiegeSummonInstance) character).getOwner());
                    }
                } catch (NullPointerException e) {
                }
            }
        }
    }

    /**
     * Removes all foreigners from the castle
     *
     * @param owningClanId
     */
    public void banishForeigners(int owningClanId) {
        for (L2Character temp : _characterList.values()) {
            if (!(temp instanceof L2PcInstance)) {
                continue;
            }
            if (((L2PcInstance) temp).getClanId() == owningClanId) {
                continue;
            }

            ((L2PcInstance) temp).teleToLocation(MapRegionTable.TeleportWhereType.Town);
        }
    }

    /**
     * Sends a message to all players in this zone
     *
     * @param message
     */
    public void announceToPlayers(String message) {
        for (L2Character temp : _characterList.values()) {
            if (temp instanceof L2PcInstance) {
                ((L2PcInstance) temp).sendMessage(message);
            }
        }
    }

    /**
     * Returns all players within this zone
     *
     * @return
     */
    public List<L2PcInstance> getAllPlayers() {
        List<L2PcInstance> players = new LinkedList<>();

        for (L2Character temp : _characterList.values()) {
            if (temp instanceof L2PcInstance) {
                players.add((L2PcInstance) temp);
            }
        }

        return players;
    }

    /**
     * Get the castles defender spawn
     *
     * @return
     */
    public int[] getSpawn() {
        return _spawnLoc;
    }
}
