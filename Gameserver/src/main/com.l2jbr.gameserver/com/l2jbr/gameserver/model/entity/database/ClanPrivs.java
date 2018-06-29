package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.database.model.Entity;
import org.springframework.data.annotation.Id;

@Table("clan_privs")
public class ClanPrivs extends Entity<Integer> {

    @Id
    @Column("clan_id")
    private int clanId;
    private int rank;
    private int party;
    private int privs;

    @Override
    public Integer getId() {
        return clanId;
    }

    public int getRank() {
        return rank;
    }

    public int getParty() {
        return party;
    }

    public int getPrivs() {
        return privs;
    }


}
