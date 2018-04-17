import org.springframework.data.jdbc.core.DefaultDataAccessStrategy;
import org.springframework.data.jdbc.core.SqlGeneratorSource;
import org.springframework.data.jdbc.mapping.model.JdbcMappingContext;

public class MyAccessStrategy extends DefaultDataAccessStrategy {

    public MyAccessStrategy(SqlGeneratorSource sqlGeneratorSource, JdbcMappingContext context) {
        super(sqlGeneratorSource, context);
    }
}
