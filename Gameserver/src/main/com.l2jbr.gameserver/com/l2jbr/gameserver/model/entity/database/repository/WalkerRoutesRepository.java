package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.WalkerRouteNode;
import org.springframework.data.repository.CrudRepository;

public interface WalkerRoutesRepository extends CrudRepository<WalkerRouteNode, Integer> {
}
