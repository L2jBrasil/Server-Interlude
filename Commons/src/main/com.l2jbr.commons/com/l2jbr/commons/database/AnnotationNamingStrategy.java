package com.l2jbr.commons.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import org.springframework.data.jdbc.mapping.model.JdbcPersistentProperty;
import org.springframework.data.jdbc.mapping.model.NamingStrategy;

import static java.util.Objects.nonNull;

public class AnnotationNamingStrategy implements NamingStrategy {

    @Override
    public String getTableName(Class<?> type) {
        Table tableAnnotation = type.getAnnotation(Table.class);
        if(nonNull(tableAnnotation)) {
            return tableAnnotation.value();
        }
        return type.getSimpleName();
    }

    @Override
    public String getColumnName(JdbcPersistentProperty property) {
        Column columnAnnotation = property.getField().getAnnotation(Column.class);
        if(nonNull(columnAnnotation)) {
            return columnAnnotation.value();
        }
        return property.getName();
    }

    @Override
    public String getReverseColumnName(JdbcPersistentProperty property) {
        return getColumnName(property);

    }
}
