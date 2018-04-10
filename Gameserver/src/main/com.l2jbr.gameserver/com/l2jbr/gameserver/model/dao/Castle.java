package com.l2jbr.gameserver.model.dao;

import com.l2jbr.commons.database.dao.DAO;

public class Castle implements DAO {
    private int id;
    private String name;
    private int taxPercent;
    private int treasury;
    private long siegeDate;
    private int siegeDayOfWeek;
    private int siegeHourOfDay;
}
