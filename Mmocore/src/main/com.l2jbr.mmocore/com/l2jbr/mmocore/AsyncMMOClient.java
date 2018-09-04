package com.l2jbr.mmocore;

import java.nio.ByteBuffer;

public abstract class AsyncMMOClient<T extends  AsyncMMOConnection<?>> {

    private final T connection;

    public AsyncMMOClient(T connection) {
        this.connection = connection;
    }

    T getConnection() {
        return connection;
    }

    public abstract boolean decrypt(final ByteBuffer buf, final int size);
    public abstract boolean encrypt(final ByteBuffer buf, final int size);
    protected abstract void onDisconnection();

}
