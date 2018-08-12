package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.Hero;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface HeroesRepository extends CrudRepository<Hero, Integer> {

    @Modifying
    @Query("UPDATE heroes SET played=0")
    int updateAllResetPlayed();

    @Modifying
    @Query("UPDATE heroes SET count=:count, played=:played  WHERE char_id=:char")
    int updateCountPlayed(@Param("char") Integer charId, @Param("count") int count, @Param("played") int played);
}
