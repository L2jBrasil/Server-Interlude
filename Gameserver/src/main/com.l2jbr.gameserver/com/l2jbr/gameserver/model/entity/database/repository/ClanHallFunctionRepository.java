package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.ClanHallFunctions;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ClanHallFunctionRepository extends CrudRepository<ClanHallFunctions, Integer> {

    @Modifying
    @Query("UPDATE clanhall_functions SET lvl=:lvl, lease=:lease, endTime=:endTime WHERE hall_id=:hall AND type=:type")
    int updateByType(@Param("hall") int hallId, @Param("type") int type, @Param("lvl") int lvl, @Param("lease") int lease,
                     @Param("endTime") long endTime);

    @Query("SELECT * FROM clanhall_functions WHERE hall_id=:hall")
    Iterable<ClanHallFunctions> findAllByHall(@Param("hall") int hallId);

    @Modifying
    @Query("DELETE FROM clanhall_functions WHERE hall_id=:hall AND type=:type")
    int deleteByType(@Param("hall") int hallId, @Param("type") int type);
}
