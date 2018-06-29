package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.database.model.Entity;
import org.springframework.data.annotation.Id;

@Table("character_quests")
public class CharacterQuests extends Entity<Integer> {

    @Id
    @Column("char_id")
    private int charId;
    private String name;
    private String var;
    private String value;
    @Column("class_index")
    private int classIndex;

    public CharacterQuests() {}

    public CharacterQuests(int objectId, String questName, String var, String value) {
        this.charId = objectId;
        this.name  = questName;
        this.var = var;
        this.value = value;
    }

    @Override
    public Integer getId() {
        return charId;
    }

    public String getName() {
        return name;
    }

    public String getVar() {
        return var;
    }

    public String getValue() {
        return value;
    }

    public int getClassIndex() {
        return classIndex;
    }
}
