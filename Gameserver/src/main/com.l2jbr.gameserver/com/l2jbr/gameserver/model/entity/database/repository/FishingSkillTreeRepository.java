package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.FishingSkillTrees;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

public interface FishingSkillTreeRepository extends CrudRepository<FishingSkillTrees, Integer> {

    @Query("SELECT * FROM fishing_skill_trees ORDER BY skill_id, level")
    Iterable<FishingSkillTrees> findAllOrderBySkillAndLevel();
}
