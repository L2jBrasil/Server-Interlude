package com.l2jbr.mmocore;

public abstract class AsyncMMOClient<T extends  AsyncMMOConnection<?>> {

    private final T connection;

    public AsyncMMOClient(T connection) {
        this.connection = connection;
    }

    T getConnection() {
        return connection;
    }

    public void sendPacket(SendablePacket<T> packet) {
        connection.write(packet.data);
    }

    public abstract boolean decrypt(byte[] data);
    public abstract boolean encrypt(byte[] data);
    protected abstract void onDisconnection();
}
