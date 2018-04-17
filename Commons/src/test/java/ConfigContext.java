import com.l2jbr.commons.database.AnnotationNamingStrategy;
import com.l2jbr.commons.database.model.Model;
import com.l2jbr.commons.util.Util;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.core.DefaultDataAccessStrategy;
import org.springframework.data.jdbc.core.SqlGeneratorSource;
import org.springframework.data.jdbc.mapping.event.AfterLoadEvent;
import org.springframework.data.jdbc.mapping.event.BeforeSaveEvent;
import org.springframework.data.jdbc.mapping.event.WithEntity;
import org.springframework.data.jdbc.mapping.model.JdbcMappingContext;
import org.springframework.data.jdbc.mapping.model.NamingStrategy;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.Optional;

@Configuration
@EnableJdbcRepositories
public class ConfigContext {

    private HikariDataSource dataSource;

    @Bean
    public HikariDataSource dataSource() {
        System.out.println("Call datasource");
        if(Util.isNull(dataSource) || dataSource.isClosed()) {
            HikariConfig dataSourceConfig = new HikariConfig();
            dataSourceConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dataSourceConfig.setJdbcUrl("jdbc:mysql://localhost/l2jdb");
            dataSourceConfig.setUsername("root");
            dataSourceConfig.setPassword("joealisson");
            dataSource = new HikariDataSource(dataSourceConfig);
        }
        return dataSource;
    }

    @Bean
    public NamedParameterJdbcOperations template(DataSource dataSource) {
        System.out.println("call template");
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public DefaultDataAccessStrategy dataAccessStrategy(JdbcMappingContext mappingContext){
        return new MyAccessStrategy(new SqlGeneratorSource(mappingContext), mappingContext);
    }

    @Bean
    public NamingStrategy namingStrategy() {
        System.out.println("call strategy");
        return new AnnotationNamingStrategy();
    }

    @Bean
    public App app() {
        return new App();
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
