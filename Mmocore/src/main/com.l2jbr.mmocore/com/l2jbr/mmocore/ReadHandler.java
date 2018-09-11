package com.l2jbr.mmocore;

import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;

import static java.util.Objects.nonNull;

public class ReadHandler<T extends AsyncMMOClient<AsyncMMOConnection<T>>> implements CompletionHandler<Integer, T> {

    static final int HEADER_SIZE = 2;

    private final IPacketHandler<T> packetHandler;
    private final IMMOExecutor<T> executor;

    public ReadHandler(IPacketHandler<T> packetHandler, IMMOExecutor<T> executor) {
        this.packetHandler = packetHandler;
        this.executor =  executor;
    }

    @Override
    public void completed(Integer bytesRead, T client) {
        var connection = client.getConnection();
        if(bytesRead < 0 ) {
            //Client disconnected.
            ResourcePool.recycleBuffer(connection.getReadingBuffer());
            connection.releaseReadingBuffer();
            client.disconnected();
            return;
        }

        var buffer = connection.getReadingBuffer();
        buffer.flip();

        if (buffer.remaining() < HEADER_SIZE){
            // no enough data to read Header. Prepare buffer to writing without data loss.
            buffer.compact();
            connection.read();
            return;
        }

        var dataSize = (buffer.getShort() & 0xFFFF) - HEADER_SIZE;

        if(dataSize > buffer.remaining()) {
            // No enough data yet, read more. Prepare buffer to writing without data loss.
            buffer.position(buffer.position() - HEADER_SIZE);
            buffer.compact();
            connection.read();
            return;
        }

        if(dataSize > 0) {
            parseAndExecutePacket(client, buffer, dataSize);
        }

        if(!buffer.hasRemaining()) {
            // No other packet had already come. so recycle it.
            ResourcePool.recycleBuffer(buffer);
            connection.releaseReadingBuffer();
        } else {
            buffer.compact();
            if(buffer.remaining() >= HEADER_SIZE) {
                completed(buffer.remaining(), client);
                return;
            }
        }
        // We need to keep reading ?
        connection.read();
    }

    private void parseAndExecutePacket(T client, ByteBuffer buffer, int dataSize) {
        byte[] data = new byte[dataSize];

        buffer.get(data, 0, dataSize);
        var decripted = client.decrypt(data);

        if(decripted) {
            DataWrapper wrapper = DataWrapper.wrap(data);
            var packet = packetHandler.handlePacket(wrapper, client);
            execute(client, packet, wrapper);
        }
    }

    private void execute(T client, ReceivablePacket<T> packet, DataWrapper wrapper) {
        if(nonNull(packet)) {
            packet._client = client;
            packet.data = wrapper.data;
            packet.dataIndex = wrapper.dataIndex;
            if(packet.read()) {
                executor.execute(packet);
            }
        }
     }

    @Override
    public void failed(Throwable exc, T client) {
        client.disconnected();
    }
}
