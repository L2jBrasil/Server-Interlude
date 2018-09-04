import com.l2jbr.mmocore.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public class App {

    ConnectionHandler<L2AsyncGameClient> connectionHandler;

    public static void main(String[] args) throws IOException {
        App app = new App();
        app.start();

        AsyncClient client = new AsyncClient();
        client.connect(8585);
        client.sendShort((short) 2);

    }

    private void start() throws IOException {
        connectionHandler = new ConnectionHandler<>(new InetSocketAddress(8585), 10, new L2AsyncGameClientClientFactory(), new L2AsyncGameClientPacketHandler(), new L2AsyncGameClientIMMOExecutor());
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
        public L2AsyncGameClient create(AsyncMMOConnection<L2AsyncGameClient> connection) {
            return new L2AsyncGameClient(connection);
        }
    }

    private static class L2AsyncGameClientPacketHandler implements IPacketHandler<L2AsyncGameClient> {
        @Override
        public ReceivablePacket<L2AsyncGameClient> handlePacket(ByteBuffer buf, L2AsyncGameClient client) {
            return null;
        }
    }

    private static class L2AsyncGameClientIMMOExecutor implements IMMOExecutor<L2AsyncGameClient> {
        @Override
        public void execute(ReceivablePacket<L2AsyncGameClient> packet) {

        }
    }
}
