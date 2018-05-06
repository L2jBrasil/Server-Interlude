package com.l2jbr.gameserver.model.database;

import com.l2jbr.commons.database.annotation.Table;
import org.springframework.data.annotation.Id;

@Table("seven_signs_festival")
public class SevenSignsFestival  {

    @Id
    private int festivalId;
    private String cabal;
    private int cycle;
    private long date;
    private int score;
    private String members;

    public SevenSignsFestival() { }

    public SevenSignsFestival(int festivalId, String cabal, int cycle, long date, int score, String members) {
        this.festivalId = festivalId;
        this.cabal = cabal;
        this.cycle = cycle;
        this.date = date;
        this.score = score;
        this.members = members;
    }

    public int getFestivalId() {
        return festivalId;
    }

    public String getCabal() {
        return cabal;
    }

    public int getCycle() {
        return cycle;
    }

    public long getDate() {
        return date;
    }

    public int getScore() {
        return score;
    }

    public String getMembers() {
        return members;
    }
}
