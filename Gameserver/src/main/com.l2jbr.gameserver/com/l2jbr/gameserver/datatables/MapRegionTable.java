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

import com.l2jbr.gameserver.instancemanager.ArenaManager;
import com.l2jbr.gameserver.instancemanager.CastleManager;
import com.l2jbr.gameserver.instancemanager.ClanHallManager;
import com.l2jbr.gameserver.instancemanager.TownManager;
import com.l2jbr.gameserver.model.L2Character;
import com.l2jbr.gameserver.model.Location;
import com.l2jbr.gameserver.model.actor.instance.L2NpcInstance;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.model.entity.Castle;
import com.l2jbr.gameserver.model.entity.ClanHall;
import com.l2jbr.gameserver.model.entity.database.repository.MapRegionRepository;
import com.l2jbr.gameserver.model.zone.type.L2ArenaZone;
import com.l2jbr.gameserver.model.zone.type.L2ClanHallZone;

import java.util.List;

import static com.l2jbr.commons.database.DatabaseAccess.getRepository;
import static java.util.Objects.isNull;


public class MapRegionTable {
	
	private static MapRegionTable _instance;
	
	private final int[][] _regions = new int[19][21];
	
	private final int[][] _pointsWithKarmas;
	
	public enum TeleportWhereType
	{
		Castle,
		ClanHall,
		SiegeFlag,
		Town
	}
	
	public static MapRegionTable getInstance() {
		if (isNull(_instance)) {
			_instance = new MapRegionTable();
		}
		return _instance;
	}
	
	private MapRegionTable() {
		getRepository(MapRegionRepository.class).findAll().forEach(mapRegion -> {
            _regions[0][mapRegion.getRegion()] = mapRegion.getSec0();
            _regions[1][mapRegion.getRegion()] = mapRegion.getSec1();
            _regions[2][mapRegion.getRegion()] = mapRegion.getSec2();
            _regions[3][mapRegion.getRegion()] = mapRegion.getSec3();
            _regions[4][mapRegion.getRegion()] = mapRegion.getSec4();
            _regions[5][mapRegion.getRegion()] = mapRegion.getSec5();
            _regions[6][mapRegion.getRegion()] = mapRegion.getSec6();
            _regions[7][mapRegion.getRegion()] = mapRegion.getSec7();
            _regions[8][mapRegion.getRegion()] = mapRegion.getSec8();
            _regions[9][mapRegion.getRegion()] = mapRegion.getSec9();

        });

		_pointsWithKarmas = new int[19][3];
		// Talking Island
		_pointsWithKarmas[0][0] = -79077;
		_pointsWithKarmas[0][1] = 240355;
		_pointsWithKarmas[0][2] = -3440;
		// Elven
		_pointsWithKarmas[1][0] = 43503;
		_pointsWithKarmas[1][1] = 40398;
		_pointsWithKarmas[1][2] = -3450;
		// DarkElven
		_pointsWithKarmas[2][0] = 1675;
		_pointsWithKarmas[2][1] = 19581;
		_pointsWithKarmas[2][2] = -3110;
		// Orc
		_pointsWithKarmas[3][0] = -44413;
		_pointsWithKarmas[3][1] = -121762;
		_pointsWithKarmas[3][2] = -235;
		// Dwalf
		_pointsWithKarmas[4][0] = 12009;
		_pointsWithKarmas[4][1] = -187319;
		_pointsWithKarmas[4][2] = -3309;
		// Gludio
		_pointsWithKarmas[5][0] = -18872;
		_pointsWithKarmas[5][1] = 126216;
		_pointsWithKarmas[5][2] = -3280;
		// Gludin
		_pointsWithKarmas[6][0] = -85915;
		_pointsWithKarmas[6][1] = 150402;
		_pointsWithKarmas[6][2] = -3060;
		// Dion
		_pointsWithKarmas[7][0] = 23652;
		_pointsWithKarmas[7][1] = 144823;
		_pointsWithKarmas[7][2] = -3330;
		// Giran
		_pointsWithKarmas[8][0] = 79125;
		_pointsWithKarmas[8][1] = 154197;
		_pointsWithKarmas[8][2] = -3490;
		// Oren
		_pointsWithKarmas[9][0] = 73840;
		_pointsWithKarmas[9][1] = 58193;
		_pointsWithKarmas[9][2] = -2730;
		// Aden
		_pointsWithKarmas[10][0] = 44413;
		_pointsWithKarmas[10][1] = 22610;
		_pointsWithKarmas[10][2] = 235;
		// Hunters
		_pointsWithKarmas[11][0] = 114137;
		_pointsWithKarmas[11][1] = 72993;
		_pointsWithKarmas[11][2] = -2445;
		// Giran
		_pointsWithKarmas[12][0] = 79125;
		_pointsWithKarmas[12][1] = 154197;
		_pointsWithKarmas[12][2] = -3490;
		// heine
		_pointsWithKarmas[13][0] = 119536;
		_pointsWithKarmas[13][1] = 218558;
		_pointsWithKarmas[13][2] = -3495;
		// Rune Castle Town
		_pointsWithKarmas[14][0] = 42931;
		_pointsWithKarmas[14][1] = -44733;
		_pointsWithKarmas[14][2] = -1326;
		// Goddard
		_pointsWithKarmas[15][0] = 147419;
		_pointsWithKarmas[15][1] = -64980;
		_pointsWithKarmas[15][2] = -3457;
		// Schuttgart
		_pointsWithKarmas[16][0] = 85184;
		_pointsWithKarmas[16][1] = -138560;
		_pointsWithKarmas[16][2] = -2256;
		// Primeval Isle
		// _pointsWithKarmas[18][0] = 10468;
		// _pointsWithKarmas[18][1] = -24569;
		// _pointsWithKarmas[18][2] = -3645;
	}
	
	public final int getMapRegion(int posX, int posY)
	{
		return _regions[getMapRegionX(posX)][getMapRegionY(posY)];
	}
	
	public final int getMapRegionX(int posX)
	{
		return (posX >> 15) + 4;// + centerTileX;
	}
	
	private int getMapRegionY(int posY)
	{
		return (posY >> 15) + 10;// + centerTileX;
	}
	
	public int getAreaCastle(L2Character activeChar)
	{
		int area = getClosestTownNumber(activeChar);
		int castle;
		switch (area)
		{
			case 0: // Talking Island Village
            case 5: // Town of Gludio
            case 6:// Gludin Village
				castle = 1;
				break;
			case 1: // Elven Village
			case 2: // Dark Elven Village
            case 9: // Town of Oren
            case 17: // Ivory Tower
				castle = 4;
				break;
			case 3: // Orc Village
            case 4: // Dwarven Village
            case 16: // Town of Shuttgart
				castle = 9;
				break;
			case 7: // Town of Dion
				castle = 2;
				break;
			case 8: // Town of Giran
            case 12: // Giran Harbor
				castle = 3;
				break;
			case 10: // Town of Aden
            case 11: // Hunters Village
				castle = 5;
				break;
			case 13: // Heine
				castle = 6;
				break;
			case 14: // Rune Township
            case 18: // Primeval Isle Wharf
				castle = 8;
				break;
			case 15: // Town of Goddard
				castle = 7;
				break;
			default:  // Town of Aden
				castle = 5;
				break;
		}
		return castle;
	}
	
	public int getClosestTownNumber(L2Character activeChar)
	{
		return getMapRegion(activeChar.getX(), activeChar.getY());
	}
	
	public String getClosestTownName(L2Character activeChar)
	{
		int nearestTownId = getMapRegion(activeChar.getX(), activeChar.getY());
		String nearestTown;
		
		switch (nearestTownId)
		{
			case 0:
				nearestTown = "Talking Island Village";
				break;
			case 1:
				nearestTown = "Elven Village";
				break;
			case 2:
				nearestTown = "Dark Elven Village";
				break;
			case 3:
				nearestTown = "Orc Village";
				break;
			case 4:
				nearestTown = "Dwarven Village";
				break;
			case 5:
				nearestTown = "Town of Gludio";
				break;
			case 6:
				nearestTown = "Gludin Village";
				break;
			case 7:
				nearestTown = "Town of Dion";
				break;
			case 8:
				nearestTown = "Town of Giran";
				break;
			case 9:
				nearestTown = "Town of Oren";
				break;
			case 10:
				nearestTown = "Town of Aden";
				break;
			case 11:
				nearestTown = "Hunters Village";
				break;
			case 12:
				nearestTown = "Giran Harbor";
				break;
			case 13:
				nearestTown = "Heine";
				break;
			case 14:
				nearestTown = "Rune Township";
				break;
			case 15:
				nearestTown = "Town of Goddard";
				break;
			case 16:
				nearestTown = "Town of Shuttgart";
				break; // //TODO@ (Check mapregion table)[Luno]
			case 18:
				nearestTown = "Primeval Isle";
				break;
			default:
				nearestTown = "Town of Aden";
				break;
		
		}
		
		return nearestTown;
	}
	
	public Location getTeleToLocation(L2Character activeChar, TeleportWhereType teleportWhere)
	{
		int[] coord;
		
		if (activeChar instanceof L2PcInstance)
		{
			L2PcInstance player = ((L2PcInstance) activeChar);
			
			// If in Monster Derby Track
			if (player.isInsideZone(L2Character.ZONE_MONSTERTRACK))
			{
				return new Location(12661, 181687, -3560);
			}
			
			Castle castle = null;
			ClanHall clanhall;
			
			if (player.getClan() != null)
			{
				// If teleport to clan hall
				if (teleportWhere == TeleportWhereType.ClanHall)
				{
					
					clanhall = ClanHallManager.getInstance().getClanHallByOwner(player.getClan());
					if (clanhall != null)
					{
						L2ClanHallZone zone = clanhall.getZone();
						if (zone != null)
						{
							return zone.getSpawn();
						}
					}
				}
				
				// If teleport to castle
				if (teleportWhere == TeleportWhereType.Castle)
				{
					castle = CastleManager.getInstance().getCastleByOwner(player.getClan());
				}
				
				// Check if player is on castle ground
				if (castle == null)
				{
					castle = CastleManager.getInstance().getCastle(player);
				}
				
				if ((castle != null) && (castle.getCastleId() > 0))
				{
					// If Teleporting to castle or
					// If is on caslte with siege and player's clan is defender
					if ((teleportWhere == TeleportWhereType.Castle) || (castle.getSiege().getIsInProgress() && (castle.getSiege().getDefenderClan(player.getClan()) != null)))
					{
						coord = castle.getZone().getSpawn();
						return new Location(coord[0], coord[1], coord[2]);
					}
					
					if ((teleportWhere == TeleportWhereType.SiegeFlag) && castle.getSiege().getIsInProgress())
					{
						// Check if player's clan is attacker
						List<L2NpcInstance> flags = castle.getSiege().getFlag(player.getClan());
						if ((flags != null) && !flags.isEmpty())
						{
							// Spawn to flag - Need more work to get player to the nearest flag
							L2NpcInstance flag = flags.get(0);
							return new Location(flag.getX(), flag.getY(), flag.getZ());
						}
					}
				}
			}
			
			// teleport RED PK 5+ to Floran Village
			if ((player.getPkKills() > 5) && (player.getKarma() > 1))
			{
				return new Location(17817, 170079, -3530);
			}
			// Karma player land out of city
			if (player.getKarma() > 1)
			{
				int closest = getMapRegion(activeChar.getX(), activeChar.getY());
				if ((closest >= 0) && (closest < _pointsWithKarmas.length))
				{
					return new Location(_pointsWithKarmas[closest][0], _pointsWithKarmas[closest][1], _pointsWithKarmas[closest][2]);
				}
				return new Location(17817, 170079, -3530);
			}
			
			// Checking if in arena
			L2ArenaZone arena = ArenaManager.getInstance().getArena(player);
			if (arena != null)
			{
				coord = arena.getSpawnLoc();
				return new Location(coord[0], coord[1], coord[2]);
			}
		}
		
		// Get the nearest town
		// TODO: Micht: Maybe we should add some checks to prevent exception here.
		coord = TownManager.getInstance().getClosestTown(activeChar).getSpawnLoc();
		
		return new Location(coord[0], coord[1], coord[2]);
	}
}
