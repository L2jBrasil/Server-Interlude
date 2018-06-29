package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.database.model.Entity;
import org.springframework.data.annotation.Id;

@Table("games")
public class Games extends Entity<Integer> {
    @Id
    private int idnr;
    private int id;
    private int number1;
    private int number2;
    private int prize;
    private int newPrize;
    private int prize1;
    private int prize2;
    private int prize3;
    private long endDate;
    private int finished;

    public  Games() {}

    public Games(int id, int idnr, long endDate, int prize) {
        this.id = id;
        this.idnr = idnr;
        this.endDate = endDate;
        this.prize = prize;
        this.newPrize = prize;
    }

    @Override
    public Integer getId() {
        return idnr;
    }

    public int getIdnr() {
        return idnr;
    }

    public int getNumber1() {
        return number1;
    }

    public int getNumber2() {
        return number2;
    }

    public int getPrize() {
        return prize;
    }

    public int getNewPrize() {
        return newPrize;
    }

    public int getPrize1() {
        return prize1;
    }

    public int getPrize2() {
        return prize2;
    }

    public int getPrize3() {
        return prize3;
    }

    public long getEndDate() {
        return endDate;
    }

    public int getFinished() {
        return finished;
    }
}
