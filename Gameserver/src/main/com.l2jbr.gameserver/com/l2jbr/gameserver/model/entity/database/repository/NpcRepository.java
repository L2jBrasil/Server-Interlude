package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.NpcTemplate;
import org.springframework.data.repository.CrudRepository;

public interface NpcRepository extends CrudRepository<NpcTemplate, Integer> {
}
