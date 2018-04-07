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
package com.l2jbr.commons.status;

import com.l2jbr.commons.Config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;


public abstract class Status extends Thread
{
	
	private final ServerSocket statusServerSocket;
	
	protected final int _uptime;
	protected final int _statusPort;
	protected String _statusPw;
	private final int _mode;
	
	@Override
	public void run()
	{
		while (true)
		{
			try
			{
				Socket connection = statusServerSocket.accept();

				startStatusThread(connection);

				if (isInterrupted())
				{
					try
					{
						statusServerSocket.close();
					}
					catch (IOException io)
					{
						io.printStackTrace();
					}
					break;
				}
			}
			catch (IOException e)
			{
				if (isInterrupted())
				{
					try
					{
						statusServerSocket.close();
					}
					catch (IOException io)
					{
						io.printStackTrace();
					}
					break;
				}
			}
		}
	}

    protected abstract void startStatusThread(Socket connection) throws IOException;

    public Status(int mode) throws IOException
	{
		super("Status");
		_mode = mode;
		Properties telnetSettings = new Properties();
		InputStream is = new FileInputStream(new File(Config.TELNET_FILE));
		telnetSettings.load(is);
		is.close();
		
		_statusPort = Integer.parseInt(telnetSettings.getProperty("StatusPort", "12345"));
		_statusPw = telnetSettings.getProperty("StatusPW");

        System.out.println("StatusServer Started! - Listening on Port: " + _statusPort);
        System.out.println("Password Has Been Set To: " + _statusPw);

		statusServerSocket = new ServerSocket(_statusPort);
		_uptime = (int) System.currentTimeMillis();
	}

    public abstract void sendMessageToTelnets(String msg);

}
