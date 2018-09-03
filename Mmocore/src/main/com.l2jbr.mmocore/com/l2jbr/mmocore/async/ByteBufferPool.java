package com.l2jbr.mmocore.async;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static java.util.Objects.nonNull;

class ByteBufferPool {

    private static final ByteOrder BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;
    private static final int BUFFER_SIZE = 64 * 1024;
    private static final int BUFFER_POOL_SIZE = 20;

    private static final Queue<ByteBuffer> buffers = new ConcurrentLinkedQueue<>();

    static ByteBuffer getPooledBuffer() {
        if(buffers.isEmpty()) {
            return ByteBuffer.allocateDirect(BUFFER_SIZE).order(BYTE_ORDER);
        }
        return buffers.remove();
    }

    static void recycleBuffer(ByteBuffer buffer) {
        if(nonNull(buffer) && buffers.size() < BUFFER_POOL_SIZE) {
            buffer.clear();
            buffers.add(buffer);
        }
    }
}
