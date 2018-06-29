package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.EnchantSkillTrees;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

public interface EnchantSkillTreesRepository extends CrudRepository<EnchantSkillTrees, Integer> {

    @Query("SELECT * FROM enchant_skill_trees ORDER BY skill_id, level")
    Iterable<EnchantSkillTrees> findAllOrderBySkillAndLevel();
}
