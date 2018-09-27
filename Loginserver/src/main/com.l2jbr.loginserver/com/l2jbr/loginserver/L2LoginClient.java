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

import com.l2jbr.commons.Config;
import com.l2jbr.commons.util.Rnd;
import com.l2jbr.loginserver.crypt.LoginCrypt;
import com.l2jbr.loginserver.crypt.ScrambledKeyPair;
import com.l2jbr.loginserver.serverpackets.Init;
import com.l2jbr.loginserver.serverpackets.L2LoginServerPacket;
import com.l2jbr.loginserver.serverpackets.LoginFail;
import com.l2jbr.loginserver.serverpackets.LoginFail.LoginFailReason;
import com.l2jbr.loginserver.serverpackets.PlayFail;
import com.l2jbr.loginserver.serverpackets.PlayFail.PlayFailReason;
import org.l2j.mmocore.Client;
import org.l2j.mmocore.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;

/**
 * Represents a client connected into the LoginServer
 */
public final class L2LoginClient extends Client<Connection<L2LoginClient>>
{
	private static Logger _log = LoggerFactory.getLogger(L2LoginClient.class);
	
	public enum LoginClientState
	{
		CONNECTED,
		AUTHED_GG,
		AUTHED_LOGIN
	}
	
	private LoginClientState _state;
	
	// Crypt
	private final LoginCrypt _loginCrypt;
	private final ScrambledKeyPair _scrambledPair;
	private final byte[] _blowfishKey;
	
	private String _account;
	private int _accessLevel;
	private int _lastServer;
	private boolean _usesInternalIP;
	private SessionKey _sessionKey;
	private final int _sessionId;
	private boolean _joinedGS;
	
	private final long _connectionStartTime;

	public L2LoginClient(Connection<L2LoginClient> con)
	{
		super(con);
		_state = LoginClientState.CONNECTED;
		String ip = getHostAddress();
		
		// TODO unhardcode this
		if (ip.startsWith("192.168") || ip.startsWith("10.0") || ip.startsWith("127.0.0.1"))
		{
			_usesInternalIP = true;
		}
		
		_scrambledPair = LoginController.getInstance().getScrambledRSAKeyPair();
		_blowfishKey = LoginController.getInstance().getBlowfishKey();
		_sessionId = Rnd.nextInt();
		_connectionStartTime = System.currentTimeMillis();
		_loginCrypt = new LoginCrypt();
		_loginCrypt.setKey(_blowfishKey);
	}
	
	public boolean usesInternalIP()
	{
		return _usesInternalIP;
	}


    @Override
    public boolean decrypt(byte[] data, int offset, int size) {
        boolean decrypted;
        try  {
            decrypted = _loginCrypt.decrypt(data, offset, size);
        }
        catch (IOException e) {
            _log.error(e.getLocalizedMessage(), e);
            disconnect();
            return false;
        }

        if (!decrypted) {
            _log.warn("Wrong checksum from client: {}", toString());
            disconnect();
        }

        return decrypted;
    }

    @Override
    public int encrypt(byte[] data, int offset, int size) {
        int encryptedSize = -1;
	    try {
	       encryptedSize = _loginCrypt.encrypt(data, offset, size);
        } catch (IOException e) {
	        _log.error(e.getLocalizedMessage(), e);
	        return encryptedSize;
        }
        return encryptedSize;
    }
	
	public LoginClientState getState()
	{
		return _state;
	}
	
	public void setState(LoginClientState state)
	{
		_state = state;
	}
	
	public byte[] getBlowfishKey()
	{
		return _blowfishKey;
	}
	
	public byte[] getScrambledModulus()
	{
		return _scrambledPair._scrambledModulus;
	}
	
	public RSAPrivateKey getRSAPrivateKey()
	{
		return (RSAPrivateKey) _scrambledPair._pair.getPrivate();
	}
	
	public String getAccount()
	{
		return _account;
	}
	
	public void setAccount(String account)
	{
		_account = account;
	}
	
	public void setAccessLevel(int accessLevel)
	{
		_accessLevel = accessLevel;
	}
	
	public int getAccessLevel()
	{
		return _accessLevel;
	}
	
	public void setLastServer(int lastServer)
	{
		_lastServer = lastServer;
	}
	
	public int getLastServer()
	{
		return _lastServer;
	}
	
	public int getSessionId()
	{
		return _sessionId;
	}
	
	public boolean hasJoinedGS()
	{
		return _joinedGS;
	}
	
	public void setJoinedGS(boolean val)
	{
		_joinedGS = val;
	}
	
	public void setSessionKey(SessionKey sessionKey)
	{
		_sessionKey = sessionKey;
	}
	
	public SessionKey getSessionKey()
	{
		return _sessionKey;
	}
	
	public long getConnectionStartTime()
	{
		return _connectionStartTime;
	}
	
	public void sendPacket(L2LoginServerPacket lsp) {
	    writePacket(lsp);
	}


    public void close(LoginFailReason reason) {
        close(new LoginFail(reason));
	}
	
	public void close(PlayFailReason reason) {
		close(new PlayFail(reason));
	}

    @Override
    public void onConnected() {
        sendPacket(new Init(this));
    }


	@Override
	protected void onDisconnection()
	{
        System.out.println("Disconnected");
		if (Config.DEBUG)
		{
			_log.info("DISCONNECTED: " + toString());
		}
		
		if (getState() != LoginClientState.AUTHED_LOGIN)
		{
			LoginController.getInstance().removeLoginClient(this);
		}
		else if (!hasJoinedGS())
		{
			LoginController.getInstance().removeAuthedLoginClient(getAccount());
		}
	}

    @Override
	public String toString() {
		String address =  getHostAddress();
		if (getState() == LoginClientState.AUTHED_LOGIN) {
			return "[" + getAccount() + " (" + (address.equals("") ? "disconnect" : address) + ")]";
		}
		return "[" + (address.equals("") ? "disconnect" : address) + "]";
	}
}
