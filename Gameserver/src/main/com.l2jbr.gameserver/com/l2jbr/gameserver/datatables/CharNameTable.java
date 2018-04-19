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

import com.l2jbr.commons.database.DatabaseAccess;
import com.l2jbr.gameserver.model.database.repository.CharacterRepository;

/**
 * This class ...
 * @version $Revision: 1.3.2.2.2.1 $ $Date: 2005/03/27 15:29:18 $
 */
public class CharNameTable {
	
	private static CharNameTable _instance;
	
	public static CharNameTable getInstance() {
		if (_instance == null) {
			_instance = new CharNameTable();
		}
		return _instance;
	}
	
	public boolean doesCharNameExist(String name) {
        CharacterRepository repository = DatabaseAccess.getRepository(CharacterRepository.class);
        return repository.existsByName(name);
	}
	
	public int accountCharNumber(String account) {
	    CharacterRepository repository = DatabaseAccess.getRepository(CharacterRepository.class);
	    return repository.countByAccount(account);
	}
}
