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
package com.l2jbr.gameserver.idfactory;

import com.l2jbr.commons.database.DatabaseAccess;
import com.l2jbr.gameserver.model.entity.database.repository.CharacterRepository;
import com.l2jbr.gameserver.model.entity.database.repository.IdFactoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.isNull;

public abstract class IdFactory {
	private static Logger _log = LoggerFactory.getLogger(IdFactory.class.getName());

	static final int FIRST_OID = 0x10000000;
	protected static final int LAST_OID = 0x7FFFFFFF;
	protected static final int FREE_OBJECT_ID_SIZE = LAST_OID - FIRST_OID;
	
	protected static IdFactory _instance = null;

    public static IdFactory getInstance() {
        if(isNull(_instance)) {
            _instance = new BitSetIDFactory();
        }
        return _instance;
    }
	
	protected IdFactory() {
		setAllCharacterOffline();
		cleanUpDB();
	}

	protected void setAllCharacterOffline()  {
        CharacterRepository repository = DatabaseAccess.getRepository(CharacterRepository.class);
        repository.updateAllOnlineStatus(0);
	}

	protected void cleanUpDB() {
        // TODO Database should ensure these data integrity using constraints
	    IdFactoryRepository repository = DatabaseAccess.getRepository(IdFactoryRepository.class);
        int cleanCount = repository.deleteCharacterFriendInconsistency();
        cleanCount += repository.deleteCharacterMacrosesInconsistency();
        cleanCount += repository.deleteCharacterQuestsInconsistency();
        cleanCount += repository.deleteCharacterRecipebookInconsistency();
        cleanCount += repository.deleteCharacterShortcutsInconsistency();
        cleanCount += repository.deleteCharacterSkillsInconsistency();
        cleanCount += repository.deleteCharacterSkillsSaveInconsistency();
        cleanCount += repository.deleteCharacterSubclassesInconsistency();
        cleanCount += repository.deleteCharacterHennasInconsistency();
        cleanCount += repository.deleteCursedWeaponsInconsistency();
        cleanCount += repository.deleteHeroesInconsistency();
        cleanCount += repository.deleteOlympiadNoblesInconsistency();
        cleanCount += repository.deletePetsInconsistency();
        cleanCount += repository.deleteSevenSignsInconsistency();
        cleanCount += repository.deleteAuctionInconsistency();
        cleanCount += repository.deleteAuctionBidInconsistency();
        cleanCount += repository.deleteClanDataInconsistency();
        cleanCount += repository.deleteAuctionBidderInconsistency();
        cleanCount += repository.deleteClanHallFunctionsInconsistency();
        cleanCount += repository.deleteClanPrivsInconsistency();
        cleanCount += repository.deleteClanSkillsInconsistency();
        cleanCount += repository.deleteClanSubpledgeInconsistency();
        cleanCount += repository.deleteClanWarsInconsistency();
        cleanCount += repository.deleteClanWarsUnderAttackInconsistency();
        cleanCount += repository.deleteSiegeClansrInconsistency();
        cleanCount += repository.deleteItemsInconsistency();
        cleanCount += repository.deleteForumInconsistency();
        cleanCount += repository.deleteTopicInconsistency();
        cleanCount += repository.deletePostInconsistency();

        repository.updateClanData();
        repository.updateCastle();
        repository.updateCharacters();

        _log.info("Cleaned {} elements from database.", cleanCount);
	}

	protected Iterable<Integer> extractUsedObjectIDTable() {
        IdFactoryRepository repository = DatabaseAccess.getRepository(IdFactoryRepository.class);
        repository.deleteItemsOnGroundDuplicated();
        return repository.findAllIds();
	}
	
	public abstract int getNextId();

	public abstract void releaseId(int id);
	
	public abstract int size();
}
