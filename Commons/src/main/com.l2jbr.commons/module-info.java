module com.l2jbr.commons {
    requires java.sql;
    requires c3p0;
    requires java.naming;
    requires java.desktop;
    requires org.slf4j;

    exports com.l2jbr.commons.util;
    exports com.l2jbr.commons.xml;
    exports com.l2jbr.commons.crypt;
    exports com.l2jbr.commons.status;
    exports com.l2jbr.commons.lib;
    exports com.l2jbr.commons;

}