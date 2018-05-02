package com.l2jbr.gameserver.model.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.model.Entity;
import org.springframework.data.annotation.Id;

public class Forums extends Entity<Integer> {
    @Id
    @Column("forum_id")
    private int forumId;
    @Column("forum_name")
    private String forumName;
    @Column("forum_parent")
    private int forumParent;
    @Column("forum_post")
    private int forumPost;
    @Column("forum_type")
    private int forumType;
    @Column("forum_perm")
    private int forumPerm;
    @Column("forum_owner_id")
    private int forumOwnerId;

    public Forums() {}

    public Forums(int forumId, String forumName, int parentId, int forumPost, int forumType, int forumPerm, int ownerId) {
        this.forumId = forumId;
        this.forumName = forumName;
        this.forumParent = parentId;
        this.forumPost = forumPost;
        this.forumType = forumType;
        this.forumPerm = forumPerm;
        this.forumOwnerId = ownerId;
    }

    @Override
    public Integer getId() {
        return forumId;
    }

    public String getForumName() {
        return forumName;
    }

    public int getForumParent() {
        return forumParent;
    }

    public int getForumPost() {
        return forumPost;
    }

    public int getForumType() {
        return forumType;
    }

    public int getForumPerm() {
        return forumPerm;
    }

    public int getForumOwnerId() {
        return forumOwnerId;
    }
}
