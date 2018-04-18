package com.l2jbr.gameserver.model.database.repository;

import com.l2jbr.gameserver.model.database.Characters;
import org.springframework.data.repository.CrudRepository;

public interface CharacterRepository extends CrudRepository<Characters, Integer> {
}
