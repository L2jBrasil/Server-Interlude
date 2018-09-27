/* This program is free software; you can redistribute it and/or modify
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
package com.l2jbr.loginserver;

import org.l2j.mmocore.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author KenM
 */
public class SelectorHelper implements PacketExecutor<L2LoginClient>, ClientFactory<L2LoginClient>, ConnectionFilter {
	private final ThreadPoolExecutor _generalPacketsThreadPool;
	
	public SelectorHelper() {
		_generalPacketsThreadPool = new ThreadPoolExecutor(4, 6, 15L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
	}
	
	@Override
	public void execute(ReadablePacket<L2LoginClient> packet) {
		_generalPacketsThreadPool.execute(packet);
	}

	@Override
	public L2LoginClient create(Connection<L2LoginClient> connection) {
		return new L2LoginClient(connection);
	}

	@Override
	public boolean accept(AsynchronousSocketChannel channel) {
		try {
			InetSocketAddress socketAddress = (InetSocketAddress) channel.getRemoteAddress();
			return !LoginController.getInstance().isBannedAddress(socketAddress.getAddress());
		} catch (IOException e) {
            return false;
		}
	}
}
