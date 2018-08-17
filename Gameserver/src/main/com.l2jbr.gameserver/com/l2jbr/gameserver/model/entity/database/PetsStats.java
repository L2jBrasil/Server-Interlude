package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import org.springframework.data.annotation.Id;

@Table("pets_stats")
public class PetsStats  {

    @Id
    private int typeID;
    private String type;
    private int level;
    private int expMax;
    private int hpMax;
    private int mpMax;
    private int patk;
    private int pdef;
    private int matk;
    private int mdef;
    private int acc;
    private int evasion;
    private int crit;
    private int speed;
    @Column("atk_speed")
    private int atkSpeed;
    @Column("cast_speed")
    private int castSpeed;
    private int feedMax;
    private int feedbattle;
    private int feednormal;
    private int loadMax;
    private int hpregen;
    private int mpregen;
    @Column("owner_exp_taken")
    private int ownerExpTaken;

    public int getTypeID() {
        return typeID;
    }

    public String getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    public int getExpMax() {
        return expMax;
    }

    public int getHpMax() {
        return hpMax;
    }

    public int getMpMax() {
        return mpMax;
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

    public int getAcc() {
        return acc;
    }

    public int getEvasion() {
        return evasion;
    }

    public int getCrit() {
        return crit;
    }

    public int getSpeed() {
        return speed;
    }

    public int getAtkSpeed() {
        return atkSpeed;
    }

    public int getCastSpeed() {
        return castSpeed;
    }

    public int getFeedMax() {
        return feedMax;
    }

    public int getFeedbattle() {
        return feedbattle;
    }

    public int getFeednormal() {
        return feednormal;
    }

    public int getLoadMax() {
        return loadMax;
    }

    public int getHpregen() {
        return hpregen;
    }

    public int getMpregen() {
        return mpregen;
    }

    public int getOwnerExpTaken() {
        return ownerExpTaken;
    }


}
