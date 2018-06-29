package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.database.model.Entity;
import com.l2jbr.gameserver.model.L2Skill;
import org.springframework.data.annotation.Id;

@Table("clan_skills")
public class ClanSkills extends Entity<Integer> {
    @Id
    @Column("clan_id")
    private int clanId;
    @Column("skill_id")
    private int skillId;
    @Column("skill_level")
    private int skillLevel;
    @Column("skill_name")
    private String skillName;

    public  ClanSkills() {}

    public ClanSkills(int clanId, L2Skill newSkill) {
        this.clanId = clanId;
        this.skillId = newSkill.getId();
        this.skillLevel = newSkill.getLevel();
        this.skillName = newSkill.getName();
    }

    @Override
    public Integer getId() {
        return clanId;
    }

    public int getSkillId() {
        return skillId;
    }

    public int getSkillLevel() {
        return skillLevel;
    }

    public String getSkillName() {
        return skillName;
    }
}
