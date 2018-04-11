package com.l2jbr.gameserver.model.dao;

import com.l2jbr.commons.database.dao.DAO;

public class Npc implements DAO {

    private int id;
    private int idTemplate;
    private String name;
    private int serverSideName;
    private String title;
    private int serverSideTitle;
    private String _class;
    private int collision_radius;
    private int collision_height;
    private int level;
    private String sex;
    private String type;
    private int attackrange;
    private int hp;
    private int mp;
    private int hpreg;
    private int mpreg;
    private int str;
    private int con;
    private int dex;
    private int _int;
    private int wit;
    private int men;
    private int exp;
    private int sp;
    private int patk;
    private int pdef;
    private int matk;
    private int mdef;
    private int atkspd;
    private int aggro;
    private int matkspd;
    private int rhand;
    private int lhand;
    private int armor;
    private int walkspd;
    private int runspd;
    private String faction_id;
    private int faction_range;
    private int isUndead;
    private int absorb_level;
    private String absorb_type; //enum('FULL_PARTY','LAST_HIT','PARTY_ONE_RANDOM') DEFAULT 'LAST_HIT' NOT NULL,
}
