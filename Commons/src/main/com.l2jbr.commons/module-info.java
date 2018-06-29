module com.l2jbr.commons {
    requires java.sql;
    requires java.naming;
    requires java.desktop;
    requires org.slf4j;
    requires com.zaxxer.hikari;
    requires spring.data.commons;
    requires spring.data.jdbc;
    requires spring.context;
    requires spring.jdbc;
    requires java.xml.bind;

    exports com.l2jbr.commons.util;
    exports com.l2jbr.commons.xml;
    exports com.l2jbr.commons.crypt;
    exports com.l2jbr.commons.status;
    exports com.l2jbr.commons.lib;
    exports com.l2jbr.commons;
    exports com.l2jbr.commons.database;
    exports com.l2jbr.commons.database.model;
    exports com.l2jbr.commons.database.annotation;

}