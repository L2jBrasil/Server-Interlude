package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.CharacterMacroses;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CharacterMacrosesRepository extends CrudRepository<CharacterMacroses, Integer> {

    @Modifying
    @Query("DELETE FROM character_macroses WHERE char_obj_id=:objectId AND id=:id")
    int deleteByMacroId(@Param("objectId") int objectId, @Param("id") int id);

    @Query("SELECT * FROM character_macroses WHERE char_obj_id=:objectId")
    Iterable<CharacterMacroses> findAllByCharacter(@Param("objectId") int objectId);
}
