package org.l2j.mmocore.async;

import com.l2jbr.mmocore.*;
import org.l2j.mmocore.packet.PongPacket;

import java.nio.ByteBuffer;

public class GenericClientHandler implements ClientFactory<AsyncClient>, IPacketHandler<AsyncClient>, IMMOExecutor<AsyncClient> {

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
                packet = new PongPacket(System.currentTimeMillis());
        }
        return packet;
    }

    @Override
    public void execute(ReceivablePacket<AsyncClient> packet) {

    }
}
