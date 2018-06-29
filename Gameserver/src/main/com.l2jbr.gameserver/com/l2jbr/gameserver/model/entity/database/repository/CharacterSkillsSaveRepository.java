package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.CharacterSkillsSave;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CharacterSkillsSaveRepository extends CrudRepository<CharacterSkillsSave, Integer> {

    @Query("SELECT * FROM character_skills_save WHERE char_obj_id=:char AND class_index=:classIndex AND restore_type=:restoreType" +
               " ORDER BY buff_index ASC")
    Iterable<CharacterSkillsSave> findAllByClassIndexAndRestoreType(@Param("char") int charObjectId, @Param("classIndex") int classIndex,
                                                                    @Param("restoreType") int restoreType);

    @Modifying
    @Query("DELETE FROM character_skills_save WHERE char_obj_id=:char AND class_index=:classIndex")
    int deleteAllByClassIndex(@Param("char") int charObjectId, @Param("classIndex") int classIndex);
}
