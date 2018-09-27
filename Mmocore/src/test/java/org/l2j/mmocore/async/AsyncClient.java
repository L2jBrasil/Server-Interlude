package org.l2j.mmocore.async;

import org.l2j.mmocore.AsyncMMOClient;
import org.l2j.mmocore.AsyncMMOConnection;
import org.l2j.mmocore.SendablePacket;

public class AsyncClient extends AsyncMMOClient<AsyncMMOConnection<AsyncClient>> {

    AsyncClient(AsyncMMOConnection<AsyncClient> connection) {
        super(connection);
    }

    @Override
    public boolean decrypt(byte[] data, int offset, int size) {
        return true;
    }

    @Override
    public int encrypt(byte[] data, int offset, int size) {
        return size;
    }

    @Override
    protected void onDisconnection() {

    }

    @Override
    public void onConnected() {

    }


    public void sendPacket(SendablePacket<AsyncClient> packet) {
        writePacket(packet);
    }
}
