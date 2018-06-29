package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.database.model.Entity;
import org.springframework.data.annotation.Id;

@Table("posts")
public class Posts extends Entity<Integer> {
    @Id
    @Column("post_id")
    private int postId;
    @Column("post_owner_name")
    private String postOwnerName;
    @Column("post_ownerid")
    private int postOwnerid;
    @Column("post_date")
    private long postDate;
    @Column("post_topic_id")
    private int postTopicId;
    @Column("post_forum_id")
    private int postForumId;
    @Column("post_txt")
    private String postTxt;

    public Posts() {}

    public Posts(int postId, String postOwner, int postOwnerId, long postDate, int postTopicId, int postForumId, String postTxt) {
        this.postId = postId;
        this.postOwnerName = postOwner;
        this.postOwnerid = postOwnerId;
        this.postDate = postDate;
        this.postTopicId = postTopicId;
        this.postForumId = postForumId;
        this.postTxt = postTxt;

    }

    @Override
    public Integer getId() {
        return postId;
    }
}
