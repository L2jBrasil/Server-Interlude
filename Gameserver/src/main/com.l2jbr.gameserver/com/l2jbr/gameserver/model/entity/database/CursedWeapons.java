package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.database.model.Entity;
import org.springframework.data.annotation.Id;

@Table("cursed_weapons")
public class CursedWeapons extends Entity<Integer> {

    @Id
    private int itemId;
    private int playerId;
    private int playerKarma;
    private int playerPkKills;
    private int nbKills;
    private long endTime;

    public CursedWeapons() {}

    public CursedWeapons(int itemId, int playerId, int playerKarma, int playerPkKills, int nbKills, long endTime) {
        this.itemId = itemId;
        this.playerId =playerId;
        this.playerKarma = playerKarma;
        this.playerPkKills = playerPkKills;
        this.nbKills = nbKills;
        this.endTime = endTime;
    }

    @Override
    public Integer getId() {
        return itemId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getPlayerKarma() {
        return playerKarma;
    }

    public int getPlayerPkKills() {
        return playerPkKills;
    }

    public int getNbKills() {
        return nbKills;
    }

    public long getEndTime() {
        return endTime;
    }
}
