package com.l2jbr.gameserver.model.database.repository;

import com.l2jbr.gameserver.model.database.Locations;
import org.springframework.data.repository.CrudRepository;

public interface LocationRepository extends CrudRepository<Locations, Integer> {
}
