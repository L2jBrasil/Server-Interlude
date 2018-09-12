package org.l2j.mmocore.packet;

import com.l2jbr.mmocore.ReceivablePacket;
import org.l2j.mmocore.async.AsyncClient;

public class AsyncPingPacket extends ReceivablePacket<AsyncClient> {
    private final long receivedTime;
    private long sendTime;

    public AsyncPingPacket(long currentTimeMillis) {
        receivedTime = currentTimeMillis;
    }

    @Override
    protected boolean read() {
        sendTime = readLong();
        return true;
    }

    @Override
    public void run() {
        client.sendPacket(new AsyncPongPacket(sendTime, receivedTime));
    }
}