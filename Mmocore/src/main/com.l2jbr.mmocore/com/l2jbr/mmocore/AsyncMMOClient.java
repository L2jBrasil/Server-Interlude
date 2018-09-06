package com.l2jbr.mmocore;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class AsyncMMOClient<T extends  AsyncMMOConnection<?>> {

    private final T connection;
    private Queue<SendablePacket<?>> packetsToSend = new ConcurrentLinkedQueue<>();
    private int dataSentSize;

    public AsyncMMOClient(T connection) {
        this.connection = connection;
    }

    T getConnection() {
        return connection;
    }

    protected void sendPacket(SendablePacket<?> packet) {
        packetsToSend.add(packet);
    }

    private void write(SendablePacket<?> packet) {
        dataSentSize = packet.data.length;
        connection.write(packet.data);
    }

    public int getDataSentSize() {
        return dataSentSize;
    }

    public abstract boolean decrypt(byte[] data);
    public abstract boolean encrypt(byte[] data);
    protected abstract void onDisconnection();
}
