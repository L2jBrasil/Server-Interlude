package org.l2j.mmocore.async;

import com.l2jbr.mmocore.*;
import org.l2j.mmocore.packet.AsyncPingPacket;

import java.nio.ByteBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GenericClientHandler implements ClientFactory<AsyncClient>, IPacketHandler<AsyncClient>, IMMOExecutor<AsyncClient> {

    ThreadPoolExecutor threadPool = new ThreadPoolExecutor(2, 2, 15L,TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), Executors.defaultThreadFactory());

    @Override
    public AsyncClient create(AsyncMMOConnection<AsyncClient> connection) {
        return new AsyncClient(connection);
    }

    @Override
    public ReceivablePacket<AsyncClient> handlePacket(ByteBuffer buf, AsyncClient client) {
        return null;
    }

    @Override
    public ReceivablePacket<AsyncClient> handlePacket(DataWrapper wrapper, AsyncClient client) {
        var opcode = Byte.toUnsignedInt(wrapper.get());
        ReceivablePacket<AsyncClient> packet = null;
        switch (opcode) {
            case 0x01:
                packet = new AsyncPingPacket(System.currentTimeMillis());
        }
        return packet;
    }

    @Override
    public void execute(ReceivablePacket<AsyncClient> packet) {
        threadPool.execute(packet);
    }
}
