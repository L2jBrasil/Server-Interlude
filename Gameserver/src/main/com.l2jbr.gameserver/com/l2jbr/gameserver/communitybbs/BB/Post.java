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
import com.l2jbr.gameserver.communitybbs.Manager.PostBBSManager;
import com.l2jbr.gameserver.model.entity.database.Posts;
import com.l2jbr.gameserver.model.entity.database.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;


/**
 * @author Maktakien
 */
public class Post {
    private static Logger _log = LoggerFactory.getLogger(Post.class.getName());

    public class CPost {
        public int postId;
        public String postOwner;
        public int postOwnerId;
        public long postDate;
        public int postTopicId;
        public int postForumId;
        public String postTxt;
    }

    private final List<CPost> _post;

    /**
     * @param _PostOwner
     * @param _PostOwnerID
     * @param date
     * @param tid
     * @param _PostForumID
     * @param txt
     */
    public Post(String _PostOwner, int _PostOwnerID, long date, int tid, int _PostForumID, String txt) {
        _post = new LinkedList<>();
        CPost cp = new CPost();
        cp.postId = 0;
        cp.postOwner = _PostOwner;
        cp.postOwnerId = _PostOwnerID;
        cp.postDate = date;
        cp.postTopicId = tid;
        cp.postForumId = _PostForumID;
        cp.postTxt = txt;
        _post.add(cp);
        insertindb(cp);

    }

    public void insertindb(CPost cp) {
        Posts post = new Posts(cp.postId, cp.postOwner, cp.postOwnerId, cp.postDate, cp.postTopicId, cp.postForumId, cp.postTxt);
        PostRepository repository = DatabaseAccess.getRepository(PostRepository.class);
        repository.save(post);
    }

    public Post(Topic t) {
        _post = new LinkedList<>();
        load(t);
    }

    public CPost getCPost(int id) {
        int i = 0;
        for (CPost cp : _post) {
            if (i == id) {
                return cp;
            }
            i++;
        }
        return null;
    }

    public void deleteme(Topic t) {
        PostBBSManager.getInstance().delPostByTopic(t);
        PostRepository repository = DatabaseAccess.getRepository(PostRepository.class);
        repository.deleteByForumAndTopic(t.getForumID(), t.getID());
    }

    private void load(Topic t) {
        PostRepository repository = DatabaseAccess.getRepository(PostRepository.class);
        repository.findAllByForumAndTopic(t.getForumID(), t.getID());
    }

    public void updatetxt(int i) {
        PostRepository repository = DatabaseAccess.getRepository(PostRepository.class);
        CPost cp = getCPost(i);
        repository.updateText(cp.postId, cp.postTopicId, cp.postForumId, cp.postTxt);
    }
}
