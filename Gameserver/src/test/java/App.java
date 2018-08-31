import com.l2jbr.mmocore.MMOConnection;

import java.io.IOException;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {

    public static void main(String[] args) throws IOException {
        App app = new App();
        app.start();
    }

    private void start() throws IOException {

        ExecutorService executor = Executors.newFixedThreadPool(10);
        AsynchronousChannelGroup group = AsynchronousChannelGroup.withThreadPool(executor);
        AsynchronousServerSocketChannel acceptor = group.provider().openAsynchronousServerSocketChannel(group);

        acceptor.accept(null, new CompletionHandler<>() {

            @Override
            public void completed(AsynchronousSocketChannel clientChannel, Object attachment) {
                if(acceptor.isOpen()) {
                    acceptor.accept(null, this);
                }

                handlerClientConnection(clientChannel);

            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                System.out.println(exc.getLocalizedMessage());
            }

        });
    }

    private void handlerClientConnection(AsynchronousSocketChannel channel) {
        var con = new MMOConnection<>(channel);
    }
}
