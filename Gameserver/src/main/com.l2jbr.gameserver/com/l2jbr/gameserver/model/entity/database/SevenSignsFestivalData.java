package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.database.model.Entity;
import org.springframework.data.annotation.Id;

@Table("seven_signs_festival")
public class SevenSignsFestivalData extends Entity<Integer> {

    @Id
    private int festivalId;
    private String cabal;
    private int cycle;
    private long date;
    private int score;
    private String members;

    public SevenSignsFestivalData() { }

    public SevenSignsFestivalData(int festivalId, String cabal, int cycle, long date, int score, String members) {
        this.festivalId = festivalId;
        this.cabal = cabal;
        this.cycle = cycle;
        this.date = date;
        this.score = score;
        this.members = members;
    }

    @Override
    public Integer getId() {
        return festivalId;
    }

    public void setId(int festivalId) {
        this.festivalId = festivalId;
    }

    public String getCabal() {
        return cabal;
    }

    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public void setCabal(String cabal) {
        this.cabal = cabal;
    }
}
