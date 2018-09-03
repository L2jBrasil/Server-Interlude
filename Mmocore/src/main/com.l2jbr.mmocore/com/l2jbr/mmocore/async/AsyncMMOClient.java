package com.l2jbr.mmocore.async;

import java.nio.ByteBuffer;

import static java.util.Objects.isNull;

public abstract class AsyncMMOClient<T extends  AsyncMMOConnection<? super AsyncMMOClient<?>>> {

    private final T connection;
    private ByteBuffer readingBuffer;

    public AsyncMMOClient(T connection) {
        this.connection = connection;
    }

    public ByteBuffer getReadingBuffer() {
        return  readingBuffer;
    }

    public void read() {
        if(isNull(readingBuffer)) {
            readingBuffer = ByteBufferPool.getPooledBuffer();
        }
        connection.read(this, readingBuffer);
    }

    public abstract boolean decrypt(final ByteBuffer buf, final int size);
    public abstract boolean encrypt(final ByteBuffer buf, final int size);
    protected abstract void onDisconnection();
}
