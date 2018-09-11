import com.l2jbr.mmocore.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;

public class App {

    ConnectionHandler<L2AsyncGameClient> connectionHandler;

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        App app = new App();
        app.start();

        AsyncClient client = AsyncClient.from(8585);
        client.ping();
    }

    private void start() throws IOException {
        connectionHandler = new ConnectionHandler<>(new InetSocketAddress(8585), false,10, new L2AsyncGameClientClientFactory(), new L2AsyncGameClientPacketHandler(), new L2AsyncGameClientIMMOExecutor());
        connectionHandler.start();
    }

    public static class L2AsyncGameClient extends AsyncMMOClient<AsyncMMOConnection<L2AsyncGameClient>> {

        public L2AsyncGameClient(AsyncMMOConnection<L2AsyncGameClient> connection) {
            super(connection);
        }

        @Override
        public boolean decrypt(byte[] data) {
            return true;
        }

        @Override
        public boolean encrypt(byte[] data) {
            return true;
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

        @Override
        public ReceivablePacket<L2AsyncGameClient> handlePacket(byte[] data, L2AsyncGameClient client) {
            var opcode =  Byte.toUnsignedInt(data[0]);
            ReceivablePacket<L2AsyncGameClient> packet = null;
            switch (opcode) {
                case 0x01:
                    packet = new PongPacket(System.currentTimeMillis());
            }
            return packet;
        }
    }

    private static class PongPacket extends ReceivablePacket<L2AsyncGameClient> {
        private final long recebido;
        int esperadob;
        char esperadoc;
        short esperados;
        int esperadoi;
        double esperadod;
        long esperadol;
        String esperadoStr;

        public PongPacket(long currentTimeMillis) {
            recebido = currentTimeMillis;
        }

        @Override
        protected boolean read() {
            readByte(); // readopcode
            esperadob = readByte();
            esperadoc = readChar();
            esperados = readShort();
            esperadoi = readInt();
            esperadod = readDouble();
            esperadol = readLong();
            esperadoStr = readString();
            return true;
        }

        @Override
        public void run() {
            System.out.println("packet recebido em " + recebido);

            System.out.println("Byte");
            System.out.println("esperado " + Byte.MAX_VALUE);
            System.out.println("recebido " + esperadob);

            System.out.println("Char");
            System.out.println("esperado " + (int) Character.MAX_VALUE);
            System.out.println("recebido " + (int) esperadoc);

            System.out.println("Short");
            System.out.println("esperado " + Short.MAX_VALUE);
            System.out.println("recebido " + esperados);

            System.out.println("Int");
            System.out.println("esperado " + Integer.MAX_VALUE);
            System.out.println("recebido " + esperadoi);

            System.out.println("Double");
            System.out.println("esperado " + Double.MAX_VALUE);
            System.out.println("recebido " + esperadod);

            System.out.println("Long");
            System.out.println("esperado " + Long.MAX_VALUE);
            System.out.println("recebido " + esperadol);

            System.out.println("String");
            System.out.println("esperado String");
            System.out.println("recebido " + esperadoStr);
        }
    }

    private static class L2AsyncGameClientIMMOExecutor implements IMMOExecutor<L2AsyncGameClient> {
        @Override
        public void execute(ReceivablePacket<L2AsyncGameClient> packet) {
            packet.run();
        }
    }
}
