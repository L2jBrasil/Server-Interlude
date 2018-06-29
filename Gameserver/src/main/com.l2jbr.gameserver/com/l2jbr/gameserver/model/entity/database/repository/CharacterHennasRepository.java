package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.CharacterHennas;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CharacterHennasRepository extends CrudRepository<CharacterHennas, Integer> {

    @Query("SELECT slot,symbol_id FROM character_hennas WHERE char_obj_id=:objectId AND class_index=:classIndex")
    Iterable<CharacterHennas> findAllByClassIndex(@Param("objectId") int objectId, @Param("classIndex") int classIndex);

    @Modifying
    @Query("DELETE FROM character_hennas WHERE char_obj_id=:objectId AND class_index=:classIndex")
    int deleteAllByClassIndex(@Param("objectId") int objectId, @Param("classIndex") int classIndex);

    @Modifying
    @Query("DELETE FROM character_hennas WHERE char_obj_id=:objectId AND slot=:slot AND class_index=:classIndex")
    void deleteByClassindexAndSlot(int objectId, int classIndex, int i);
}
