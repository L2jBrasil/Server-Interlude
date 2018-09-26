package org.l2j.mmocore.selector;

import com.l2jbr.mmocore.SelectorConfig;
import com.l2jbr.mmocore.SelectorThread;

import java.io.IOException;

public class SelectorMmoCoreServerTest {

    private SelectorThread<SelectorClient> selectorThread;

    public static void main(String[] args) throws IOException {
        SelectorMmoCoreServerTest app =  new SelectorMmoCoreServerTest();
        app.start();
    }

    private void start() throws IOException {
        final SelectorConfig sc = new SelectorConfig();
        sc.MAX_READ_PER_PASS = 12;
        sc.MAX_SEND_PER_PASS = 12;
        sc.SLEEP_TIME = 20;
        sc.HELPER_BUFFER_COUNT = 20;
        sc.TCP_NODELAY = false;
        var handler = new GenericClientHandler();
        selectorThread = new SelectorThread<>(sc, handler, handler, handler, null);
        selectorThread.openServerSocket(null, 8080);
        selectorThread.start();
    }
}
