package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.Wedding;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ModsWeddingRepository extends CrudRepository<Wedding, Integer> {

    @Modifying
    @Query("UPDATE mods_wedding SET married=:married, weddingDate=:weddingDate WHERE id=:id")
    int updateMarried(@Param("id") int id, @Param("married") boolean married, @Param("weddingDate") long weddingDate);
}
