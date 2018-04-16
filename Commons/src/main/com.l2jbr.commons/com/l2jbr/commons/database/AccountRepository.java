package com.l2jbr.commons.database;

import com.l2jbr.commons.database.model.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, String> {
}
