package org.l2j.mmocore.selector;

import com.l2jbr.mmocore.MMOClient;
import com.l2jbr.mmocore.MMOConnection;
import com.l2jbr.mmocore.SendablePacket;

import java.nio.ByteBuffer;

public class SelectorClient  extends MMOClient<MMOConnection<SelectorClient>> {

    public SelectorClient(MMOConnection<SelectorClient> con) {
        super(con);
    }

    @Override
    public boolean decrypt(ByteBuffer buf, int size) {
        return true;
    }

    @Override
    public boolean encrypt(ByteBuffer buf, int size) {
        buf.position(buf.position() + size);
        return true;
    }

    @Override
    protected void onDisconnection() {

    }

    @Override
    protected void onForcedDisconnection() {

    }

    public void sendPacket(SendablePacket<SelectorClient> packet) {
        getConnection().sendPacket(packet);
    }
}
