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
package com.l2jbr.gameserver.handler;

import com.l2jbr.gameserver.handler.itemhandlers.*;

import java.util.Map;
import java.util.TreeMap;

import static java.util.Objects.isNull;

public class ItemHandler {

	private static ItemHandler _instance;
	
	private final Map<Integer, IItemHandler> _datatable;

	public static ItemHandler getInstance() {
		if (isNull(_instance)) {
			_instance = new ItemHandler();
		}
		return _instance;
	}
	
	/**
	 * Returns the number of elements contained in datatable
	 * @return int : Size of the datatable
	 */
	public int size()
	{
		return _datatable.size();
	}

	private ItemHandler() {
		_datatable = new TreeMap<>();
		load();
	}

	private void load() {
        registerItemHandler(new ScrollOfEscape());
        registerItemHandler(new ScrollOfResurrection());
        registerItemHandler(new SoulShots());
        registerItemHandler(new SpiritShot());
        registerItemHandler(new BlessedSpiritShot());
        registerItemHandler(new BeastSoulShot());
        registerItemHandler(new BeastSpiritShot());
        registerItemHandler(new ChestKey());
        registerItemHandler(new PaganKeys());
        registerItemHandler(new Maps());
        registerItemHandler(new Potions());
        registerItemHandler(new Recipes());
        registerItemHandler(new RollingDice());
        registerItemHandler(new MysteryPotion());
        registerItemHandler(new EnchantScrolls());
        registerItemHandler(new EnergyStone());
        registerItemHandler(new Book());
        registerItemHandler(new Remedy());
        registerItemHandler(new Scrolls());
        registerItemHandler(new CrystalCarol());
        registerItemHandler(new SoulCrystals());
        registerItemHandler(new SevenSignsRecord());
        registerItemHandler(new CharChangePotions());
        registerItemHandler(new Firework());
        registerItemHandler(new Seed());
        registerItemHandler(new Harvester());
        registerItemHandler(new MercTicket());
        registerItemHandler(new FishShots());
        registerItemHandler(new ExtractableItems());
        registerItemHandler(new SpecialXMas());
        registerItemHandler(new SummonItems());
        registerItemHandler(new BeastSpice());
    }

	/**
	 * Adds handler of item type in <I>datatable</I>.<BR>
	 * <BR>
	 * <B><I>Concept :</I></U><BR>
	 * This handler is put in <I>datatable</I> Map &lt;Integer ; IItemHandler &gt; for each ID corresponding to an item type (existing in classes of package itemhandlers) sets as key of the Map.
	 * @param handler (IItemHandler)
	 */
    private void registerItemHandler(IItemHandler handler) {
		// Get all ID corresponding to the item type of the handler
		int[] ids = handler.getItemIds();
		// Add handler for each ID found
		for (int id : ids) {
			_datatable.put(id, handler);
		}
	}
	
	/**
	 * Returns the handler of the item
	 * @param itemId : int designating the itemID
	 * @return IItemHandler
	 */
	public IItemHandler getItemHandler(int itemId)
	{
		return _datatable.get(itemId);
	}
}
