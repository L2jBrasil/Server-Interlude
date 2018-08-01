package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.Spawn;
import org.springframework.data.repository.CrudRepository;

public interface SpawnListRepository extends CrudRepository<Spawn, Integer> {
}
