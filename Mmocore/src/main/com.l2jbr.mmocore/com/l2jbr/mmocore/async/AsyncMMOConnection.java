package com.l2jbr.mmocore.async;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

public class AsyncMMOConnection<T extends AsyncMMOClient<?>> {

    private final AsynchronousSocketChannel channel;
    private ConnectionHandler<T>.ReadHandler readHandler;
    private ConnectionHandler<T>.WriteHandler writeHandler;

    public AsyncMMOConnection(AsynchronousSocketChannel channel, ConnectionHandler<T>.ReadHandler readHandler, ConnectionHandler<T>.WriteHandler writeHandler) {
        this.channel = channel;
        this.readHandler = readHandler;
        this.writeHandler = writeHandler;
    }

    void read(T client, ByteBuffer buffer) {
        channel.read(buffer, client, readHandler);
    }

    void write(T client, ByteBuffer buffer) {
        channel.write(buffer, client, writeHandler);
    }
}
