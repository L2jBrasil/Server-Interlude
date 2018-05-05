package com.l2jbr.commons.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.CrudRepository;

import java.sql.SQLException;
import java.util.Map;
import java.util.WeakHashMap;

public class DatabaseAccess {

    private static Logger logger = LoggerFactory.getLogger(DatabaseAccess.class);

    private static Map<Object, Object> objects = new WeakHashMap<>();

    public static <T extends CrudRepository> T getRepository(Class<T> repositoryClass) {
        if(objects.containsKey(repositoryClass)) {
            return repositoryClass.cast(objects.get(repositoryClass));
        }
        T repository = null;
        try {
            repository = L2DatabaseFactory.getInstance().getRepository(repositoryClass);
            objects.put(repositoryClass, repository);
        } catch (SQLException e) {
            logger.error("Error accessing Database", e);
        }
        return repository;
    }


}
