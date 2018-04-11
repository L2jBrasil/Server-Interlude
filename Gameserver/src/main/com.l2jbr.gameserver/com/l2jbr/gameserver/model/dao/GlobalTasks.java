package com.l2jbr.gameserver.model.dao;

import com.l2jbr.commons.database.dao.DAO;

public class GlobalTasks implements DAO {

    private int id;
    private String task;
    private String type;
    private long last_activation;
    private String param1;
    private String param2;
    private String param3;
}
