package com.l2jbr.commons.database;

import com.l2jbr.commons.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.CrudRepository;

import java.util.LinkedHashMap;
import java.util.Map;

public class DatabaseAccess {

    private static Logger logger = LoggerFactory.getLogger(DatabaseAccess.class);

    private static Map<Object, Object> objects = new LinkedHashMap<>();

    public static <T extends CrudRepository> T getRepository(Class<T> repositoryClass) {
        if(objects.containsKey(repositoryClass)) {
            return repositoryClass.cast(objects.get(repositoryClass));
        }
        T repository = null;
        try {
            repository = L2DatabaseFactory.getInstance().getRepository(repositoryClass);
            if(Util.isNotNull(repository)) {
                objects.put(repositoryClass, repository);
            }
        } catch (Exception e) {
            logger.error("Error accessing Database", e);
        }
        return repository;
    }


}
