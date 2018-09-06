import com.l2jbr.mmocore.AsyncMMOClient;
import com.l2jbr.mmocore.AsyncMMOConnection;
import com.l2jbr.mmocore.SendablePacket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;

public class AsyncClient extends AsyncMMOClient<AsyncMMOConnection<AsyncClient>> {

    SocketChannel socket;
    ByteBuffer buffer = ByteBuffer.allocateDirect(1024).order(ByteOrder.LITTLE_ENDIAN);

    public AsyncClient(AsyncMMOConnection<AsyncClient> connection) {
        super(connection);
    }

    public void connect(int porta) throws IOException {
        socket = SocketChannel.open(new InetSocketAddress(8585));
    }

    public void sendMessage(byte[] bytes) throws IOException {
        buffer.put(bytes);
        buffer.flip();
        socket.write(buffer);
        buffer.clear();
    }

    public void sendMessage(byte data) throws IOException {
        buffer.put(data);
        buffer.flip();
        socket.write(buffer);
        buffer.clear();
    }


    public void sendShort(short value) throws IOException {
        buffer.putShort(value);
        buffer.flip();
        socket.write(buffer);
        buffer.clear();
        sendPacket(new AsyncClientSendablePacket());
    }

    @Override
    public boolean decrypt(byte[] data) {
        return false;
    }

    @Override
    public boolean encrypt(byte[] data) {
        return false;
    }

    @Override
    protected void onDisconnection() {

    }


    private static class AsyncClientSendablePacket extends SendablePacket<AsyncClient> {
        @Override
        protected void write() {

        }

        @Override
        protected int packetSize() {
            return 0;
        }
    }
}
