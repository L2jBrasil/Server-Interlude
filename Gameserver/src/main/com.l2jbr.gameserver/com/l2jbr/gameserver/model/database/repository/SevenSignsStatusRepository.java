package com.l2jbr.gameserver.model.database.repository;

import com.l2jbr.gameserver.model.database.SevenSignsStatus;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface SevenSignsStatusRepository extends CrudRepository<SevenSignsStatus, Integer> {

    @Modifying
    @Query("UPDATE seven_signs_status SET date=:date WHERE id=:id")
    int updateDate(@Param("id") int id, @Param("date") int date);

    @Query("UPDATE seven_signs_status SET current_cycle=?, active_period=?, previous_winner=?, " +
               "dawn_stone_score=?, dawn_festival_score=?, dusk_stone_score=?, dusk_festival_score=?, " +
               "avarice_owner=?, gnosis_owner=?, strife_owner=?, avarice_dawn_score=?, gnosis_dawn_score=?, " +
               "strife_dawn_score=?, avarice_dusk_score=?, gnosis_dusk_score=?, strife_dusk_score=?, " +
               "festival_cycle=?, accumulated_bonus0=?, accumulated_bonus1=?, accumulated_bonus2=?, accumulated_bonus3=?, " +
               "accumulated_bonus4=?, date=? WHERE id=?")
    void update(@Param("id") int id, @Param("cycle") int currentCycle, @Param("period") int activePeriod, @Param("previousWinner") int previousWinner,
                @Param("dawnStone") double dawnStoneScore, @Param("dawnFestival") int dawnFestivalScore, @Param("duskStone") double duskStoneScore,
                @Param("duskFestival") int duskFestivalScore, @Param("avariceOwner") int avariceOwner, @Param("gnosisOwner") int gnosisOwner,
                @Param("strifeOwner") int strifeOwner, @Param("avariceDawn") int avariceDawnScore, @Param("gnosisDawn") int gnosisDawnScore,
                @Param("strifeDawn") int strifeDawnScore, @Param("avariceDusk") int avariceDuskScore, @Param("gnosisDusk") int gnosisDuskScore,
                @Param("strifeDusk") int strifeDuskScore, @Param("festivalCycle") int currentFestivalCycle, @Param("bonus0") int accumulatedBonus0,
                @Param("bonus1") int accumulatedBonus1, @Param("bonus2") int accumulatedBonus2, @Param("bonus3") int accumulatedBonus3,
                @Param("bonus4") int accumulatedBonus4, @Param("date") int date);
}
