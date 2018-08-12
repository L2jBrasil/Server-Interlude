package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.Nobles;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OlympiadNoblesRepository extends CrudRepository<Nobles, Integer> {

    @Modifying
    @Query("UPDATE olympiad_nobles SET olympiad_points=:points, competitions_done=:competitions WHERE char_id=:char")
    int updateCompetitions(@Param("char") int charId, @Param("points") int points, @Param("competitions") int competitions);

    @Query("SELECT char_name FROM olympiad_nobles WHERE class_id=:class ORDER BY olympiad_points DESC, competitions_done DESC")
    Iterable<Nobles> findAllNoblesByClassId(@Param("class") int classId);

    @Query("SELECT * FROM olympiad_nobles WHERE class_id=:class AND competitions_done >=9 ORDER BY olympiad_points DESC, competitions_done DESC LIMIT 1")
    Optional<Nobles> findWinnerByClassId(@Param("class") int classId);
}
