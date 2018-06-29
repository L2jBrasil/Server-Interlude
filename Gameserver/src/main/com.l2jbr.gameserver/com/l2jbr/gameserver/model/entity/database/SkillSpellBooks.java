package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import org.springframework.data.annotation.Id;

@Table("skill_spellbooks")
public class SkillSpellBooks  {
    @Id
    @Column("skill_id")
    private int skillId;
    @Column("item_id")
    private int itemId;

    public int getSkillId() {
        return skillId;
    }

    public int getItemId() {
        return itemId;
    }
}
