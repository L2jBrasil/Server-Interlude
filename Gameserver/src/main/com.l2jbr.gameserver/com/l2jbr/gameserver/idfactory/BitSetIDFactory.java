/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jbr.gameserver.idfactory;

import com.l2jbr.gameserver.util.PrimeFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.BitSet;
import java.util.concurrent.atomic.AtomicInteger;

public class BitSetIDFactory extends IdFactory {
	private static final int INITIAL_CAPACITY = 100000;
	private static Logger _log = LoggerFactory.getLogger(BitSetIDFactory.class.getName());
	
	private BitSet _freeIds;
	private AtomicInteger _freeIdCount;
	private AtomicInteger _nextFreeId;

	protected BitSetIDFactory() {
		initialize();
		_log.info("IDFactory: {} id's available.", _freeIds.size());
	}
	
	private void initialize() {
		try {
			_freeIds = new BitSet(PrimeFinder.nextPrime(INITIAL_CAPACITY));
			_freeIds.clear();
			_freeIdCount = new AtomicInteger(FREE_OBJECT_ID_SIZE);
			markUsedIds();
			_nextFreeId = new AtomicInteger(_freeIds.nextClearBit(0));
		}
		catch (Exception e) {
			_log.error("BitSet ID Factory could not be initialized correctly");
			_log.error(e.getLocalizedMessage(), e);
			throw e;
		}
	}

	private void markUsedIds() {
		for (int usedObjectId : extractUsedObjectIDTable()) {
			int objectID = usedObjectId - FIRST_OID;
			if (objectID < 0) {
				_log.warn("Object ID {} in DB is less than minimum ID of {}", usedObjectId, FIRST_OID);
				continue;
			}
			_freeIds.set(objectID);
			_freeIdCount.decrementAndGet();
		}
	}

	@Override
	public synchronized void releaseId(int objectID) {
		if ((objectID - FIRST_OID) > -1) {
			_freeIds.clear(objectID - FIRST_OID);
			_freeIdCount.incrementAndGet();
		} else {
			_log.warn("BitSet ID Factory: release objectID {} failed (< {} )", objectID, FIRST_OID);
		}
	}
	
	@Override
	public synchronized int getNextId() {
		int newID = _nextFreeId.get();
		_freeIds.set(newID);
		_freeIdCount.decrementAndGet();
		
		int nextFree = _freeIds.nextClearBit(newID);
		
		if (nextFree < 0) {
			nextFree = _freeIds.nextClearBit(0);
		}
		if (nextFree < 0) {
			if (_freeIds.size() < FREE_OBJECT_ID_SIZE) {
				increaseBitSetCapacity();
			} else {
				throw new NullPointerException("Ran out of valid Id's.");
			}
		}
		_nextFreeId.set(nextFree);
		return newID + FIRST_OID;
	}
	
	@Override
	public synchronized int size()
	{
		return _freeIdCount.get();
	}

	private synchronized void increaseBitSetCapacity() {
		BitSet newBitSet = new BitSet(PrimeFinder.nextPrime((usedIdCount() * 11) / 10));
		newBitSet.or(_freeIds);
		_freeIds = newBitSet;
	}

    private int usedIdCount() {
        return (size() - FIRST_OID);
    }
}
