package com.l2jbr.gameserver.model.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import org.springframework.data.annotation.Id;

@Table("enchant_skill_trees")
public class EnchantSkillTrees  {
    @Id
    @Column("skill_id")
    private int skillId;
    private int level;
    private String name;
    @Column("base_lvl")
    private int baseLvl;
    @Column("enchant_type")
    private String enchantType;
    private int sp;
    private int exp;
    @Column("min_skill_lvl")
    private int minSkillLvl;
    @Column("success_rate76")
    private byte successRate76;
    @Column("success_rate77")
    private byte successRate77;
    @Column("success_rate78")
    private byte successRate78;

    public int getSkillId() {
        return skillId;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public int getBaseLvl() {
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

    public int getMinSkillLvl() {
        return minSkillLvl;
    }

    public byte getSuccessRate76() {
        return successRate76;
    }

    public byte getSuccessRate77() {
        return successRate77;
    }

    public byte getSuccessRate78() {
        return successRate78;
    }
}
