package com.l2jbr.gameserver.model.database;

import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.database.model.Entity;
import org.python.antlr.op.Mod;
import org.springframework.data.annotation.Id;

@Table("mods_wedding")
public class ModsWedding extends Entity<Integer> {

    @Id
    private int id;
    private int player1Id;
    private int player2Id;
    private String married;
    private long affianceDate;
    private long weddingDate;

    public ModsWedding() {}

    public ModsWedding(int id, int player1Id, int player2Id, boolean married, long affianceDate, long weddingDate) {
        this.id = id;
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.married = String.valueOf(married);
        this.affianceDate = affianceDate;
        this.weddingDate = weddingDate;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public int getPlayer1Id() {
        return player1Id;
    }

    public int getPlayer2Id() {
        return player2Id;
    }

    public String getMarried() {
        return married;
    }

    public long getAffianceDate() {
        return affianceDate;
    }

    public long getWeddingDate() {
        return weddingDate;
    }
}
