package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.Games;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GameRepository extends CrudRepository<Games, Integer> {

    @Modifying
    @Query("UPDATE games SET prize=:prize, newprize=:prize WHERE id=:id AND idnr=:idnr")
    int updatePrizeByIdNr(@Param("id") int id, @Param("idnr") int idnr, @Param("prize") int prize);

    @Modifying
    @Query("UPDATE games SET finished=1, prize=:prize, newprize=:newPrize, number1=:number1, number2=:number2, prize1=:prize1, " +
               "prize2=:prize2, prize3=:prize3 WHERE id=:id AND idnr=:idnr")
    int updateByIdNr(@Param("id") int id, @Param("idnr") int idnr, @Param("prize") int prize, @Param("newPrize") int newprize,
                     @Param("number1") int number1, @Param("number2") int number2, @Param("prize1") int prize1, @Param("prize2") int prize2,
                     @Param("prize3") int prize3);

    @Query("SELECT * FROM games WHERE id=1 ORDER BY idnr DESC LIMIT 1")
    Optional<Games> findLastIdnrById(@Param("id") int id);
}
