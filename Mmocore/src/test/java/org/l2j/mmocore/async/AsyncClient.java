package org.l2j.mmocore.async;

import com.l2jbr.mmocore.AsyncMMOClient;
import com.l2jbr.mmocore.AsyncMMOConnection;
import com.l2jbr.mmocore.SendablePacket;

public class AsyncClient extends AsyncMMOClient<AsyncMMOConnection<AsyncClient>> {

    public AsyncClient(AsyncMMOConnection<AsyncClient> connection) {
        super(connection);
    }

    @Override
    public boolean decrypt(byte[] data) {
        return true;
    }

    @Override
    public boolean encrypt(byte[] data) {
        return true;
    }

    @Override
    protected void onDisconnection() {

    }


    public void sendPacket(SendablePacket<AsyncClient> packet) {
        writePacket(packet);
    }
}
