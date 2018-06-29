package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.Pets;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PetsRepository extends CrudRepository<Pets, Integer> {

    @Modifying
    @Query("UPDATE pets SET item_obj_id=:newObjectId WHERE item_obj_id=:objectId")
    int updateId(@Param("objectId") int oldObjectId, @Param("newObjectId") int newObjectId);

    @Modifying
    @Query("UPDATE pets SET name=:name,level=:level,curHp=:hp,curMp=:mp,exp=:exp,sp=:sp,karma=:karma,pkkills=:pk,fed=:fed WHERE item_obj_id=:objectId")
    int updateById(@Param("objectId") int objectId, @Param("name") String name, @Param("level") byte level, @Param("hp") double hp,
                   @Param("mp") double mp, @Param("exp") long exp, @Param("sp") int sp, @Param("karma") int karma, @Param("pk") int pkKills,
                   @Param("fed") int fed);

    @Query("SELECT EXISTS (SELECT 1 FROM pets WHERE name=:name)")
    boolean existsByName(@Param("name") String name);

    @Modifying
    @Query("DELETE FROM pets WHERE item_obj_id IN (SELECT object_id FROM items WHERE items.owner_id=:owner)")
    int deleteByOwner(@Param("owner") int ownerObjectId);
}
