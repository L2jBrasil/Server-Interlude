package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.database.model.Entity;
import com.l2jbr.gameserver.model.L2Effect;
import org.springframework.data.annotation.Id;

@Table("character_skills_save")
public class CharacterSkillsSave extends Entity<Integer> {

    @Id
    @Column("char_obj_id")
    private int charObjId;
    @Column("skill_id")
    private int skillId;
    @Column("skill_level")
    private int skillLevel;
    @Column("effect_count")
    private int effectCount;
    @Column("effect_cur_time")
    private int effectCurTime;
    @Column("reuse_delay")
    private long reuseDelay;
    @Column("restore_type")
    private int restoreType;
    @Column("class_index")
    private int classIndex;
    @Column("buff_index")
    private int buffIndex;

    public  CharacterSkillsSave() {}

    public CharacterSkillsSave(int objectId, int skillId, int skillLevel, int effectCount, int effectCurTime, long reuseDelay, int restoreType, int classIndex, int buffIndex) {
        this.charObjId = objectId;
        this.skillId = skillId;
        this.skillLevel = skillLevel;
        this.effectCount = effectCount;
        this.effectCurTime = effectCurTime;
        this.reuseDelay = reuseDelay;
        this.restoreType = restoreType;
        this.classIndex = classIndex;
        this.buffIndex = buffIndex;
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

    public int getEffectCount() {
        return effectCount;
    }

    public int getEffectCurTime() {
        return effectCurTime;
    }

    public long getReuseDelay() {
        return reuseDelay;
    }

    public int getRestoreType() {
        return restoreType;
    }

    public int getClassIndex() {
        return classIndex;
    }

    public int getBuffIndex() {
        return buffIndex;
    }
}
