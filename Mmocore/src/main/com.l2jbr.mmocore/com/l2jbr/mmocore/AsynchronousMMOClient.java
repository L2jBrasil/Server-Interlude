package com.l2jbr.mmocore;

import java.nio.ByteBuffer;

public abstract class AsynchronousMMOClient {

    AsyncronousMMOConnection connection;

    AsynchronousMMOClient(AsyncronousMMOConnection con) {
        this.connection = con;
    }

    public abstract boolean decrypt(final ByteBuffer buf, final int size);

    public abstract boolean encrypt(final ByteBuffer buf, final int size);

    protected abstract void onDisconnection();

    protected abstract void onForcedDisconnection();

}
