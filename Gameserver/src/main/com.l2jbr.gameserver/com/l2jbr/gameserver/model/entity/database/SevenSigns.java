package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.database.model.Entity;
import org.springframework.data.annotation.Id;

@Table("seven_signs")
public class SevenSigns extends Entity<Integer> {

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
    private long ancientAdenaAmount;
    @Column("contribution_score")
    private long contributionScore;

    public SevenSigns(int charObjId, String cabal, int seal) {
        this.charObjId = charObjId;
        this.cabal = cabal;
        this.seal = seal;
    }

    @Override
    public Integer getId() {
        return charObjId;
    }

    public String getCabal() {
        return cabal;
    }

    public int getSeal() {
        return seal;
    }

    public int getRedStones() {
        return redStones;
    }

    public int getGreenStones() {
        return greenStones;
    }

    public int getBlueStones() {
        return blueStones;
    }

    public long getAncientAdenaAmount() {
        return ancientAdenaAmount;
    }

    public long getContributionScore() {
        return contributionScore;
    }
}
