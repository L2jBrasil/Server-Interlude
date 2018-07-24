package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import org.springframework.data.annotation.Id;

@Table("pledge_skill_trees")
public class ClanSkillInfo {

    @Id
    @Column("skill_id")
    private int skillId;
    private int level;
    private String name;
    @Column("clan_lvl")
    private int clanLvl;
    private String Description;
    private int repCost;
    private int itemId;

    public int getId() {
        return skillId;
    }

    public int getLevel() { return  level; }

    public String getName() {
        return name;
    }

    public int getClanLvl() {
        return clanLvl;
    }

    public String getDescription() {
        return Description;
    }

    public int getRepCost() {
        return repCost;
    }

    public int getItemId() {
        return itemId;
    }

}
