import com.l2jbr.gameserver.model.database.AutoChat;
import com.l2jbr.gameserver.model.database.repository.AutoChatRepository;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class App {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConfigContext.class);
        AutoChatRepository rep = context.getBean(AutoChatRepository.class);
        Optional<AutoChat> result = rep.findById(1);
        Set<String> chatTexts = result.get().getTexts().stream().map(autoChatText ->
                autoChatText.getChatText()).collect(Collectors.toSet());
        String[] strings = chatTexts.toArray(new String[]{});
        System.out.println(strings);
    }
}
