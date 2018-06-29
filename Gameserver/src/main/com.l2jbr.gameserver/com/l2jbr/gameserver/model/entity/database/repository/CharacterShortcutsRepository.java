package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.CharacterShortcuts;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CharacterShortcutsRepository extends CrudRepository<CharacterShortcuts, Integer> {

    @Modifying
    @Query("DELETE FROM character_shortcuts WHERE char_obj_id=:char AND class_index=:classIndex")
    int deleteAllByClassIndex(@Param("char") int characterObjectId, @Param("classIndex") int classIndex);

    @Modifying
    @Query("REPLACE INTO character_shortcuts (char_obj_id,slot,page,type,shortcut_id,level,class_index) " +
               "values(:char,:slot,:page,:type,:shortcut,:level,:classIndex)")
    int saveOrUpdate(@Param("char") int characterObjectId, @Param("shortcut") int shortcutId, @Param("slot") int slot,
                     @Param("page") int page, @Param("type") int type, @Param("level") int level, @Param("classIndex") int classIndex);

    @Modifying
    @Query("DELETE FROM character_shortcuts WHERE char_obj_id=:char AND slot=:slot AND page=:page AND class_index=:classIndex")
    int delete(@Param("char") int characterObjectId, @Param("slot") int slot, @Param("page") int page, @Param("classIndex") int classIndex);

    @Query("SELECT * FROM character_shortcuts WHERE char_obj_id=:char AND class_index=:classIndex")
    Iterable<CharacterShortcuts> finAllByClassIndex(@Param("char") int charObjectId, @Param("classIndex") int classIndex);
}
