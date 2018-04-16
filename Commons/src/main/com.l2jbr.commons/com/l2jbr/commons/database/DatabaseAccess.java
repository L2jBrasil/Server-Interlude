package com.l2jbr.commons.database;

import org.springframework.data.repository.CrudRepository;

import java.sql.SQLException;
import java.util.Map;
import java.util.WeakHashMap;

public class DatabaseAccess {

    private static Map<Object, Object> objects = new WeakHashMap<>();

    public static <T extends CrudRepository> T getRepository(Class<T> repositoryClass) {

        for (Map.Entry entry : objects.entrySet()) {
            if (repositoryClass.equals(entry.getValue())) {
                if(entry.getKey().getClass().isAssignableFrom(repositoryClass)) {
                    return repositoryClass.cast(entry.getKey());
                }
            }
        }
        T repository = null;
        try {
            repository = L2DatabaseFactory.getInstance().getRepository(repositoryClass);
            objects.put(repository, repositoryClass);
        } catch (SQLException e) {

        }
        return repository;
    }


}
