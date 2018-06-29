package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.DropList;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DropListRepository extends CrudRepository<DropList, Integer> {

    @Query("SELECT * FROM droplist WHERE mobId=:npc AND itemId=:item AND category=:category")
    Optional<DropList> findByNpcItemAndCategory(@Param("npc") int npcId, @Param("item") int itemId, @Param("category") int category);

    @Modifying
    @Query("UPDATE droplist SET min=:min, max=:max, chance=:chance WHERE mobId=:npc AND itemId=:item AND category=:category")
    int updateDrop(@Param("npc") int npcId, @Param("item") int itemId, @Param("category") int category, @Param("min") int min,
                   @Param("max") int max, @Param("chance") int chance);

    @Query("DELETE FROM droplist WHERE mobId=:npc AND itemId=:item AND category=:category")
    int deleteByNpcItemAndCategory(@Param("npc") int npcId, @Param("item") int itemId, @Param("category") int category);

    @Query("SELECT * FROM droplist WHERE mobId=:npc")
    Iterable<DropList> findAllByNpc(@Param("npc") int npcId);
}
