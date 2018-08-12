package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.util.Objects;

@Table("heroes")
public class Hero {

    @Id
    @Column("char_id")
    private int charId;
    @Column("char_name")
    private String charName;
    @Column("class_id")
    private int classId;
    private int count;
    private int played;

    @Transient private int clanCrest;
    @Transient private String clanName;
    @Transient private int allyCrest;
    @Transient private String allyName;

    public Hero() {}

    public Hero(Nobles hero) {
        this.charId = Objects.requireNonNullElse(hero.getId(), 0);
        this.charName = hero.getCharName();
        this.classId = hero.getClassId();
        this.count = 1;
        this.played = 1;
    }

    public int getId() {
        return charId;
    }

    public String getCharName() {
        return charName;
    }

    public int getClassId() {
        return classId;
    }

    public int getCount() {
        return count;
    }

    public void incrementCount() {
        count++;
    }

    public int getPlayed() {
        return played;
    }

    public void setPlayed(int played) {
        this.played = played;
    }

    public void setClanCrest(int clanCrest) {
        this.clanCrest = clanCrest;
    }

    public int getclanCrest() {
        return clanCrest;
    }

    public void setClanName(String clanName) {
        this.clanName = clanName;
    }

    public String getClanName() {
        return clanName;
    }

    public void setAllyCrest(int allyCrest) {
        this.allyCrest = allyCrest;
    }

    public void setAllyName(String allyName) {
        this.allyName = allyName;
    }

    public String getAllyName() {
        return allyName;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getAllyCrest() {
        return allyCrest;
    }
}
