package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.SevenSignsStatus;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface SevenSignsStatusRepository extends CrudRepository<SevenSignsStatus, Integer> {

    @Modifying
    @Query("UPDATE seven_signs_status SET date=:date WHERE id=:id")
    int updateDate(@Param("id") int id, @Param("date") int date);

    @Modifying
    @Query("UPDATE seven_signs_status SET current_cycle=:cycle, active_period=:period, previous_winner=:previousWinner, " +
               "dawn_stone_score=:dawnStone, dawn_festival_score=:dawnFestival, dusk_stone_score=:duskStone, dusk_festival_score=:duskFestival, " +
               "avarice_owner=:avariceOwner, gnosis_owner=:gnosisOwner, strife_owner=:strifeOwner, avarice_dawn_score=:avariceDawn, gnosis_dawn_score=:gnosisDawn, " +
               "strife_dawn_score=:strifeDawn, avarice_dusk_score=:avariceDusk, gnosis_dusk_score=:gnosisDusk, strife_dusk_score=:strifeDusk, " +
               "festival_cycle=:festivalCycle, accumulated_bonus0=:bonus0, accumulated_bonus1=:bonus1, accumulated_bonus2=:bonus2, accumulated_bonus3=:bonus3, " +
               "accumulated_bonus4=:bonus4, date=:date WHERE id=:id")
    void update(@Param("id") int id, @Param("cycle") int currentCycle, @Param("period") int activePeriod, @Param("previousWinner") int previousWinner,
                @Param("dawnStone") double dawnStoneScore, @Param("dawnFestival") int dawnFestivalScore, @Param("duskStone") double duskStoneScore,
                @Param("duskFestival") int duskFestivalScore, @Param("avariceOwner") int avariceOwner, @Param("gnosisOwner") int gnosisOwner,
                @Param("strifeOwner") int strifeOwner, @Param("avariceDawn") int avariceDawnScore, @Param("gnosisDawn") int gnosisDawnScore,
                @Param("strifeDawn") int strifeDawnScore, @Param("avariceDusk") int avariceDuskScore, @Param("gnosisDusk") int gnosisDuskScore,
                @Param("strifeDusk") int strifeDuskScore, @Param("festivalCycle") int currentFestivalCycle, @Param("bonus0") int accumulatedBonus0,
                @Param("bonus1") int accumulatedBonus1, @Param("bonus2") int accumulatedBonus2, @Param("bonus3") int accumulatedBonus3,
                @Param("bonus4") int accumulatedBonus4, @Param("date") int date);
}
