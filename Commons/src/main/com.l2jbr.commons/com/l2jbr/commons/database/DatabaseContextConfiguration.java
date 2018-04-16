package com.l2jbr.commons.database;

import com.l2jbr.commons.Config;
import com.l2jbr.commons.database.model.Model;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.mapping.event.AfterLoadEvent;
import org.springframework.data.jdbc.mapping.event.BeforeSaveEvent;
import org.springframework.data.jdbc.mapping.event.WithEntity;
import org.springframework.data.jdbc.mapping.model.NamingStrategy;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.Optional;

@Configuration
@EnableJdbcRepositories
public class DatabaseContextConfiguration {

    @Bean
    public HikariDataSource dataSource() {
        HikariConfig dataSourceConfig = new HikariConfig();
        dataSourceConfig.addDataSourceProperty("driverClassName", Config.DATABASE_DRIVER);
        dataSourceConfig.setJdbcUrl(Config.DATABASE_URL);
        dataSourceConfig.setUsername(Config.DATABASE_LOGIN);
        dataSourceConfig.setPassword(Config.DATABASE_PASSWORD);
        dataSourceConfig.addDataSourceProperty("maximumPoolSize", Config.DATABASE_MAX_CONNECTIONS);
        dataSourceConfig.addDataSourceProperty("idleTimeout", Config.DATABASE_MAX_IDLE_TIME); // 0 = idle connections never expire
        dataSourceConfig.addDataSourceProperty("cachePrepStmts", true);
        dataSourceConfig.addDataSourceProperty("prepStmtCacheSize", 250);
        dataSourceConfig.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        dataSourceConfig.addDataSourceProperty("useServerPrepStmts", true);
        dataSourceConfig.addDataSourceProperty("useLocalSessionState", true);
        dataSourceConfig.addDataSourceProperty("useLocalTransactionState", true);
        dataSourceConfig.addDataSourceProperty("rewriteBatchedStatements", true);
        dataSourceConfig.addDataSourceProperty("cacheServerConfiguration", true);
        dataSourceConfig.addDataSourceProperty("cacheResultSetMetadata", true);
        dataSourceConfig.addDataSourceProperty("maintainTimeStats", true);
        dataSourceConfig.addDataSourceProperty("logger", "com.mysql.cj.core.log.Slf4JLogger");
        dataSourceConfig.addDataSourceProperty("autoCommit", true);
        dataSourceConfig.addDataSourceProperty("minimumIdle", 10);
        dataSourceConfig.addDataSourceProperty("validationTimeout", 500); // 500 milliseconds wait before try to acquire connection again
        dataSourceConfig.addDataSourceProperty("connectionTimeout", 0); // 0 = wait indefinitely for new connection if pool is exhausted
        return new HikariDataSource(dataSourceConfig);
    }

    @Bean
    public NamedParameterJdbcOperations template(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public NamingStrategy namingStrategy() {
        return  new AnnotationNamingStrategy();
    }

    @Bean
    public ApplicationListener<BeforeSaveEvent> beforeSaveEventApplicationListener() {
        return event -> {
            extractModel(event).ifPresent(Model::onSave);
        };
    }

    private Optional<Model> extractModel(WithEntity event) {
        Object entity = event.getEntity();
        if (entity instanceof Model) {
            return Optional.of((Model) entity);
        }
        return  Optional.empty();
    }

    @Bean
    public ApplicationListener<AfterLoadEvent> afterLoadEventApplicationListener() {
        return event -> {
            extractModel(event).ifPresent(Model::onLoad);
        };
    }


}
