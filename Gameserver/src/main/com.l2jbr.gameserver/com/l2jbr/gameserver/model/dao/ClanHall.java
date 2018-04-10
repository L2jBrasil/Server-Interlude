package com.l2jbr.gameserver.model.dao;

import com.l2jbr.commons.database.dao.DAO;

public class ClanHall implements DAO {

    private int id;
    private String name;
    private int ownerId;
    private int lease;
    private String desctext;
    private String location;
    private long paidUntil;
    private int Grade;
    private int paid;
}
