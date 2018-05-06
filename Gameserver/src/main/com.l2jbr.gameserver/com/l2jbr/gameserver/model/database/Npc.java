package com.l2jbr.gameserver.model.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import org.springframework.data.annotation.Id;

@Table("npc")
public class Npc  {

    @Id
    private Integer id;
    private Integer idTemplate;
    private String name;
    private Integer serverSideName;
    private String title;
    private Integer serverSideTitle;
    private String _class;
    private Double collision_radius;
    private Double collision_height;
    private Integer level;
    private String sex;
    private String type;
    private Integer attackrange;
    private Integer hp;
    private Integer mp;
    private Float hpreg;
    private Integer mpreg;
    private Integer str;
    private Integer con;
    private Integer dex;
    @Column("int")
    private Integer _int;
    private Integer wit;
    private Integer men;
    private Integer exp;
    private Integer sp;
    private Integer patk;
    private Integer pdef;
    private Integer matk;
    private Integer mdef;
    private Integer atkspd;
    private Integer aggro;
    private Integer matkspd;
    private Integer rhand;
    private Integer lhand;
    private Integer armor;
    private Integer walkspd;
    private Integer runspd;
    @Column("faction_id")
    private String factionId;
    @Column("faction_range")
    private Integer factionRange;
    private Integer isUndead;
    @Column("absorb_level")
    private Integer absorbLevel;
    @Column("absorb_type")
    private String absorbType; //enum('FULL_PARTY','LAST_HIT','PARTY_ONE_RANDOM') DEFAULT 'LAST_HIT' NOT NULL,

    public Integer getId() {
        return  id;
    }

    public int getIdTemplate() {
        return idTemplate;
    }

    public String getName() {
        return name;
    }

    public int getServerSideName() {
        return serverSideName;
    }

    public String getTitle() {
        return title;
    }

    public int getServerSideTitle() {
        return serverSideTitle;
    }

    public String getNpcClass() {
        return _class;
    }

    public double getCollision_radius() {
        return collision_radius;
    }

    public double getCollision_height() {
        return collision_height;
    }

    public int getLevel() {
        return level;
    }

    public String getSex() {
        return sex;
    }

    public String getType() {
        return type;
    }

    public int getAttackrange() {
        return attackrange;
    }

    public int getHp() {
        return hp;
    }

    public int getMp() {
        return mp;
    }

    public float getHpreg() {
        return hpreg;
    }

    public int getMpreg() {
        return mpreg;
    }

    public int getStr() {
        return str;
    }

    public int getCon() {
        return con;
    }

    public int getDex() {
        return dex;
    }

    public int getInt() {
        return _int;
    }

    public int getWit() {
        return wit;
    }

    public int getMen() {
        return men;
    }

    public int getExp() {
        return exp;
    }

    public int getSp() {
        return sp;
    }

    public int getPatk() {
        return patk;
    }

    public int getPdef() {
        return pdef;
    }

    public int getMatk() {
        return matk;
    }

    public int getMdef() {
        return mdef;
    }

    public int getAtkspd() {
        return atkspd;
    }

    public int getAggro() {
        return aggro;
    }

    public int getMatkspd() {
        return matkspd;
    }

    public int getRhand() {
        return rhand;
    }

    public int getLhand() {
        return lhand;
    }

    public int getArmor() {
        return armor;
    }

    public int getWalkspd() {
        return walkspd;
    }

    public int getRunspd() {
        return runspd;
    }

    public String getFactionId() {
        return factionId;
    }

    public int getFactionRange() {
        return factionRange;
    }

    public int getIsUndead() {
        return isUndead;
    }

    public int getAbsorbLevel() {
        return absorbLevel;
    }

    public String getAbsorbType() {
        return absorbType;
    }
}
