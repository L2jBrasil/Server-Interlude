package com.l2jbr.gameserver.model.dao;

import com.l2jbr.commons.database.dao.DAO;

public class Posts implements DAO {
    private int post_id;
    private String post_owner_name;
    private int post_ownerid;
    private long post_date;
    private int post_topic_id;
    private int post_forum_id;
    private String post_txt;

}
