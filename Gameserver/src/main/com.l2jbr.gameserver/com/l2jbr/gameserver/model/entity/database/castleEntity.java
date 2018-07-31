package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.database.model.Entity;
import org.springframework.data.annotation.Id;

import java.util.Set;

@Table("castle")
public class castleEntity extends Entity<Integer> {

    @Id
    private int id;
    private String name;
    private int taxPercent;
    private int treasury;
    private long siegeDate;
    private int siegeDayOfWeek;
    private int siegeHourOfDay;
    @Column("castleId")
    private Set<CastleDoor> doors;

    @Override
    public void onLoad() {
        super.onLoad();

        if(siegeDayOfWeek < 1 || siegeDayOfWeek > 7) {
            siegeDayOfWeek = 7;
        }

        if(siegeHourOfDay < 0 || siegeHourOfDay > 23) {
            siegeHourOfDay = 20;
        }
    }

    @Override
     public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTaxPercent() {
        return taxPercent;
    }

    public void setTaxPercent(int taxPercent) {
        this.taxPercent = taxPercent;
    }

    public int getTreasury() {
        return treasury;
    }

    public void setTreasury(int treasury) {
        this.treasury = treasury;
    }

    public long getSiegeDate() {
        return siegeDate;
    }

    public int getSiegeDayOfWeek() {
        return siegeDayOfWeek;
    }


    public int getSiegeHourOfDay() {
        return siegeHourOfDay;
    }

    public Set<CastleDoor> getDoors() {
        return doors;
    }

}
