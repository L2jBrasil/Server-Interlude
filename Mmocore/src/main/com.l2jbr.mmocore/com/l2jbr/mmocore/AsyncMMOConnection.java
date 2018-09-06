package com.l2jbr.mmocore;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

import static com.l2jbr.mmocore.ResourcePool.getPooledBuffer;
import static com.l2jbr.mmocore.ResourcePool.recycleBuffer;
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
            readingBuffer = getPooledBuffer();
        }
        return readingBuffer;
    }

    void releaseReadingBuffer() {
        recycleBuffer(readingBuffer);
        readingBuffer=null;
    }

    public void read() {
        channel.read(getReadingBuffer(), client, readHandler);
    }

    public void write(byte[] data) {
        ByteBuffer buffer = getWritingBuffer();
        buffer.put(data);
        channel.write(writingBuffer, client, writeHandler);
    }

    ByteBuffer getWritingBuffer() {
        if(isNull(writingBuffer)) {
            writingBuffer =  getPooledBuffer();
        }
        return writingBuffer;
    }

    void releaseWritingBuffer() {
        recycleBuffer(writingBuffer);
        writingBuffer = null;
    }

    void close() {

    }
}
