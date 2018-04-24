package com.l2jbr.gameserver.model.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import org.springframework.data.annotation.Id;

import java.util.Set;

@Table("castle")
public class CastleData {

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

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTaxPercent() {
        return taxPercent;
    }

    public int getTreasury() {
        return treasury;
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
