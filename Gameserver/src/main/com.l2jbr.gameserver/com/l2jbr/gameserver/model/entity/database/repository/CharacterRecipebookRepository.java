package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.CharacterRecipeBook;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CharacterRecipebookRepository extends CrudRepository<CharacterRecipeBook, Integer> {

    @Query("DELETE FROM character_recipebook WHERE char_id=:char")
    int deleteAllByCharacter(@Param("char") int charObjectId);

    @Query("SELECT * FROM character_recipebook WHERE char_id=:char")
    Iterable<CharacterRecipeBook> findAllByCharacter(@Param("char") int characterObjectId);
}
