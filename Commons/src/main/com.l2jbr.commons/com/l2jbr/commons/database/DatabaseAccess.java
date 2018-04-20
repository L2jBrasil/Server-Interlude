package com.l2jbr.commons.database;

import org.springframework.data.repository.CrudRepository;

import java.sql.SQLException;
import java.util.Map;
import java.util.WeakHashMap;

public class DatabaseAccess {

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

        }
        return repository;
    }


}
