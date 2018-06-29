package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.PetsStats;
import org.springframework.data.repository.CrudRepository;

public interface PetStatsRepository extends CrudRepository<PetsStats, Integer> {
}
