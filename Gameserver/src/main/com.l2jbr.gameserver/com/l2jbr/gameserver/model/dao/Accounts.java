package com.l2jbr.gameserver.model.dao;

import com.l2jbr.commons.database.dao.DAO;

public class Accounts implements DAO {

    private String login;
    private String password;
    private long lastActive;
    private int accessLevel;
    private String lastIp;
    private int lastServer;
}
