package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.Items;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ItemRepository extends CrudRepository<Items, Integer> {

    @Query("SELECT * FROM items WHERE owner_id=:owner AND (loc=:loc1 OR loc=:loc2)")
    Iterable<Items> findAllByOwnerAndLocations(@Param("owner") int ownerObjectId, @Param("loc1") String loc1, @Param("loc2") String loc2);

    @Query("SELECT * FROM items WHERE owner_id=:owner AND loc=:loc")
    Iterable<Items> findAllByOwnerAndLocation(@Param("owner") int ownerObjectId, @Param("loc") String loc);

    @Query("SELECT owner_id FROM items WHERE item_id=:item")
    Optional<Integer> findOwnerIdByItem(@Param("item") int itemId);

    @Modifying
    @Query("DELETE FROM items WHERE owner_id=:owner AND item_id=:item")
    int deleteByOwnerAndItem(@Param("owner") int ownerId, @Param("item") int itemId);

    @Query("SELECT * FROM items WHERE item_id=:item AND custom_type1=:customType")
    Iterable<Items> findByItemAndCustomType(@Param("item") int itemId, @Param("customType") int customType);

    @Modifying
    @Query("UPDATE items SET loc=:loc WHERE owner_id=:owner")
    void updateLocationByOwner(@Param("owner") int ownerId, @Param("loc") String loc);

    @Modifying
    @Query("UPDATE items SET owner_id=:owner,count=:count,loc=:loc,loc_data=:locData,enchant_level=:enchantLevel,price_sell=:priceSell,price_buy=:priceBuy," +
            "custom_type1=:customType1,custom_type2=:customType2,mana_left=:mana WHERE object_id=:id")
    int updateById(@Param("id") int id, @Param("owner") int ownerId, @Param("count") int count, @Param("loc") String loc, @Param("locData") int locData,
                   @Param("enchantLevel") int enchantLevel, @Param("priceSell") int priceSell, @Param("priceBuy") int priceBuy,
                   @Param("customType1") int customType1, @Param("customType2") int customType2, @Param("mana") int mana);

    @Modifying
    @Query("DELETE FROM items WHERE owner_id=:owner")
    int deleteByOwner(@Param("owner") int ownerId);


    @Modifying
    @Query("DELETE FROM items WHERE item_id IN :items AND owner_id IN (SELECT obj_id FROM characters WHERE accesslevel <= 0)")
    int deleteItemsFromBannedOwners(@Param("items") Iterable<Integer> items);
}
