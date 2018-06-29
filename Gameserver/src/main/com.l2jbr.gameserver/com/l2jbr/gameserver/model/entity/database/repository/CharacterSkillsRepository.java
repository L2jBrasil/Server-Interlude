package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.CharacterSkills;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CharacterSkillsRepository extends CrudRepository<CharacterSkills, Integer> {

    @Query("SELECT * FROM character_skills WHERE char_obj_id=:char AND class_index=:classIndex")
    Iterable<CharacterSkills> findAllByClassIndex(@Param("char") int charObjectId, @Param("classIndex") int classIndex);

    @Modifying
    @Query("UPDATE character_skills SET skill_level=:level WHERE skill_id=:skill AND char_obj_id=:char AND class_index=:classIndex")
    int updateSkillLevelByClassIndex(@Param("char") int charObjectId, @Param("skill") int skillId, @Param("level") int level,
                                     @Param("classIndex") int classIndex);

    @Modifying
    @Query("DELETE FROM character_skills WHERE skill_id=:skill AND char_obj_id=:char AND class_index=:classIndex")
    int deleteSkillByClassIndex(@Param("char") int charObjectId, @Param("skill") int skillId, @Param("classIndex") int classIndex);

    @Modifying
    @Query("DELETE FROM character_skills WHERE char_obj_id=:char AND class_index=:classIndex")
    int deleteAllByClassIndex(@Param("char") int charObjectId, @Param("classIndex") int classIndex);

}
