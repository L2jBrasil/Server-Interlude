package org.l2j.mmocore.packet;

import com.l2jbr.mmocore.ReceivablePacket;
import org.l2j.mmocore.async.AsyncClient;

public class PongPacket extends ReceivablePacket<AsyncClient> {
    private final long receivedTime;
    private long sendTime;

    public PongPacket(long currentTimeMillis) {
        receivedTime = currentTimeMillis;
    }

    @Override
    protected boolean read() {
        sendTime = readLong();
        return true;
    }

    @Override
    public void run() {

    }
}