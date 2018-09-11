package org.l2j.mmocore;

import org.l2j.mmocore.packet.PingPacket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;

public class Client {

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.connect(8585);
        client.sendPing();
    }

    ByteBuffer buffer = ByteBuffer.allocateDirect(11).order(ByteOrder.LITTLE_ENDIAN);
    ByteBuffer rBuffer = ByteBuffer.allocateDirect(11).order(ByteOrder.LITTLE_ENDIAN);
    private SocketChannel socket;

    public void connect(int port) throws IOException {
        socket = SocketChannel.open(new InetSocketAddress(port));
    }

    public void sendPing() throws IOException {

        PingPacket packet = new PingPacket();
        packet.write(buffer);
        buffer.flip();
        int i = socket.write(buffer);
    }

}
