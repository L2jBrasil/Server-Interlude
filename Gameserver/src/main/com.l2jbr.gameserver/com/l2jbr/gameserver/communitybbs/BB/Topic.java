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
package com.l2jbr.gameserver.communitybbs.BB;

import com.l2jbr.commons.database.DatabaseAccess;
import com.l2jbr.gameserver.communitybbs.Manager.TopicBBSManager;
import com.l2jbr.gameserver.model.entity.database.repository.TopicRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Topic
{
	
	private static Logger _log = LoggerFactory.getLogger(Topic.class.getName());
	public static final int MORMAL = 0;
	public static final int MEMO = 1;
	
	private final int _id;
	private final int _forumId;
	private final String _topicName;
	private final long _date;
	private final String _ownerName;
	private final int _ownerId;
	private final int _type;
	private final int _cReply;
	
	/**
	 * @param ct
	 * @param id
	 * @param fid
	 * @param name
	 * @param date
	 * @param oname
	 * @param oid
	 * @param type
	 * @param Creply
	 */
	public Topic(ConstructorType ct, int id, int fid, String name, long date, String oname, int oid, int type, int Creply)
	{
		_id = id;
		_forumId = fid;
		_topicName = name;
		_date = date;
		_ownerName = oname;
		_ownerId = oid;
		_type = type;
		_cReply = Creply;
		TopicBBSManager.getInstance().addTopic(this);
		
		if (ct == ConstructorType.CREATE)
		{
			
			insertindb();
		}
	}

	public void insertindb() {
        com.l2jbr.gameserver.model.entity.database.Topic topic = new com.l2jbr.gameserver.model.entity.database.Topic(_id, _forumId, _topicName, _date,
            _ownerName, _ownerId, _type, _cReply);
        TopicRepository repository = DatabaseAccess.getRepository(TopicRepository.class);
        repository.save(topic);
	}
	
	public enum ConstructorType
	{
		RESTORE,
		CREATE
	}

	public int getID()
	{
		return _id;
	}
	
	public int getForumID()
	{
		return _forumId;
	}

	public String getName() {
		return _topicName;
	}
	
	public String getOwnerName() {
		return _ownerName;
	}

	public void deleteme(Forum f) {
		TopicBBSManager.getInstance().delTopic(this);
		f.rmTopicByID(getID());
        TopicRepository repository = DatabaseAccess.getRepository(TopicRepository.class);
        repository.deleteByForum(getID(), f.getID());
	}

	public long getDate()
	{
		return _date;
	}
}