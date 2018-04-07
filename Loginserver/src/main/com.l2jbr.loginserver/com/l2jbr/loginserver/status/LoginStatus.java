package com.l2jbr.loginserver.status;

import com.l2jbr.commons.Server;
import com.l2jbr.commons.status.Status;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;


public class LoginStatus extends Status {

    private final List<LoginStatusThread> _loginStatus;

    public LoginStatus() throws IOException {
        super(Server.MODE_LOGINSERVER);
        _loginStatus = new LinkedList<>();
    }

    @Override
    protected void startStatusThread(Socket connection) throws IOException {
        LoginStatusThread lst = new LoginStatusThread(connection, _uptime);
        if (lst.isAlive()) {
            _loginStatus.add(lst);
        }
    }

    @Override
    public void sendMessageToTelnets(String msg) {
        List<LoginStatusThread> lsToRemove = new LinkedList<>();
        for (LoginStatusThread ls : _loginStatus) {
            if (ls.isInterrupted()) {
                lsToRemove.add(ls);
            } else {
                ls.printToTelnet(msg);
            }
        }
    }
}
