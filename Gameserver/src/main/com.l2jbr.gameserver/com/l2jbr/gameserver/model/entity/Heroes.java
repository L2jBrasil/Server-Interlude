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
 *
 * @author godson
 **/
package com.l2jbr.gameserver.model.entity;

import com.l2jbr.commons.util.Util;
import com.l2jbr.gameserver.datatables.ClanTable;
import com.l2jbr.gameserver.model.L2Clan;
import com.l2jbr.gameserver.model.L2ItemInstance;
import com.l2jbr.gameserver.model.L2World;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.model.entity.database.Hero;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.l2jbr.commons.database.DatabaseAccess.getRepository;
import static java.util.Objects.isNull;

public class Heroes {

	private static Logger _log = LoggerFactory.getLogger(Heroes.class);
	private static Heroes _instance;
	
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
	
	private static Map<Integer, Hero> _heroes;
	private static Map<Integer, Hero> _completeHeroes;
	
	public static Heroes getInstance() {
		if (isNull(_instance)) {
			_instance = new Heroes();
		}
		return _instance;
	}
	
	private Heroes() {
		init();
	}
	
	private void init() {
		_heroes = new HashMap<>();
		_completeHeroes = new HashMap<>();

		CharacterRepository characterRepository = getRepository(CharacterRepository.class);

		getRepository(HeroesRepository.class).findAll().forEach(hero -> {
			int charId = hero.getId();

			loadClanData(characterRepository, hero);

			if(hero.getPlayed() > 0) {
				_heroes.put(charId, hero);
			}
			_completeHeroes.put(charId, hero);
		});

		_log.info("Heroes System: Loaded {} Hero.", _heroes.size());
		_log.info("Heroes System: Loaded {} all times Hero.", _completeHeroes.size());
	}

    private void loadClanData(CharacterRepository repository, Hero hero) {
        String clanName = "";
        String allyName = "";
        int clanCrest = 0;
        int allyCrest = 0;
        int clanId = repository.findClanIdById(hero.getId());

        if(clanId > 0) {
            L2Clan clan = ClanTable.getInstance().getClan(clanId);
            clanName = clan.getName();
            clanCrest = clan.getCrestId();

            if (clan.getAllyId() > 0) {
                allyName = clan.getAllyName();
                allyCrest = clan.getAllyCrestId();
            }
        }

        hero.setClanCrest(clanCrest);
        hero.setClanName(clanName);
        hero.setAllyCrest(allyCrest);
        hero.setAllyName(allyName);
    }

    public Map<Integer, Hero> getHeroes()
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
			for (Hero hero : _heroes.values())
			{
				String name = hero.getCharName();
				
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
						
						player.destroyItem("Heroes", item, null, true);
						iu = new InventoryUpdate();
						iu.addRemovedItem(item);
						player.sendPacket(iu);
					}
					
					player.sendPacket(new UserInfo(player));
					player.broadcastUserInfo();
				}
				catch (NullPointerException e) {
				    _log.error(e.getLocalizedMessage(), e);
				}
			}
		}
		
		if (newHeroes.size() == 0)
		{
			_heroes.clear();
			return;
		}
		
		Map<Integer, Hero> heroes = new LinkedHashMap<>();

		CharacterRepository repository = getRepository(CharacterRepository.class);
		for (Nobles hero : newHeroes)  {
			int charId = Objects.requireNonNullElse(hero.getId(), 0);
			
			if ((_completeHeroes != null) && _completeHeroes.containsKey(charId))  {
				Hero oldHero = _completeHeroes.get(charId);
				oldHero.incrementCount();
				oldHero.setPlayed(1);
				heroes.put(charId, oldHero);
			}
			else {
				Hero newHero = new Hero(hero);
				heroes.put(charId, newHero);
			}
			loadClanData(repository, heroes.get(charId));

		}
		
		deleteItemsInDb();
		
		_heroes.clear();
		_heroes.putAll(heroes);
		heroes.clear();
		
		updateHeroes(false);
		
		for (Hero hero : _heroes.values()) {
			String name = hero.getCharName();
			L2PcInstance player = L2World.getInstance().getPlayer(name);
			
			if (player != null)
			{
				player.setHero(true);
				L2Clan clan = player.getClan();
                increaseClanReputation(name, clan);
                player.sendPacket(new UserInfo(player));
				player.broadcastUserInfo();
				
			} else {
                String clanName = hero.getClanName();
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

	private void updateHeroes(boolean setDefault) {
        HeroesRepository heroesRepository = getRepository(HeroesRepository.class);
        if (setDefault) {
            heroesRepository.updateAllResetPlayed();
        } else {

            CharacterRepository characterRepository = getRepository(CharacterRepository.class);
            for (Integer heroId : _heroes.keySet()) {
                Hero hero = _heroes.get(heroId);

                if ((_completeHeroes == null) || !_completeHeroes.containsKey(heroId)) {

                    heroesRepository.save(hero);

                    loadClanData(characterRepository, hero);

                    _heroes.remove(heroId);
                    _heroes.put(heroId, hero);
                    _completeHeroes.put(heroId, hero);
                } else {
                    heroesRepository.updateCountPlayed(heroId, hero.getCount(), hero.getPlayed());
                }
            }
        }
	}
	
	private void deleteItemsInDb() {
        ItemRepository repository = getRepository(ItemRepository.class);
        repository.deleteItemsFromBannedOwners(_heroItems);
	}
}