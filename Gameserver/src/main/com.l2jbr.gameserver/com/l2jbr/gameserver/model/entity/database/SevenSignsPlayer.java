package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.database.model.Entity;
import org.springframework.data.annotation.Id;

@Table("seven_signs")
public class SevenSignsPlayer extends Entity<Integer> {

    @Id
    @Column("char_obj_id")
    private int charObjId;
    private String cabal;
    private int seal;
    @Column("red_stones")
    private int redStones;
    @Column("green_stones")
    private int greenStones;
    @Column("blue_stones")
    private int blueStones;
    @Column("ancient_adena_amount")
    private int ancientAdenaAmount;
    @Column("contribution_score")
    private int contributionScore;

    public SevenSignsPlayer(int charObjId, String cabal, int seal) {
        this.charObjId = charObjId;
        this.cabal = cabal;
        this.seal = seal;
    }

    public int getStoneContrib() {
        return  redStones + blueStones + greenStones;
    }

    @Override
    public Integer getId() {
        return charObjId;
    }

    public String getCabal() {
        return cabal;
    }

    public void setCabal(String cabal) {
        this.cabal = cabal;
    }

    public int getSeal() {
        return seal;
    }

    public void setSeal(int seal) {
        this.seal = seal;
    }

    public int getRedStones() {
        return redStones;
    }

    public void setRedStones(int stones) {
        this.redStones = stones;
    }

    public int getGreenStones() {
        return greenStones;
    }

    public void setGreenStones(int stones) {
        this.greenStones = stones;
    }

    public int getBlueStones() {
        return blueStones;
    }

    public void setBlueStones(int stones) {
        this.blueStones = stones;
    }

    public int getAncientAdenaAmount() {
        return ancientAdenaAmount;
    }

    public void setAncientAdenaAmount(int totalAncientAdena) {
        this.ancientAdenaAmount = totalAncientAdena;
    }

    public int getContributionScore() {
        return contributionScore;
    }

    public void setContribuitionScore(int contribuition) {
        this.contributionScore = contribuition;
    }
}
