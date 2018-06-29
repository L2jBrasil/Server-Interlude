package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.database.model.Entity;
import org.springframework.data.annotation.Id;

@Table("siege_clan")
public class SiegeClan extends Entity<Integer> {

    @Id
    @Column("castle_id")
    private int castleId;
    @Column("clan_id")
    private int clanId;
    private int type;
    @Column("castle_owner")
    private int castleOwner;

    public SiegeClan() { }

    public SiegeClan(int castleId, int clanId, int typeId, int castleOwner) {
        this.castleId = castleId;
        this.clanId = clanId;
        this.type = typeId;
        this.castleOwner = castleOwner;
    }

    @Override
    public Integer getId() {
        return castleId;
    }

    public int getClanId() {
        return clanId;
    }

    public int getType() {
        return type;
    }

    public int getCastleOwner() {
        return castleOwner;
    }
}
