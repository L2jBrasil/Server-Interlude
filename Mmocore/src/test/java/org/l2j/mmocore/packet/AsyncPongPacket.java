package org.l2j.mmocore.packet;

import com.l2jbr.mmocore.SendablePacket;
import org.l2j.mmocore.async.AsyncClient;

public class AsyncPongPacket extends SendablePacket<AsyncClient> {

    private final long sendTime;
    private final long receivedTime;

    public AsyncPongPacket(long sendTime, long receivedTime) {
        this.sendTime = sendTime;
        this.receivedTime = receivedTime;
    }

    @Override
    protected void write() {
        writeLong(sendTime);
        writeLong(receivedTime);

    }
}
