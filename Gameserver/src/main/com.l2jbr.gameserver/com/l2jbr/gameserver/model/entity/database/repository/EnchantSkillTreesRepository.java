package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.EnchantSkillInfo;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EnchantSkillTreesRepository extends CrudRepository<EnchantSkillInfo, Integer> {

    @Query("SELECT * FROM enchant_skill_trees ORDER BY skill_id, level")
    List<EnchantSkillInfo> findAllOrderBySkillAndLevel();
}
