package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import org.springframework.data.annotation.Id;

@Table("seven_signs_status")
public class SevenSignsStatus  {

    @Id
    private int id;
    @Column("current_cycle")
    private int currentCycle;
    @Column("festival_cycle")
    private int festivalCycle;
    @Column("active_period")
    private int activePeriod;
    private int date;
    @Column("previous_winner")
    private int previousWinner;
    @Column("dawn_stone_score")
    private long dawnStoneScore;
    @Column("dawn_festival_score")
    private int dawnFestivalScore;
    @Column("dusk_stone_score")
    private long duskStoneScore;
    @Column("dusk_festival_score")
    private int duskFestivalScore;
    @Column("avarice_owner")
    private int avariceOwner;
    @Column("gnosis_owner")
    private int gnosisOwner;
    @Column("strife_owner")
    private int strifeOwner;
    @Column("avarice_dawn_score")
    private int avariceDawnScore;
    @Column("gnosis_dawn_score")
    private int gnosisDawnScore;
    @Column("strife_dawn_score")
    private int strifeDawnScore;
    @Column("avarice_dusk_score")
    private int avariceDuskScore;
    @Column("gnosis_dusk_score")
    private int gnosisDuskScore;
    @Column("strife_dusk_score")
    private int strifeDuskScore;
    @Column("accumulated_bonus0")
    private int accumulatedBonus0;
    @Column("accumulated_bonus1")
    private int accumulatedBonus1;
    @Column("accumulated_bonus2")
    private int accumulatedBonus2;
    @Column("accumulated_bonus3")
    private int accumulatedBonus3;
    @Column("accumulated_bonus4")
    private int accumulatedBonus4;

    public int getId() {
        return id;
    }

    public int getCurrentCycle() {
        return currentCycle;
    }

    public int getFestivalCycle() {
        return festivalCycle;
    }

    public int getActivePeriod() {
        return activePeriod;
    }

    public int getDate() {
        return date;
    }

    public int getPreviousWinner() {
        return previousWinner;
    }

    public long getDawnStoneScore() {
        return dawnStoneScore;
    }

    public int getDawnFestivalScore() {
        return dawnFestivalScore;
    }

    public long getDuskStoneScore() {
        return duskStoneScore;
    }

    public int getDuskFestivalScore() {
        return duskFestivalScore;
    }

    public int getAvariceOwner() {
        return avariceOwner;
    }

    public int getGnosisOwner() {
        return gnosisOwner;
    }

    public int getStrifeOwner() {
        return strifeOwner;
    }

    public int getAvariceDawnScore() {
        return avariceDawnScore;
    }

    public int getGnosisDawnScore() {
        return gnosisDawnScore;
    }

    public int getStrifeDawnScore() {
        return strifeDawnScore;
    }

    public int getAvariceDuskScore() {
        return avariceDuskScore;
    }

    public int getGnosisDuskScore() {
        return gnosisDuskScore;
    }

    public int getStrifeDuskScore() {
        return strifeDuskScore;
    }

    public void incrementCurrentCycle() {
        currentCycle++;
    }

    public void incrementActivePeriod() {
        activePeriod++;
    }

    public void setActivePeriod(int period) {
        this.activePeriod = period;
    }

    public void setPreviousWinner(int winner) {
        previousWinner = winner;
    }

    public void addDawnStoneScore(int score) {
        dawnStoneScore += score;
    }

    public void setDawnStoneScore(int score) {
        dawnStoneScore = score;
    }

    public void changeDawnFestivalScore(int score) {
        dawnFestivalScore += score;
    }

    public void setDawnFestivalScore(int score) {
        dawnFestivalScore = score;
    }

    public void addDuskStoneScore(int score) {
        duskStoneScore += score;
    }

    public void setDuskStoneScore(int score) {
        duskStoneScore = score;
    }

    public void changeDuskFestivalScore(int score) {
        duskFestivalScore += score;
    }

    public void setDuskFestivalScore(int score) {
        duskFestivalScore = score;
    }

    public void setAvariceOwner(int owner) {
        avariceOwner = owner;
    }

    public void setGnosisOwner(int owner) {
        gnosisOwner = owner;
    }

    public void setStrifeOwner(int owner) {
        strifeOwner = owner;
    }

    public void setAvariceDawnScore(int score) {
        avariceDawnScore = score;
    }

    public void setGnosisDawnScore(int score) {
        this.gnosisDawnScore = score;
    }

    public void setStrifeDawnScore(int score) {
        this.strifeDawnScore = score;
    }

    public void incrementDawnSealScore(int seal) {
        switch (seal) {
            case 1:
                avariceDawnScore++;
                break;
            case 2:
                gnosisDawnScore++;
                break;
            case 3:
                strifeDawnScore++;
        }
    }

    public void incrementDuskSealScore(int seal) {
        switch (seal) {
            case 1:
                avariceDuskScore++;
                break;
            case 2:
                gnosisDuskScore++;
                break;
            case 3:
                strifeDuskScore++;
        }
    }

    public void setAvariceDuskScore(int score) {
        avariceDuskScore = score;
    }

    public void setGnosisDuskScore(int score) {
        this.gnosisDuskScore = score;
    }

    public void setStrifeDuskScore(int score) {
        this.strifeDuskScore = score;
    }

    public void setFestivalCycle(int cycle) {
        this.festivalCycle = cycle;
    }

    public void incrementFestivalCycle() {
        festivalCycle++;
    }

    public int getAccumulatedBonusForFestival(int festivalId) {
        switch (festivalId) {
            case 0:
                return accumulatedBonus0;
            case 1:
                return accumulatedBonus1;
            case 2:
                return accumulatedBonus2;
            case 3:
                return accumulatedBonus3;
            case 4:
                return accumulatedBonus4;
        }
        return 0;
    }

    public int getAccumulatedBonusSum() {
        return accumulatedBonus0 + accumulatedBonus1 + accumulatedBonus2 + accumulatedBonus3 + accumulatedBonus4;
    }

    public void setAccumulatedBonusForFestival(int festivalId, int bonus) {
        switch (festivalId) {
            case 0:
                accumulatedBonus0 = bonus;
                break;
            case 1:
                accumulatedBonus1 = bonus;
                break;
            case 2:
                accumulatedBonus2 = bonus;
                break;
            case 3:
                accumulatedBonus3 = bonus;
                break;
            case 4:
                accumulatedBonus4 = bonus;
        }
    }
}
