package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.ClanSkillInfo;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PledgeSkillTreesRepository extends CrudRepository<ClanSkillInfo, Integer> {

    @Query("SELECT * FROM pledge_skill_trees ORDER BY skill_id, level")
    List<ClanSkillInfo> findAllOrderBySkillAndLevel();
}
