package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.Topic;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TopicRepository extends CrudRepository<Topic, Integer> {

    @Query("DELETE FROM topic WHERE topic_id=:topic AND topic_forum_id=:forum")
    int deleteByForum(@Param("topic") int topicId, @Param("forum") int forumId);
}
