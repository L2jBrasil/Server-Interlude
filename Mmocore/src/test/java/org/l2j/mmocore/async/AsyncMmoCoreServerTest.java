package org.l2j.mmocore.async;

import com.l2jbr.mmocore.ConnectionHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class AsyncMmoCoreServerTest {

    public static void main(String[] args) throws IOException {
        AsyncMmoCoreServerTest app = new AsyncMmoCoreServerTest();
        app.start();
    }

    private void start() throws IOException {
        GenericClientHandler handler = new GenericClientHandler();
        ConnectionHandler<AsyncClient> connectionHandler = new ConnectionHandler<>(new InetSocketAddress(8585), false, 4, handler, handler, handler);
        connectionHandler.start();
    }

}
