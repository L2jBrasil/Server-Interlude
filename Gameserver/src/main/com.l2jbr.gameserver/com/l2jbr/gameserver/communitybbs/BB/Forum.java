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
import com.l2jbr.gameserver.communitybbs.Manager.ForumsBBSManager;
import com.l2jbr.gameserver.communitybbs.Manager.TopicBBSManager;
import com.l2jbr.gameserver.model.entity.database.Forums;
import com.l2jbr.gameserver.model.entity.database.repository.ForumRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class Forum {
    // type
    public static final int ROOT = 0;
    public static final int NORMAL = 1;
    public static final int CLAN = 2;
    public static final int MEMO = 3;
    public static final int MAIL = 4;
    // perm
    public static final int INVISIBLE = 0;
    public static final int ALL = 1;
    public static final int CLANMEMBERONLY = 2;
    public static final int OWNERONLY = 3;

    private static Logger _log = LoggerFactory.getLogger(Forum.class.getName());
    private final List<Forum> _children;
    private final Map<Integer, Topic> _topic;
    private final int _forumId;
    private String _forumName;
    // private int _ForumParent;
    private int _forumType;
    private int _forumPost;
    private int _forumPerm;
    private final Forum _fParent;
    private int _ownerID;
    private boolean _loaded = false;

    /**
     * @param Forumid
     * @param FParent
     */
    public Forum(int Forumid, Forum FParent) {
        _forumId = Forumid;
        _fParent = FParent;
        _children = new LinkedList<>();
        _topic = new LinkedHashMap<>();

        /*
         * load(); getChildren();
         */
        ForumsBBSManager.getInstance().addForum(this);

    }

    /**
     * @param name
     * @param parent
     * @param type
     * @param perm
     * @param OwnerID
     */
    public Forum(String name, Forum parent, int type, int perm, int OwnerID) {
        _forumName = name;
        _forumId = ForumsBBSManager.getInstance().getANewID();
        // _ForumParent = parent.getID();
        _forumType = type;
        _forumPost = 0;
        _forumPerm = perm;
        _fParent = parent;
        _ownerID = OwnerID;
        _children = new LinkedList<>();
        _topic = new LinkedHashMap<>();
        parent._children.add(this);
        ForumsBBSManager.getInstance().addForum(this);
        _loaded = true;
    }

    private void load() {
        ForumRepository repository = DatabaseAccess.getRepository(ForumRepository.class);
        repository.findById(_forumId).ifPresent(forum -> {
            _forumName = forum.getForumName();
            _forumPost = forum.getForumPost();
            _forumType = forum.getForumType();
            _forumPerm = forum.getForumPerm();
            _ownerID = forum.getForumOwnerId();
            forum.getTopics().forEach(topic -> {
                Topic t = new Topic(Topic.ConstructorType.RESTORE, topic.getId(), topic.getTopicForumId(), topic.getTopicName(),
                    topic.getTopicDate(), topic.getTopicOwnerName(), topic.getTopicOwnerId(), topic.getTopicType(), topic.getTopicReply());
                _topic.put(t.getID(), t);
                if (t.getID() > TopicBBSManager.getInstance().getMaxID(this)) {
                    TopicBBSManager.getInstance().setMaxID(t.getID(), this);
                }
            });
        });
    }

    private void getChildren() {
        ForumRepository repository = DatabaseAccess.getRepository(ForumRepository.class);
        repository.findAllIdsByParent(_forumId).forEach(forumId -> {
            _children.add(new Forum(forumId, this));
        });
    }

    public int getTopicSize() {
        if (_loaded == false) {
            load();
            getChildren();
            _loaded = true;
        }
        return _topic.size();
    }

    public Topic gettopic(int j) {
        if (_loaded == false) {
            load();
            getChildren();
            _loaded = true;
        }
        return _topic.get(j);
    }

    public void addtopic(Topic t) {
        if (_loaded == false) {
            load();
            getChildren();
            _loaded = true;
        }
        _topic.put(t.getID(), t);
    }

    /**
     * @return
     */
    public int getID() {
        return _forumId;
    }

    public String getName() {
        if (_loaded == false) {
            load();
            getChildren();
            _loaded = true;
        }
        return _forumName;
    }

    public int getType() {
        if (_loaded == false) {
            load();
            getChildren();
            _loaded = true;
        }
        return _forumType;
    }

    /**
     * @param name
     * @return
     */
    public Forum getChildByName(String name) {
        if (_loaded == false) {
            load();
            getChildren();
            _loaded = true;
        }
        for (Forum f : _children) {
            if (f.getName().equals(name)) {
                return f;
            }
        }
        return null;
    }


    public void rmTopicByID(int id) {
        _topic.remove(id);
    }

    public void insertInDb() {
        Forums forum = new Forums(_forumId, _forumName, _fParent.getID(), _forumPost, _forumType, _forumPerm, _ownerID);
        ForumRepository repository = DatabaseAccess.getRepository(ForumRepository.class);
        repository.save(forum);
    }

    public void vload() {
        if (_loaded == false) {
            load();
            getChildren();
            _loaded = true;
        }
    }

}