package org.l2j.mmocore.selector;

import com.l2jbr.mmocore.*;
import org.l2j.mmocore.packet.SelectorPingPacket;

import java.nio.ByteBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GenericClientHandler implements IPacketHandler<SelectorClient>, IMMOExecutor<SelectorClient>, IClientFactory<SelectorClient> {

    ThreadPoolExecutor threadPool = new ThreadPoolExecutor(2, 2, 15L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), Executors.defaultThreadFactory());

    @Override
    public SelectorClient create(MMOConnection<SelectorClient> connection) {
        return new SelectorClient(connection);
    }

    @Override
    public ReceivablePacket<SelectorClient> handlePacket(ByteBuffer buf, SelectorClient client) {
        var opcode = Byte.toUnsignedInt(buf.get());
        ReceivablePacket<SelectorClient> packet = null;
        switch (opcode) {
            case 0x01:
                packet = new SelectorPingPacket(System.currentTimeMillis());
        }
        return packet;
    }

    @Override
    public ReceivablePacket<SelectorClient> handlePacket(DataWrapper wrapper, SelectorClient client) {
        return null;
    }

    @Override
    public void execute(ReceivablePacket<SelectorClient> packet) {
        threadPool.execute(packet);
    }
}

