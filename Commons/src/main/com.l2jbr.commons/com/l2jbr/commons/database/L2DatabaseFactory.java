/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package com.l2jbr.commons.database;

import com.l2jbr.commons.Config;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;


public class L2DatabaseFactory {
    private static Logger _log = LoggerFactory.getLogger(L2DatabaseFactory.class);

    private static L2DatabaseFactory _instance;
    private final HikariDataSource _dataSource;

    public L2DatabaseFactory() throws SQLException {


        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(Config.DATABASE_URL);
        config.setUsername(Config.DATABASE_LOGIN);
        config.setPassword(Config.DATABASE_PASSWORD);
        config.addDataSourceProperty("cachePrepStmts", true);
        config.addDataSourceProperty("prepStmtCacheSize", 250);
        config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        config.addDataSourceProperty("useServerPrepStmts", true);
        config.addDataSourceProperty("useLocalSessionState", true);
        config.addDataSourceProperty("useLocalTransactionState", true);
        config.addDataSourceProperty("rewriteBatchedStatements", true);
        config.addDataSourceProperty("cacheServerConfiguration", true);
        config.addDataSourceProperty("cacheResultSetMetadata", true);
        config.addDataSourceProperty("maintainTimeStats", true);
        config.addDataSourceProperty("logger", "com.mysql.cj.core.log.Slf4JLogger");
        config.addDataSourceProperty("autoCommit", true);
        config.addDataSourceProperty("minimumIdle", 10);
        config.addDataSourceProperty("validationTimeout", 500); // 500 milliseconds wait before try to acquire connection again
        config.addDataSourceProperty("connectionTimeout", 0); // 0 = wait indefinitely for new connection if pool is exhausted
        config.addDataSourceProperty("maximumPoolSize", Config.DATABASE_MAX_CONNECTIONS);
        config.addDataSourceProperty("idleTimeout", Config.DATABASE_MAX_IDLE_TIME); // 0 = idle connections never expire
        config.addDataSourceProperty("driverClassName", Config.DATABASE_DRIVER);
        _dataSource = new HikariDataSource(config);


        // Test DB connection
        try {
            _dataSource.getConnection().close();
        } catch (SQLException e) {
            if (Config.DEBUG) {
                _log.error("Database Connection FAILED");
            }
            _log.error(e.getMessage(), e);
        }
    }

    public final static String prepQuerySelect(String[] fields, String tableName, String whereClause, boolean returnOnlyTopRecord) {
        String msSqlTop1 = "";
        String mySqlTop1 = "";
        if (returnOnlyTopRecord) {
            mySqlTop1 = " Limit 1 ";
        }
        String query = "SELECT " + msSqlTop1 + safetyString(fields) + " FROM " + tableName + " WHERE " + whereClause + mySqlTop1;
        return query;
    }

    public void shutdown() {
        try {
            _dataSource.close();
        } catch (Exception e) {
            _log.info(e.getMessage(), e);
        }

    }

    public final static String safetyString(String[] whatToCheck) {
        // NOTE: Use brace as a safty percaution just incase name is a reserved word
        String braceLeft = "`";
        String braceRight = "`";

        String result = "";
        for (String word : whatToCheck) {
            if (result != "") result += ", ";
            result += braceLeft + word + braceRight;
        }
        return result;
    }

    public static L2DatabaseFactory getInstance() throws SQLException {
        if (_instance == null) {
            _instance = new L2DatabaseFactory();
        }
        return _instance;
    }

    public Connection getConnection() //throws SQLException
    {
        Connection con = null;

        while (con == null) {
            try {
                con = _dataSource.getConnection();
            } catch (SQLException e) {
                _log.warn("L2DatabaseFactory: getConnection() failed, trying again", e);
            }
        }
        return con;
    }

    public static void close(Connection conn) {
    }
}