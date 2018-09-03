import com.l2jbr.mmocore.async.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public class App {

    public static void main(String[] args) throws IOException {
        App app = new App();
        app.start();
    }

    private void start() throws IOException {
        var connectionHandler = new ConnectionHandler<>(new InetSocketAddress(8585), 10, new L2AsyncGameClientClientFactory(), new L2AsyncGameClientPacketHandler());
        connectionHandler.start();
    }

    public static class L2AsyncGameClient extends AsyncMMOClient<AsyncMMOConnection<L2AsyncGameClient>> {

        public L2AsyncGameClient(AsyncMMOConnection<L2AsyncGameClient> connection) {
            super(connection);
        }

        @Override
        public boolean decrypt(ByteBuffer buf, int size) {
            return false;
        }

        @Override
        public boolean encrypt(ByteBuffer buf, int size) {
            return false;
        }

        @Override
        protected void onDisconnection() {

        }
    }


    private static class L2AsyncGameClientClientFactory implements ClientFactory<L2AsyncGameClient> {
        @Override
        public L2AsyncGameClient create(AsyncMMOConnection<L2AsyncGameClient> channel) {
            return null;
        }
    }

    private static class L2AsyncGameClientPacketHandler implements PacketHandler<L2AsyncGameClient> {
        @Override
        public void handler(ByteBuffer buffer, L2AsyncGameClient client) {

        }
    }
}
