package com.l2jbr.commons.database;

import com.l2jbr.commons.database.model.Account;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends CrudRepository<Account, String> {

    @Modifying
    @Query("REPLACE accounts (login, password, access_level) values (:login, :password, :accessLevel)")
    int replaceAccount(@Param("login") String login, @Param("password") String password, @Param("accessLevel") short accessLevel);
}
