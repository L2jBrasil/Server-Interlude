package com.l2jbr.mmocore.async;

import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;

class ReadHandler<T extends AsyncMMOClient<AsyncMMOConnection<T>>> implements CompletionHandler<Integer, T> {

    private static final int HEADER_SIZE = 2;

    private final PacketHandler<T> packetHandler;

    ReadHandler(PacketHandler<T> packetHandler) {
        this.packetHandler = packetHandler;
    }

    @Override
    public void completed(Integer bytesRead, T client) {
        var connection = client.getConnection();
        if(bytesRead < 0 ) {
            //Client disconnected.
            ByteBufferPool.recycleBuffer(connection.getReadingBuffer());
            connection.dropReadingBuffer();
            client.onDisconnection();
            return;
        }

        if (bytesRead < HEADER_SIZE){
            // no enough data to read Header
            connection.read();
            return;
        }

        var buffer = connection.getReadingBuffer();

        buffer.flip();

        var dataSize = (buffer.getShort() & 0xFFFF) - HEADER_SIZE;

        if(dataSize > buffer.remaining()) {
            // No enough data yet, read more. Prepare buffer to writing without data loss.
            buffer.position(buffer.position() - HEADER_SIZE);
            buffer.compact();
            connection.read();
            return;
        }

        if(dataSize > 0) {
            final int pos = buffer.position();
            parseAndExecutePacket(client, buffer, dataSize);
        }

        packetHandler.handler(connection.getReadingBuffer(), client);


        ByteBufferPool.recycleBuffer(buffer);
        connection.dropReadingBuffer();
    }

    private void parseAndExecutePacket(T client, ByteBuffer buffer, int dataSize) {
        client.decrypt(buffer, dataSize);
    }

    @Override
    public void failed(Throwable exc, T client) {
        var connection = client.getConnection();
        ByteBufferPool.recycleBuffer(connection.getReadingBuffer());
        connection.dropReadingBuffer();
        client.onDisconnection();
    }
}
