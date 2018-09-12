package org.l2j.mmocore.packet;

import com.l2jbr.mmocore.SendablePacket;
import org.l2j.mmocore.selector.SelectorClient;

public class SelectorPongPacket extends SendablePacket<SelectorClient> {

    private final long sendTime;
    private final long receivedTime;

    public SelectorPongPacket(long sendTime, long receivedTime) {
        this.sendTime = sendTime;
        this.receivedTime = receivedTime;
    }

    @Override
    protected void write() {
        writeByte(0x01);
        writeLong(sendTime);
        writeLong(receivedTime);
    }
}
