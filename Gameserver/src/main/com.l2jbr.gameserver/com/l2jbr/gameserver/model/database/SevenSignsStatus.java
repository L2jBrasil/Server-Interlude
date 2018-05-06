package com.l2jbr.gameserver.model.database;

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
    @Column("avarice_onwer")
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

    public int getAccumulatedBonus0() {
        return accumulatedBonus0;
    }

    public int getAccumulatedBonus1() {
        return accumulatedBonus1;
    }

    public int getAccumulatedBonus2() {
        return accumulatedBonus2;
    }

    public int getAccumulatedBonus3() {
        return accumulatedBonus3;
    }

    public int getAccumulatedBonus4() {
        return accumulatedBonus4;
    }
}
