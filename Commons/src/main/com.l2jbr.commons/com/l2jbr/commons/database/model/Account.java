package com.l2jbr.commons.database.model;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import org.springframework.data.annotation.Id;

@Table("accounts")
public class Account extends  Model<String> {

    @Id
    private String login;
    private String password;
    private long lastActive;
    @Column("access_level")
    private short accessLevel;
    private short lastServer;
    private String lastIP;

    public  Account() { }

    public Account(String login, String password, long lastActive, short accessLevel, short lastServer, String lastIP) {
        this.login = login;
        this.password = password;
        this.lastActive = lastActive;
        this.accessLevel = accessLevel;
        this.lastIP = lastIP;
        this.lastServer = lastServer;
    }

    @Override
    public String getId() {
        return login;
    }

    public short getAccessLevel() {
        return accessLevel;
    }

    public String getPassword() {
        return password;
    }

    public short getLastServer() {
        return lastServer;
    }

    public boolean isBanned() {
        return accessLevel < 0;
    }

    public void setLastActive(long lastActive) {
        this.lastActive = lastActive;
    }

    public void setLastIP(String lastIP) {
        this.lastIP = lastIP;
    }
}
