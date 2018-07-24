package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.Skill;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface SkillTreeRepository extends CrudRepository<Skill, Integer> {

    @Query("SELECT * FROM skill_trees where class_id=:class ORDER BY skill_id, level")
    Iterable<Skill> findAllByClassOrderBySkill(@Param("class") int classId);
}
