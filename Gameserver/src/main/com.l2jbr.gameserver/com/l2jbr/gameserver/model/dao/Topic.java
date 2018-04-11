package com.l2jbr.gameserver.model.dao;

import com.l2jbr.commons.database.dao.DAO;

public class Topic implements DAO {

    private int topic_id;
    private int topic_forum_id;
    private String topic_name;
    private long topic_date;
    private String topic_ownername;
    private int topic_ownerid;
    private int topic_type;
    private int topic_reply;
}
