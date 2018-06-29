package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.database.model.Entity;
import org.springframework.data.annotation.Id;

@Table("character_skills")
public class CharacterSkills extends Entity<Integer> {

    @Id
    @Column("char_obj_id")
    private int charObjId;
    @Column("skill_id")
    private int skillId;
    @Column("skill_level")
    private int skillLevel;
    @Column("skill_name")
    private String skillName;
    @Column("class_index")
    private int classIndex;

    public CharacterSkills() {}

    public CharacterSkills(int charObjectId, int skillId, int skillLevel, String skillName, int classIndex) {
        this.charObjId = charObjectId;
        this.skillId = skillId;
        this.skillLevel = skillLevel;
        this.skillName = skillName;
        this.classIndex = classIndex;
    }

    @Override
    public Integer getId() {
        return charObjId;
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

    public int getClassIndex() {
        return classIndex;
    }
}
