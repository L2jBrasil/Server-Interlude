package com.l2jbr.gameserver.model.database.repository;

import com.l2jbr.gameserver.model.database.PetsStats;
import org.springframework.data.repository.CrudRepository;

public interface PetStatsRepository extends CrudRepository<PetsStats, Integer> {
}
