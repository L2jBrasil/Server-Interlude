package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.HelperBuff;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HelperBuffRepository extends CrudRepository<HelperBuff, Integer> {

    @Override
    List<HelperBuff> findAll();
}
