package com.l2jbr.gameserver.model.database.repository;

import com.l2jbr.gameserver.model.database.NpcTemplate;
import org.springframework.data.repository.CrudRepository;

public interface NpcRepository extends CrudRepository<NpcTemplate, Integer> {
}
