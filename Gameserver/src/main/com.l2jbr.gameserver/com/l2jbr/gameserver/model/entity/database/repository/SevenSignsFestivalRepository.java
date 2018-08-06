package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.SevenSignsFestivalData;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface SevenSignsFestivalRepository extends CrudRepository<SevenSignsFestivalData, Integer> {

    @Modifying
    @Query("UPDATE seven_signs_festival SET date=:date, score=:score, members=:members WHERE cycle=:cycle AND cabal=:cabal " +
               "AND festivalId=:festival")
    int updateByCycleAndCabal(@Param("festival") int festivalId, @Param("cycle") int cycle, @Param("cabal") String cabal,
                              @Param("date") long date, @Param("score") int score, @Param("members") String members);
}