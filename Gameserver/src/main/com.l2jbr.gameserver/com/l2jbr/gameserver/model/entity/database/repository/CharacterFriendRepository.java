package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.CharacterFriends;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CharacterFriendRepository extends CrudRepository<CharacterFriends, Integer> {

    @Modifying
    @Query("DELETE FROM character_friends WHERE char_id=:objectId OR friend_id=:objectId")
    int deleteFriends(@Param("objectId") int objectId);

    @Query("SELECT * FROM character_friends WHERE char_id=:objectId")
    List<CharacterFriends> findAllByCharacterId(@Param("objectId") int objectId);

    @Query("SELECT EXISTS(SELECT 1 FROM character_friends WHERE (char_id=:character AND friend_id=:friend))")
    boolean existsFriends(@Param("character") int objectId, @Param("friend") int friendId);

    @Modifying
    @Query("DELETE FROM character_friends WHERE (char_id=:objectId AND friend_Id=:friend) OR (char_id=:friend AND friend_Id=:objectId)")
    int deleteFriendship(@Param("objectId") int objectId, @Param("friend") int friendId);
}
