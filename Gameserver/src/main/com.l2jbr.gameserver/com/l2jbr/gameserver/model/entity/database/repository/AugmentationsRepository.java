package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.Augmentation;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AugmentationsRepository extends CrudRepository<Augmentation, Integer> {

    @Modifying
    @Query("DELETE FROM augmentations WHERE item_id IN (SELECT object_id FROM items i WHERE i.owner_id=:owner)")
    int deleteByItemOwner(@Param("owner") int itemOwnerId);
}
