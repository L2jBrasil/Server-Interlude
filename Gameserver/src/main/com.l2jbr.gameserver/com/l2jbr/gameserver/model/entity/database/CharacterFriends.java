package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.database.model.Entity;
import org.springframework.data.annotation.Id;

@Table("character_friends")
public class CharacterFriends extends Entity<Integer> {

    @Id
    @Column("char_id")
    private int charId;
    @Column("friend_id")
    private int friendId;
    @Column("friend_name")
    private String friendName;

    public CharacterFriends() {}

    public CharacterFriends(int objectId, int friendId, String name) {
        this.charId = objectId;
        this.friendId = friendId;
        this.friendName = name;
    }

    @Override
    public Integer getId() {
        return charId;
    }

    public int getFriendId() {
        return friendId;
    }

    public String getFriendName() {
        return friendName;
    }
}
