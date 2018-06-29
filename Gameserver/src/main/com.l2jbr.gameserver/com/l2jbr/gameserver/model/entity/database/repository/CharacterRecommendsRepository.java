package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.CharacterRecommends;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CharacterRecommendsRepository extends CrudRepository<CharacterRecommends, Integer> {

    @Query("SELECT * FROM character_recommends WHERE char_id=:char")
    Iterable<CharacterRecommends> findAllByCharacter(@Param("char") int characterObjectId);
}
