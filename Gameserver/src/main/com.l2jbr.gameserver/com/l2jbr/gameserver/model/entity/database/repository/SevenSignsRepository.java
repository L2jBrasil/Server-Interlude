package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.SevenSignsPlayer;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface SevenSignsRepository extends CrudRepository<SevenSignsPlayer, Integer> {

    @Modifying
    @Query("UPDATE seven_signs SET cabal=:cabal, seal=:seal, red_stones=:red, green_stones=:green, blue_stones=:blue, " +
               "ancient_adena_amount=:ancientAdena, contribution_score=:score WHERE char_obj_id=:char")
    int updateContribution(@Param("char") int charObjId, @Param("cabal") String cabal, @Param("seal") int seal, @Param("red") int redStones,
                           @Param("green") int greenStones, @Param("blue") int blueStones, @Param("ancientAdena") double ancientAdenaAmount,
                           @Param("score") double contributionScore);
}
