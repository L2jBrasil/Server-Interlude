package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.Forums;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ForumRepository extends CrudRepository<Forums, Integer> {

    @Query("SELECT forum_id FROM forums WHERE forum_type=:type")
    Iterable<Integer> findAllIdsByType(@Param("type") int type);

    @Query("SELECT forum_id FROM forums WHERE forum_parent=:parent")
    Iterable<Integer> findAllIdsByParent(@Param("parent") int parentId);
}
