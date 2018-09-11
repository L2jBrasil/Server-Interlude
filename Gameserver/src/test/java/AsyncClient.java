import com.l2jbr.mmocore.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutionException;

public class AsyncClient extends AsyncMMOClient<AsyncMMOConnection<AsyncClient>> {

    SocketChannel socket;
    ByteBuffer buffer = ByteBuffer.allocateDirect(1024).order(ByteOrder.LITTLE_ENDIAN);

    public AsyncClient(AsyncMMOConnection<AsyncClient> connection) {
        super(connection);
    }

    public static AsyncClient from(int port) throws IOException, ExecutionException, InterruptedException {
        var socket = AsynchronousSocketChannel.open();
        socket.connect(new InetSocketAddress(port)).get();

        var connection = new AsyncMMOConnection<>(socket, new ReadHandler<>(new IPacketHandler<AsyncClient>() {
            @Override
            public ReceivablePacket<AsyncClient> handlePacket(ByteBuffer buf, AsyncClient client) {
                return null;
            }
        }, new IMMOExecutor<AsyncClient>() {
            @Override
            public void execute(ReceivablePacket<AsyncClient> packet) {

            }
        }), new WriteHandler<>());

        var client =  new AsyncClient(connection);
        connection.setClient(client);
        return  client;
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

    public void ping() {
        sendPacket(new PingPacket());
    }


    private static class PingPacket extends SendablePacket<AsyncClient> {

        @Override
        protected void write() {
            writeByte(0x01);
            writeByte(Byte.MAX_VALUE);
            writeChar(Character.MAX_VALUE);
            writeShort(Short.MAX_VALUE);
            writeInt(Integer.MAX_VALUE);
            writeDouble(Double.MAX_VALUE);
            writeLong(Long.MAX_VALUE);
            writeString("String");
        }
    }
}
