package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.database.model.Entity;
import org.springframework.data.annotation.Id;

@Table("clan_subpledges")
public class ClanSubpledges extends Entity<Integer> {

    @Id
    @Column("clan_id")
    private int clanId;
    @Column("sub_pledge_id")
    private int subPledgeId;
    private String name;
    @Column("leader_name")
    private String leaderName;

    public ClanSubpledges() {}

    public ClanSubpledges(int clanId, int subPledgeId, String subPledgeName, String leader) {
        this.clanId = clanId;
        this.subPledgeId = subPledgeId;
        this.name = subPledgeName;
        this.leaderName = leader;
    }

    @Override
    public Integer getId() {
        return clanId;
    }

    public int getSubPledgeId() {
        return subPledgeId;
    }

    public String getName() {
        return name;
    }

    public String getLeaderName() {
        return leaderName;
    }
}
