package org.l2j.mmocore.packet;

import com.l2jbr.mmocore.ReceivablePacket;
import org.l2j.mmocore.selector.SelectorClient;

public class SelectorPingPacket extends ReceivablePacket<SelectorClient> {

    private final long receivedTime;
    private long sendTime;

    public SelectorPingPacket(long currentTimeMillis) {
        receivedTime = currentTimeMillis;
    }

    @Override
    protected boolean read() {
        sendTime = readLong();
        return true;
    }

    @Override
    public void run() {
        client.sendPacket(new SelectorPongPacket(sendTime, receivedTime));
    }
}
