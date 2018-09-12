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
        sc.MAX_READ_PER_PASS = 12; // Config.MMO_MAX_READ_PER_PASS;
        sc.MAX_SEND_PER_PASS = 12; // Config.MMO_MAX_SEND_PER_PASS;
        sc.SLEEP_TIME = 20; // Config.MMO_SELECTOR_SLEEP_TIME;
        sc.HELPER_BUFFER_COUNT = 20; // Config.MMO_HELPER_BUFFER_COUNT;
        sc.TCP_NODELAY = false; // Config.MMO_TCP_NODELAY;
        var handler = new GenericClientHandler();
        selectorThread = new SelectorThread<>(sc, handler, handler, handler, null);
        selectorThread.openServerSocket(null, 8586);
        selectorThread.start();
    }
}
