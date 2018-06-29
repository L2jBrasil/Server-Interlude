package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.QuestGlobalData;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QuestGlobalDataRepository extends CrudRepository<QuestGlobalData, String> {

    @Modifying
    @Query("REPLACE INTO quest_global_data VALUES (:quest,:var,:value)")
    void saveOrUpdate(@Param("quest") String questName, @Param("var") String var, @Param("value") String value);

    @Query("SELECT value FROM quest_global_data WHERE quest_name=:quest AND var=:var")
    Optional<String> findValueByVar(@Param("quest") String name, @Param("var") String var);

    @Query("DELETE FROM quest_global_data WHERE quest_name=:quest AND var=:var")
    int deleteByVar(@Param("quest") String questName, @Param("var") String var);
}
