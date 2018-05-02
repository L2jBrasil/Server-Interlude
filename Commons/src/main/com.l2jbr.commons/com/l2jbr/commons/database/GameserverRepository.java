package com.l2jbr.commons.database;

import com.l2jbr.commons.database.model.GameServers;
import org.springframework.data.repository.CrudRepository;

public interface GameserverRepository extends CrudRepository<GameServers, Integer> {
}
