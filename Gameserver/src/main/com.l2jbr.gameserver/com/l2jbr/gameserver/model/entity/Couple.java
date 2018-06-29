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
package com.l2jbr.gameserver.model.entity;

import com.l2jbr.commons.database.DatabaseAccess;
import com.l2jbr.commons.database.L2DatabaseFactory;
import com.l2jbr.gameserver.idfactory.IdFactory;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.model.entity.database.ModsWedding;
import com.l2jbr.gameserver.model.entity.database.repository.ModsWeddingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;


/**
 * @author evill33t
 */
public class Couple
{
	private static final Logger _log = LoggerFactory.getLogger(Couple.class.getName());

	private int _Id = 0;
	private int _player1Id = 0;
	private int _player2Id = 0;
	private boolean _maried = false;
	private Calendar _affiancedDate;
	private Calendar _weddingDate;
	
	public Couple(L2PcInstance player1, L2PcInstance player2)
	{
		int _tempPlayer1Id = player1.getObjectId();
		int _tempPlayer2Id = player2.getObjectId();
		
		_player1Id = _tempPlayer1Id;
		_player2Id = _tempPlayer2Id;
		
		_affiancedDate = Calendar.getInstance();
		_affiancedDate.setTimeInMillis(Calendar.getInstance().getTimeInMillis());
		
		_weddingDate = Calendar.getInstance();
		_weddingDate.setTimeInMillis(Calendar.getInstance().getTimeInMillis());

        _Id = IdFactory.getInstance().getNextId();
        ModsWedding wedding = new ModsWedding(_Id, _player1Id, _player2Id, false, _affiancedDate.getTimeInMillis(), _weddingDate.getTimeInMillis());
        ModsWeddingRepository repository = DatabaseAccess.getRepository(ModsWeddingRepository.class);
        repository.save(wedding);
	}

	public Couple(ModsWedding wedding) {
        _player1Id = wedding.getPlayer1Id();
        _player2Id = wedding.getPlayer2Id();
        _maried = Boolean.valueOf(wedding.getMarried());

        _affiancedDate = Calendar.getInstance();
        _affiancedDate.setTimeInMillis(wedding.getAffianceDate());

        _weddingDate = Calendar.getInstance();
        _weddingDate.setTimeInMillis(wedding.getWeddingDate());
	}

	public void marry() {
        _weddingDate = Calendar.getInstance();
        ModsWeddingRepository repository = DatabaseAccess.getRepository(ModsWeddingRepository.class);
        repository.updateMarried(_Id, true, _weddingDate.getTimeInMillis());
        _maried = true;
	}
	
	public void divorce()  {
	    ModsWeddingRepository repository = DatabaseAccess.getRepository(ModsWeddingRepository.class);
	    repository.deleteById(_Id);
	}
	
	public final int getId()
	{
		return _Id;
	}
	
	public final int getPlayer1Id()
	{
		return _player1Id;
	}
	
	public final int getPlayer2Id()
	{
		return _player2Id;
	}
	
	public final boolean getMaried()
	{
		return _maried;
	}
	
	public final Calendar getAffiancedDate()
	{
		return _affiancedDate;
	}
	
	public final Calendar getWeddingDate()
	{
		return _weddingDate;
	}
}
