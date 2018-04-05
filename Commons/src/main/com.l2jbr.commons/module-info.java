module com.l2jbr.commons {

    requires java.logging;
    requires java.sql;
    requires c3p0;
    requires java.naming;
    requires java.desktop;

    exports  com.l2jbr.commons;
    exports com.l2jbr.commons.util;
    exports com.l2jbr.commons.xml;
    exports com.l2jbr.commons.crypt;
    exports com.l2jbr.commons.status;
    exports com.l2jbr.commons.lib;

}