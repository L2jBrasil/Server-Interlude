/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package com.l2jbr.loginserver.serverpackets;

import com.l2jbr.commons.Config;
import com.l2jbr.loginserver.GameServerTable;
import com.l2jbr.loginserver.gameserverpackets.ServerStatus;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * ServerList Format: cc [cddcchhcdc] c: server list size (number of servers) c: ? [ (repeat for each servers) c: server id (ignored by client?) d: server ip d: server port c: age limit (used by client?) c: pvp or not (used by client?) h: current number of players h: max number of players c: 0 if
 * server is down d: 2nd bit: clock 3rd bit: wont dsiplay server name 4th bit: test server (used by client?) c: 0 if you dont want to display brackets in front of sever name ] Server will be considered as Good when the number of online players is less than half the maximum. as Normal between half
 * and 4/5 and Full when there's more than 4/5 of the maximum number of players
 */
public final class ServerList extends L2LoginServerPacket {

    @Override
    public void write() {
        var servers = GameServerTable.getInstance().getRegisteredGameServers();
        writeByte(0x04);
        writeByte(servers.size());
        writeByte(client.getLastServer());

        for (var server : servers.values()) {
            writeByte(server.getId());

            var host = client.usesInternalIP() ? server.getInternalHost() : server.getExternalHost();

            try {
                var ip = InetAddress.getByName(host);
                var address = ip.getAddress();
                writeByte(Byte.toUnsignedInt(address[0]));
                writeByte(Byte.toUnsignedInt(address[1]));
                writeByte(Byte.toUnsignedInt(address[2]));
                writeByte(Byte.toUnsignedInt(address[3]));
            } catch (UnknownHostException e) {
                e.printStackTrace();
                writeByte(127);
                writeByte(0);
                writeByte(0);
                writeByte(1);
            }

            writeInt(server.getPort());
            writeByte(0x00);
            writeByte(server.isPvp() ? 1 : 0);
            writeShort(server.getCurrentPlayerCount());
            writeShort(server.getMaxPlayers());

            var status = server.getStatus();
            if(ServerStatus.STATUS_GM_ONLY == server.getStatus() && client.getAccessLevel() < Config.GM_MIN) {
                status = ServerStatus.STATUS_DOWN;
            }

            writeByte(ServerStatus.STATUS_DOWN == status ? 0x00 : 0x01);

            var bits = 0;

            if (server.isTestServer()) {
                bits |= 0x04;
            }

            if (server.isShowingClock()) {
                bits |= 0x02;
            }
            writeInt(bits);
            writeByte(server.isShowingBrackets() ? 0x01 : 0x00);
        }
    }

    @Override
    protected int packetSize() {
        int servers = GameServerTable.getInstance().getRegisteredGameServers().size();
        return super.packetSize() + 3 + servers * 21;
    }
}
