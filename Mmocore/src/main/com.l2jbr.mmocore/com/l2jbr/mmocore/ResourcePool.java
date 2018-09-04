package com.l2jbr.mmocore;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static java.util.Objects.nonNull;

class ResourcePool {

    private static final ByteOrder BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;
    private static final int BUFFER_SIZE = 64 * 1024;
    private static final int BYTE_BUFFER_POOL_SIZE = 20;
    private static final int STRING_BUFFER_POOL_SIZE = 10;

    private static final Queue<ByteBuffer> buffers = new ConcurrentLinkedQueue<>();
    private static final Queue<StringBuilder> stringBuffers = new ConcurrentLinkedQueue<>();


    static ByteBuffer getPooledBuffer() {
        if(buffers.isEmpty()) {
            return ByteBuffer.allocateDirect(BUFFER_SIZE).order(BYTE_ORDER);
        }
        return buffers.remove();
    }

    static void recycleBuffer(ByteBuffer buffer) {
        if(nonNull(buffer) && buffers.size() < BYTE_BUFFER_POOL_SIZE) {
            buffer.clear();
            buffers.add(buffer);
        }
    }

    static  StringBuilder getPooledStringBuffer() {
        if(stringBuffers.isEmpty()) {
            return new StringBuilder();
        }
        return stringBuffers.remove();
    }

    static void recycleStringBuffer(StringBuilder buffer) {
        if(nonNull(buffer) && stringBuffers.size() < STRING_BUFFER_POOL_SIZE) {
            buffer.setLength(0);
        }
    }
}
