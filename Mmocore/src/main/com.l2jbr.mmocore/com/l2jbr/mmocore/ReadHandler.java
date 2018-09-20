package com.l2jbr.mmocore;

import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;

import static java.util.Objects.nonNull;

class ReadHandler<T extends AsyncMMOClient<AsyncMMOConnection<T>>> implements CompletionHandler<Integer, T> {

    static final int HEADER_SIZE = 2;

    private final IPacketHandler<T> packetHandler;
    private final IMMOExecutor<T> executor;

    ReadHandler(IPacketHandler<T> packetHandler, IMMOExecutor<T> executor) {
        this.packetHandler = packetHandler;
        this.executor =  executor;
    }

    @Override
    public void completed(Integer bytesRead, T client) {
        AsyncMMOConnection<T> connection = client.getConnection();
        if(bytesRead < 0 ) {
            client.disconnect();
            return;
        }

        var buffer = connection.getReadingBuffer();
        buffer.flip();

        if (buffer.remaining() < HEADER_SIZE){
            buffer.compact();
            connection.read();
            return;
        }

        int dataSize = Short.toUnsignedInt(buffer.getShort()) - HEADER_SIZE;

        if(dataSize > buffer.remaining()) {
            buffer.position(buffer.position() - HEADER_SIZE);
            buffer.compact();
            connection.read();
            return;
        }

        if(dataSize > 0) {
            parseAndExecutePacket(client, buffer, dataSize);
        }

        if(!buffer.hasRemaining()) {
            buffer.clear();
        } else {
            int remaining = buffer.remaining();
            buffer.compact();
            if(remaining >= HEADER_SIZE) {
                completed(remaining, client);
                return;
            }
        }
        connection.read();
    }

    private void parseAndExecutePacket(T client, ByteBuffer buffer, int dataSize) {
        byte[] data = new byte[dataSize];

        buffer.get(data, 0, dataSize);
        boolean decrypted = client.decrypt(data, 0, dataSize);

        if(decrypted) {
            DataWrapper wrapper = DataWrapper.wrap(data);
            ReceivablePacket<T> packet = packetHandler.handlePacket(wrapper, client);
            execute(client, packet, wrapper);
        }
    }

    private void execute(T client, ReceivablePacket<T> packet, DataWrapper wrapper) {
        if(nonNull(packet)) {
            packet.client = client;
            packet.data = wrapper.data;
            packet.dataIndex = wrapper.dataIndex;
            if(packet.read()) {
                executor.execute(packet);
            }
        }
     }

    @Override
    public void failed(Throwable exc, T client) {
        client.disconnect();
    }
}