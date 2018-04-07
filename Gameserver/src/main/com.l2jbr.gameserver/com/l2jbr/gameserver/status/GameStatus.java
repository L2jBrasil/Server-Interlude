package com.l2jbr.gameserver.status;

import com.l2jbr.commons.Server;
import com.l2jbr.commons.status.Status;
import com.l2jbr.commons.util.Rnd;

import java.io.IOException;
import java.net.Socket;


public class GameStatus extends Status {

    public GameStatus() throws IOException {
        super(Server.MODE_GAMESERVER);

        if (_statusPw == null) {
            System.out.println("Server's Telnet Function Has No Password Defined!");
            System.out.println("A Password Has Been Automaticly Created!");
            _statusPw = rndPW(10);
            System.out.println("Password Has Been Set To: " + _statusPw);
        }
    }

    @Override
    protected void startStatusThread(Socket connection) throws IOException {
        new GameStatusThread(connection, _uptime, _statusPw);
    }

    @Override
    public void sendMessageToTelnets(String msg) {
        // not implemented yet
    }

    private String rndPW(int length) {
        StringBuilder password = new StringBuilder();
        String lowerChar = "qwertyuiopasdfghjklzxcvbnm";
        String upperChar = "QWERTYUIOPASDFGHJKLZXCVBNM";
        String digits = "1234567890";
        for (int i = 0; i < length; i++) {
            int charSet = Rnd.nextInt(3);
            switch (charSet) {
                case 0:
                    password.append(lowerChar.charAt(Rnd.nextInt(lowerChar.length() - 1)));
                    break;
                case 1:
                    password.append(upperChar.charAt(Rnd.nextInt(upperChar.length() - 1)));
                    break;
                case 2:
                    password.append(digits.charAt(Rnd.nextInt(digits.length() - 1)));
                    break;
            }
        }
        return password.toString();
    }
}
