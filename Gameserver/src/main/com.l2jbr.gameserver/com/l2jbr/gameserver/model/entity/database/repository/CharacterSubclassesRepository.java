package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.CharacterSubclasses;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CharacterSubclassesRepository extends CrudRepository<CharacterSubclasses, Integer> {

    @Query("SELECT * FROM character_subclasses WHERE char_obj_id=:char AND class_id=:classId")
    Optional<CharacterSubclasses> findByClassId(@Param("char") int charObjectId, @Param("classId") int classId);

    @Query("SELECT * FROM character_subclasses WHERE char_obj_id=:char ORDER BY class_index ASC")
    Iterable<CharacterSubclasses> findAllByCharacter(@Param("char") int charObjectId);

    @Modifying
    @Query("UPDATE character_subclasses SET exp=:exp,sp=:sp,level=:level,class_id=:classId WHERE char_obj_id=:char AND class_index=:classIndex")
    int updateByClassIndex(@Param("char") int charObjectId, @Param("classIndex") int classIndex, @Param("exp") long exp,
                           @Param("sp") int sp, @Param("level") byte level, @Param("classId") int classId);

    @Modifying
    @Query("DELETE FROM character_subclasses WHERE char_obj_id=:char AND class_index=:classIndex")
    int deleteByClassIndex(@Param("char") int charObjectId, @Param("classIndex") int classIndex);
}
