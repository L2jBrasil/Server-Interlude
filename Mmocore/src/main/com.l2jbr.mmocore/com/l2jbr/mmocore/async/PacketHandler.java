package com.l2jbr.mmocore.async;

import java.nio.ByteBuffer;

public interface PacketHandler<T extends AsyncMMOClient> {


    void handler(ByteBuffer buffer, T client);
}
