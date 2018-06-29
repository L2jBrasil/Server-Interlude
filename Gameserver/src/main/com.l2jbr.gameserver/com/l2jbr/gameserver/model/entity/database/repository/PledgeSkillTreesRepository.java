package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.PledgeSkillTrees;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

public interface PledgeSkillTreesRepository extends CrudRepository<PledgeSkillTrees, Integer> {

    @Query("SELECT * FROM pledge_skill_trees ORDER BY skill_id, level")
    Iterable<PledgeSkillTrees> findAllOrderBySkillAndLevel();
}
