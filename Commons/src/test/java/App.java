import com.l2jbr.commons.database.AccountRepository;
import com.l2jbr.commons.database.model.Account;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConfigContext.class);
        AccountRepository rep = context.getBean(AccountRepository.class);
        Iterable<Account> result = rep.findAll();
        System.out.println(result);

    }
}
