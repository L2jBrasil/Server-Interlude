package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.Posts;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends CrudRepository<Posts, Integer> {

    @Modifying
    @Query("DELETE FROM posts WHERE post_forum_id=:forum AND post_topic_id=:topic")
    int deleteByForumAndTopic(@Param("forum") int forumId, @Param("topic") int topicId);

    @Query("SELECT * FROM posts WHERE post_forum_id=:forum AND post_topic_id=:post ORDER BY post_id ASC")
    int findAllByForumAndTopic(@Param("forum") int forumId, @Param("topic") int topicId);

    @Modifying
    @Query("UPDATE posts SET post_txt=:text WHERE post_id=:pot AND post_topic_id=:topic AND post_forum_id=:forum")
    void updateText(@Param("post") int postId, @Param("topic") int topicId, @Param("forum") int forumId, @Param("text") String postTxt);
}
