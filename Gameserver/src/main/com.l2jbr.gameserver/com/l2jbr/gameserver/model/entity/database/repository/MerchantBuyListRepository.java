package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.MerchantItem;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MerchantBuyListRepository extends CrudRepository<MerchantItem, Integer> {

    @Modifying
    @Query("UPDATE merchant_buylists SET savetimer=:saveTimer WHERE time=:time")
    int updateSaveTimerByTime(@Param("time") int time, @Param("saveTimer") long saveTimer);

    @Modifying
    @Query("UPDATE merchant_buylists SET currentCount=:count WHERE item_id=:item AND shop_id=:shop")
    int updateCurrentCountByItem(@Param("shop") Integer shopId, @Param("item") int itemId, @Param("count") int count);

    @Modifying
    @Query("UPDATE merchant_buylists SET price=:price WHERE item_id=:item AND shop_id=:shop AND ordering=:order")
    int updatePriceByItem(@Param("shop") int shopId, @Param("item") int itemId, @Param("order") int order, @Param("price") int price);

    @Modifying
    @Query("DELETE FROM merchant_buylists WHERE shop_id=:shop AND ordering=:order")
    int deleteByOrder(@Param("shop") int shopId, @Param("order") int order);

    @Query("SELECT order FROM merchant_buylists WHERE shop_id=:shop AND item_id=:item AND price=:price")
    Optional<Integer> findOrderByItemAndPrice(@Param("shop") int shopId, @Param("item") int itemId, @Param("price") int price);
}
