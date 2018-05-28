package com.l2jbr.gameserver.model.database.repository;

import com.l2jbr.gameserver.model.database.PlayerTemplate;
import org.springframework.data.repository.CrudRepository;

public interface CharTemplateRepository extends CrudRepository<PlayerTemplate, Integer> {
}
