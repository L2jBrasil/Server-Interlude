import com.l2jbr.commons.database.AnnotationNamingStrategy;
import com.l2jbr.commons.database.model.Entity;
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
public class ConfigContext {

    @Bean
    public DataSource dataSource() {
        HikariConfig dataSourceConfig = new HikariConfig();
        dataSourceConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSourceConfig.setJdbcUrl("jdbc:mysql://localhost/l2jdb");
        dataSourceConfig.setUsername("root");
        dataSourceConfig.setPassword("root");
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
            extractModel(event).ifPresent(Entity::onSave);
        };
    }

    private Optional<Entity> extractModel(WithEntity event) {
        Object entity = event.getEntity();
        if (entity instanceof Entity) {
            return Optional.of((Entity) entity);
        }
        return  Optional.empty();
    }

    @Bean
    public ApplicationListener<AfterLoadEvent> afterLoadEventApplicationListener() {
        return event -> {
            extractModel(event).ifPresent(Entity::onLoad);
        };
    }

    @Bean
    public App app() {
        return new App();
    }
}
