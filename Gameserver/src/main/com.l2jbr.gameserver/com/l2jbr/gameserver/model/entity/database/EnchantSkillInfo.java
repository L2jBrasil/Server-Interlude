package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import org.springframework.data.annotation.Id;

@Table("enchant_skill_trees")
public class EnchantSkillInfo {
    @Id
    @Column("skill_id")
    private int id;
    private int level;
    private String name;
    @Column("base_lvl")
    private int baseLvl;
    @Column("enchant_type")
    private String enchantType;
    private int sp;
    private int exp;
    @Column("min_skill_lvl")
    private int minSkillLevel;
    @Column("success_rate76")
    private byte successRate76;
    @Column("success_rate77")
    private byte successRate77;
    @Column("success_rate78")
    private byte successRate78;

    public int getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public int getBaseLevel() {
        return baseLvl;
    }

    public String getEnchantType() {
        return enchantType;
    }

    public int getSp() {
        return sp;
    }

    public int getExp() {
        return exp;
    }

    public int getMinSkillLevel() {
        return minSkillLevel;
    }

    public byte getRate(L2PcInstance ply)
    {
        byte result;
        switch (ply.getLevel())
        {
            case 76:
                result = successRate76;
                break;
            case 77:
                result = successRate77;
                break;
            case 78:
                result = successRate78;
                break;
            default:
                result = successRate78;
                break;
        }
        return result;
    }

}
