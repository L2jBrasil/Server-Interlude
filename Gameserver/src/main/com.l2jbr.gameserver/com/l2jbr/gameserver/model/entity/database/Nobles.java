package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.database.model.Entity;
import org.springframework.data.annotation.Id;

@Table("olympiad_nobles")
public class Nobles extends Entity<Integer> {

    @Id
    @Column("char_id")
    private int charId;
    @Column("class_id")
    private int classId;
    @Column("char_name")
    private String charName;
    @Column("olympiad_points")
    private int olympiadPoints;
    @Column("competitions_done")
    private int competitionsDone;

    public Nobles() {}

    public Nobles(int charId, int classId, String charName, int points, int competitionsDone) {
        this.charId = charId;
        this.classId = classId;
        this.charName = charName;
        this.olympiadPoints = points;
        this.competitionsDone = competitionsDone;
    }

    @Override
    public Integer getId() {
        return charId;
    }

    public int getClassId() {
        return classId;
    }

    public String getCharName() {
        return charName;
    }

    public int getOlympiadPoints() {
        return olympiadPoints;
    }

    public void addPoints(int points) {
        olympiadPoints += points;
    }

    public void setOlympiadPoints(int points) {
        olympiadPoints = points;
    }

    public int getCompetitionsDone() {
        return competitionsDone;
    }

    public void addCompentitionDone(int competitions) {
        competitionsDone += competitions;
    }
}
