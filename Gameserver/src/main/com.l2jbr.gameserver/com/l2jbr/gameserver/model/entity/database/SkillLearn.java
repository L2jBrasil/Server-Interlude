package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import org.springframework.data.annotation.Id;

@Table("skill_learn")
public class SkillLearn  {

    @Id
    @Column("npc_id")
    private int npcId;
    @Column("class_id")
    private int classId;

    public int getNpcId() {
        return npcId;
    }

    public int getClassId() {
        return classId;
    }
}
