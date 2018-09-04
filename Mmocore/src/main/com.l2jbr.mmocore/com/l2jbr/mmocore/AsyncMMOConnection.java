package com.l2jbr.mmocore;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

import static java.util.Objects.isNull;

public class AsyncMMOConnection<T extends AsyncMMOClient<AsyncMMOConnection<T>>> {

    private final AsynchronousSocketChannel channel;
    private final ReadHandler<T> readHandler;
    private final WriteHandler<T> writeHandler;
    private T client;

    private ByteBuffer readingBuffer;
    private ByteBuffer writingBuffer;

    AsyncMMOConnection(AsynchronousSocketChannel channel, ReadHandler<T> readHandler, WriteHandler<T> writeHandler) {
        this.channel = channel;
        this.readHandler = readHandler;
        this.writeHandler = writeHandler;
    }

    public void setClient(T client) {
        this.client = client;
    }

    ByteBuffer getReadingBuffer() {
        if(isNull(readingBuffer)) {
            readingBuffer = ResourcePool.getPooledBuffer();
        }
        return readingBuffer;
    }

    void dropReadingBuffer() {
        readingBuffer =null;
    }

    public void read() {
        channel.read(getReadingBuffer(), client, readHandler);
    }

    public void write() {
        channel.write(writingBuffer, client, writeHandler);
    }

    public void close() {

    }
}
