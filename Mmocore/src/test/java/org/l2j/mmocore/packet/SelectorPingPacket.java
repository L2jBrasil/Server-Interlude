package org.l2j.mmocore.packet;

import com.l2jbr.mmocore.ReceivablePacket;
import org.l2j.mmocore.selector.SelectorClient;

public class SelectorPingPacket extends ReceivablePacket<SelectorClient> {

    private long packetSize;

    @Override
    protected boolean read() {
        packetSize = readShort();
        while (availableData() > 8) {
            readLong();
        }

        while (availableData() > 4) {
            readInt();
        }

        while (availableData() > 2) {
            readShort();
        }

        while (availableData() > 1) {
            read();
        }
        return true;
    }

    @Override
    public void run() {
        client.sendPacket(new SelectorPongPacket(packetSize));
    }
}
