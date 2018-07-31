import com.l2jbr.gameserver.model.entity.database.castleEntity;
import com.l2jbr.gameserver.model.entity.database.repository.CastleRepository;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConfigContext.class);
        CastleRepository rep = context.getBean(CastleRepository.class);
        Iterable<castleEntity> result = rep.findAll();
        System.out.println(result);
    }
}
