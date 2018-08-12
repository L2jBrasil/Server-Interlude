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

/**
 * @author godson
 */

package com.l2jbr.gameserver.model.entity;

import com.l2jbr.commons.database.DatabaseAccess;
import com.l2jbr.commons.util.Util;
import com.l2jbr.gameserver.Olympiad;
import com.l2jbr.gameserver.datatables.ClanTable;
import com.l2jbr.gameserver.model.L2Clan;
import com.l2jbr.gameserver.model.L2ItemInstance;
import com.l2jbr.gameserver.model.L2World;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.model.entity.database.Heroes;
import com.l2jbr.gameserver.model.entity.database.Nobles;
import com.l2jbr.gameserver.model.entity.database.repository.CharacterRepository;
import com.l2jbr.gameserver.model.entity.database.repository.HeroesRepository;
import com.l2jbr.gameserver.model.entity.database.repository.ItemRepository;
import com.l2jbr.gameserver.network.SystemMessageId;
import com.l2jbr.gameserver.serverpackets.InventoryUpdate;
import com.l2jbr.gameserver.serverpackets.PledgeShowInfoUpdate;
import com.l2jbr.gameserver.serverpackets.SystemMessage;
import com.l2jbr.gameserver.serverpackets.UserInfo;
import com.l2jbr.gameserver.templates.BodyPart;
import com.l2jbr.gameserver.templates.StatsSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class Hero {

	private static Logger _log = LoggerFactory.getLogger(Hero.class.getName());
	private static Hero _instance;
	
	private static final List<Integer> _heroItems = new ArrayList<>();
	
	static
	{
		_heroItems.add(6842);
		_heroItems.add(6611);
		_heroItems.add(6612);
		_heroItems.add(6613);
		_heroItems.add(6614);
		_heroItems.add(6615);
		_heroItems.add(6616);
		_heroItems.add(6617);
		_heroItems.add(6618);
		_heroItems.add(6619);
		_heroItems.add(6620);
		_heroItems.add(6621);
	}
	
	private static Map<Integer, StatsSet> _heroes;
	private static Map<Integer, StatsSet> _completeHeroes;
	
	public static final String COUNT = "count";
	public static final String PLAYED = "played";
	public static final String CLAN_NAME = "clan_name";
	public static final String CLAN_CREST = "clan_crest";
	public static final String ALLY_NAME = "ally_name";
	public static final String ALLY_CREST = "ally_crest";
	
	public static Hero getInstance()
	{
		if (_instance == null)
		{
			_instance = new Hero();
		}
		return _instance;
	}
	
	public Hero()
	{
		init();
	}
	
	private void init() {
		_heroes = new LinkedHashMap<>();
		_completeHeroes = new LinkedHashMap<>();

		CharacterRepository characterRepository = DatabaseAccess.getRepository(CharacterRepository.class);

		HeroesRepository heroesRepository = DatabaseAccess.getRepository(HeroesRepository.class);
		heroesRepository.findAll().forEach(heroes -> {
			StatsSet hero = new StatsSet();
			int charId = heroes.getId();
			hero.set(Olympiad.CHAR_NAME, heroes.getCharName());
			hero.set(Olympiad.CLASS_ID, heroes.getClassId());
			hero.set(COUNT, heroes.getCount());
			int played = heroes.getPlayed();
			hero.set(PLAYED, played);

			loadClanData(characterRepository, hero, charId);

			if(played > 0) {
				_heroes.put(charId, hero);
			}
			_completeHeroes.put(charId, hero);
		});

		_log.info("Hero System: Loaded " + _heroes.size() + " Heroes.");
		_log.info("Hero System: Loaded " + _completeHeroes.size() + " all time Heroes.");
	}

    private void loadClanData(CharacterRepository repository, StatsSet hero, int charId) {
        String clanName = "";
        String allyName = "";
        int clanCrest = 0;
        int allyCrest = 0;
        int clanId = repository.findClanIdById(charId);

        if(clanId > 0) {
            L2Clan clan = ClanTable.getInstance().getClan(clanId);
            clanName = clan.getName();
            clanCrest = clan.getCrestId();

            if (clan.getAllyId() > 0) {
                allyName = clan.getAllyName();
                allyCrest = clan.getAllyCrestId();
            }
        }

        hero.set(CLAN_CREST, clanCrest);
        hero.set(CLAN_NAME, clanName);
        hero.set(ALLY_CREST, allyCrest);
        hero.set(ALLY_NAME, allyName);
    }

    public Map<Integer, StatsSet> getHeroes()
	{
		return _heroes;
	}
	
	public synchronized void computeNewHeroes(List<Nobles> newHeroes)
	{
		updateHeroes(true);
		L2ItemInstance[] items;
		InventoryUpdate iu;
		
		if (_heroes.size() != 0)
		{
			for (StatsSet hero : _heroes.values())
			{
				String name = hero.getString(Olympiad.CHAR_NAME);
				
				L2PcInstance player = L2World.getInstance().getPlayer(name);
				
				if (player == null)
				{
					continue;
				}
				try
				{
					player.setHero(false);
					
					items = player.getInventory().unEquipItemInBodySlotAndRecord(BodyPart.TWO_HAND);
					iu = new InventoryUpdate();
					for (L2ItemInstance item : items)
					{
						iu.addModifiedItem(item);
					}
					player.sendPacket(iu);
					
					items = player.getInventory().unEquipItemInBodySlotAndRecord(BodyPart.RIGHT_HAND);
					iu = new InventoryUpdate();
					for (L2ItemInstance item : items)
					{
						iu.addModifiedItem(item);
					}
					player.sendPacket(iu);
					
					items = player.getInventory().unEquipItemInBodySlotAndRecord(BodyPart.HAIR);
					iu = new InventoryUpdate();
					for (L2ItemInstance item : items)
					{
						iu.addModifiedItem(item);
					}
					player.sendPacket(iu);
					
					items = player.getInventory().unEquipItemInBodySlotAndRecord(BodyPart.FACE);
					iu = new InventoryUpdate();
					for (L2ItemInstance item : items)
					{
						iu.addModifiedItem(item);
					}
					player.sendPacket(iu);
					
					items = player.getInventory().unEquipItemInBodySlotAndRecord(BodyPart.DHAIR);
					iu = new InventoryUpdate();
					for (L2ItemInstance item : items)
					{
						iu.addModifiedItem(item);
					}
					player.sendPacket(iu);
					
					for (L2ItemInstance item : player.getInventory().getAvailableItems(false))
					{
						if (item == null)
						{
							continue;
						}
						if (_heroItems.contains(item.getItemId()))
						{
							continue;
						}
						
						player.destroyItem("Hero", item, null, true);
						iu = new InventoryUpdate();
						iu.addRemovedItem(item);
						player.sendPacket(iu);
					}
					
					player.sendPacket(new UserInfo(player));
					player.broadcastUserInfo();
				}
				catch (NullPointerException e)
				{
				}
			}
		}
		
		if (newHeroes.size() == 0)
		{
			_heroes.clear();
			return;
		}
		
		Map<Integer, StatsSet> heroes = new LinkedHashMap<>();

		CharacterRepository repository = DatabaseAccess.getRepository(CharacterRepository.class);
		for (Nobles hero : newHeroes)
		{
			int charId = Objects.requireNonNullElse(hero.getId(), 0);
			
			if ((_completeHeroes != null) && _completeHeroes.containsKey(charId))
			{
				StatsSet oldHero = _completeHeroes.get(charId);
				int count = oldHero.getInteger(COUNT);
				oldHero.set(COUNT, count + 1);
				oldHero.set(PLAYED, 1);
				
				heroes.put(charId, oldHero);
			}
			else
			{
				StatsSet newHero = new StatsSet();
				newHero.set(Olympiad.CHAR_NAME, hero.getCharName());
				newHero.set(Olympiad.CLASS_ID, hero.getClassId());
				newHero.set(COUNT, 1);
				newHero.set(PLAYED, 1);
				
				heroes.put(charId, newHero);
			}
			loadClanData(repository, heroes.get(charId), charId);

		}
		
		deleteItemsInDb();
		
		_heroes.clear();
		_heroes.putAll(heroes);
		heroes.clear();
		
		updateHeroes(false);
		
		for (StatsSet hero : _heroes.values()) {
			String name = hero.getString(Olympiad.CHAR_NAME);
			L2PcInstance player = L2World.getInstance().getPlayer(name);
			
			if (player != null)
			{
				player.setHero(true);
				L2Clan clan = player.getClan();
                increaseClanReputation(name, clan);
                player.sendPacket(new UserInfo(player));
				player.broadcastUserInfo();
				
			} else {
                String clanName = hero.getString(CLAN_NAME);
                if (!Util.isNullOrEmpty(clanName)) {
                    L2Clan clan = ClanTable.getInstance().getClanByName(clanName);
                    increaseClanReputation(name, clan);
                }
            }
		}
	}

    private void increaseClanReputation(String name, L2Clan clan) {
        if (clan != null) {
            clan.setReputationScore(clan.getReputationScore() + 1000, true);
            clan.broadcastToOnlineMembers(new PledgeShowInfoUpdate(clan));
            SystemMessage sm = new SystemMessage(SystemMessageId.CLAN_MEMBER_S1_BECAME_HERO_AND_GAINED_S2_REPUTATION_POINTS);
            sm.addString(name);
            sm.addNumber(1000);
            clan.broadcastToOnlineMembers(sm);
        }
    }

	public void updateHeroes(boolean setDefault) {
        HeroesRepository heroesRepository = DatabaseAccess.getRepository(HeroesRepository.class);
        if (setDefault) {
            heroesRepository.updateAllResetPlayed();
        } else {

            CharacterRepository characterRepository = DatabaseAccess.getRepository(CharacterRepository.class);
            for (Integer heroId : _heroes.keySet()) {
                StatsSet hero = _heroes.get(heroId);

                if ((_completeHeroes == null) || !_completeHeroes.containsKey(heroId)) {
                    Heroes heroes = new Heroes(heroId, hero.getString(Olympiad.CHAR_NAME), hero.getInteger(Olympiad.CLASS_ID),
                            hero.getInteger(COUNT), hero.getInteger(PLAYED));

                    heroesRepository.save(heroes);

                    loadClanData(characterRepository, hero, heroId);

                    _heroes.remove(heroId);
                    _heroes.put(heroId, hero);
                    _completeHeroes.put(heroId, hero);
                } else {
                    heroesRepository.updateCountPlayed(heroId, hero.getInteger(COUNT), hero.getInteger(PLAYED));
                }
            }
        }
	}
	
	public List<Integer> getHeroItems()
	{
		return _heroItems;
	}
	
	private void deleteItemsInDb() {
        ItemRepository repository = DatabaseAccess.getRepository(ItemRepository.class);
        repository.deleteItemsFromBannedOwners(_heroItems);
	}
}