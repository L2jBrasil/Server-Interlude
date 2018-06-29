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
package com.l2jbr.gameserver.model;

import com.l2jbr.commons.database.DatabaseAccess;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.model.entity.database.repository.CharacterShortcutsRepository;
import com.l2jbr.gameserver.serverpackets.ExAutoSoulShot;
import com.l2jbr.gameserver.serverpackets.ShortCutInit;
import com.l2jbr.gameserver.templates.ItemType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.TreeMap;


/**
 * This class ...
 * @version $Revision: 1.1.2.1.2.3 $ $Date: 2005/03/27 15:29:33 $
 */
public class ShortCuts
{
	private static Logger _log = LoggerFactory.getLogger(ShortCuts.class.getName());
	
	private final L2PcInstance _owner;
	private final Map<Integer, L2ShortCut> _shortCuts = new TreeMap<>();
	
	public ShortCuts(L2PcInstance owner)
	{
		_owner = owner;
	}
	
	public L2ShortCut[] getAllShortCuts()
	{
		return _shortCuts.values().toArray(new L2ShortCut[_shortCuts.values().size()]);
	}
	
	public L2ShortCut getShortCut(int slot, int page)
	{
		L2ShortCut sc = _shortCuts.get(slot + (page * 12));
		
		// verify shortcut
		if ((sc != null) && (sc.getType() == L2ShortCut.TYPE_ITEM))
		{
			if (_owner.getInventory().getItemByObjectId(sc.getId()) == null)
			{
				deleteShortCut(sc.getSlot(), sc.getPage());
				sc = null;
			}
		}
		
		return sc;
	}
	
	public synchronized void registerShortCut(L2ShortCut shortcut)
	{
		L2ShortCut oldShortCut = _shortCuts.put(shortcut.getSlot() + (12 * shortcut.getPage()), shortcut);
		registerShortCutInDb(shortcut, oldShortCut);
	}
	
	private void registerShortCutInDb(L2ShortCut shortcut, L2ShortCut oldShortCut) {
		if (oldShortCut != null) {
			deleteShortCutFromDb(oldShortCut);
		}

		CharacterShortcutsRepository repository = DatabaseAccess.getRepository(CharacterShortcutsRepository.class);
        repository.saveOrUpdate(_owner.getObjectId(), shortcut.getId(), shortcut.getSlot(), shortcut.getPage(), shortcut.getType(),
                shortcut.getLevel(), _owner.getClassIndex());
	}
	
	/**
	 * @param slot
	 * @param page
	 */
	public synchronized void deleteShortCut(int slot, int page)
	{
		L2ShortCut old = _shortCuts.remove(slot + (page * 12));
		
		if ((old == null) || (_owner == null))
		{
			return;
		}
		deleteShortCutFromDb(old);
		if (old.getType() == L2ShortCut.TYPE_ITEM)
		{
			L2ItemInstance item = _owner.getInventory().getItemByObjectId(old.getId());
			
			if ((item != null) && (item.getItemType() == ItemType.SHOT))
			{
				_owner.removeAutoSoulShot(item.getItemId());
				_owner.sendPacket(new ExAutoSoulShot(item.getItemId(), 0));
			}
		}
		
		_owner.sendPacket(new ShortCutInit(_owner));
		
		for (int shotId : _owner.getAutoSoulShot().values())
		{
			_owner.sendPacket(new ExAutoSoulShot(shotId, 1));
		}
	}
	
	public synchronized void deleteShortCutByObjectId(int objectId)
	{
		L2ShortCut toRemove = null;
		
		for (L2ShortCut shortcut : _shortCuts.values())
		{
			if ((shortcut.getType() == L2ShortCut.TYPE_ITEM) && (shortcut.getId() == objectId))
			{
				toRemove = shortcut;
				break;
			}
		}
		
		if (toRemove != null)
		{
			deleteShortCut(toRemove.getSlot(), toRemove.getPage());
		}
	}

	private void deleteShortCutFromDb(L2ShortCut shortcut) {
	    CharacterShortcutsRepository repository = DatabaseAccess.getRepository(CharacterShortcutsRepository.class);
	    repository.delete(_owner.getObjectId(), shortcut.getSlot(), shortcut.getPage(), _owner.getClassIndex());
	}
	
	public void restore()
	{
		_shortCuts.clear();
        CharacterShortcutsRepository repository = DatabaseAccess.getRepository(CharacterShortcutsRepository.class);
        repository.finAllByClassIndex(_owner.getObjectId(), _owner.getClassIndex()).forEach(shortcut -> {
            int slot = shortcut.getSlot();
            int page = shortcut.getPage();
            int type = shortcut.getType();
            int id = shortcut.getShortcutId();
            int level = shortcut.getLevel();

            L2ShortCut sc = new L2ShortCut(slot, page, type, id, level, 1);
            _shortCuts.put(slot + (page * 12), sc);
        });

		// verify shortcuts
		for (L2ShortCut sc : getAllShortCuts())
		{
			if (sc.getType() == L2ShortCut.TYPE_ITEM)
			{
				if (_owner.getInventory().getItemByObjectId(sc.getId()) == null)
				{
					deleteShortCut(sc.getSlot(), sc.getPage());
				}
			}
		}
	}
}
