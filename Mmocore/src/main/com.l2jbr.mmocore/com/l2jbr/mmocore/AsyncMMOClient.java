package com.l2jbr.mmocore;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AsyncMMOClient<T extends  AsyncMMOConnection<?>> {

    private final T connection;
    private Queue<SendablePacket<? extends  AsyncMMOClient<T>>> packetsToWrite = new ConcurrentLinkedQueue<>();
    private int dataSentSize;
    private AtomicBoolean writing = new AtomicBoolean(false);

    public AsyncMMOClient(T connection) {
        this.connection = connection;
    }

    T getConnection() {
        return connection;
    }

    protected void writePacket(SendablePacket<? extends AsyncMMOClient<T>> packet) {
        if(packetsToWrite.isEmpty() && writing.compareAndSet(false, true) ) {
            write(packet);
        } else {
            packetsToWrite.add(packet);
        }
    }

    void tryWriteNextPacket() {
        if(packetsToWrite.isEmpty()) {
            writing.getAndSet(false);
        } else {
            SendablePacket<? extends AsyncMMOClient<T>> packet = packetsToWrite.poll();
            write(packet);
        }
    }

    void resumeSend(int result) {
        dataSentSize-= result;
        connection.write();
    }

    private void write(SendablePacket<? extends  AsyncMMOClient<T>> packet) {
        int dataSize = packet.writeData();
        dataSentSize  = encrypt(packet.data, ReadHandler.HEADER_SIZE, dataSize);
        if(dataSentSize > 0) {
            connection.write(packet.data, 0, dataSentSize);
        }
    }

    public void disconnected() {
        connection.close();
        onDisconnection();
    }

    int getDataSentSize() {
        return dataSentSize;
    }

    public abstract boolean decrypt(byte[] data, int offset, int size);

    /**
     *
     * @param data - the data to be encrypted
     * @param offset - the initial index to be encrypted
     * @param size - the length of data to be encrypted
     * @return The size of the data encrypted
     */
    public abstract int encrypt(byte[] data, int offset, int size);
    protected abstract void  onDisconnection();
}
