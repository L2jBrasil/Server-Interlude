package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.database.model.Entity;

@Table("character_recipebook")
public class CharacterRecipeBook extends Entity<Integer> {

    @Column("char_id")
    private int charId;
    private int id;
    private int type;

    public CharacterRecipeBook() {}

    public CharacterRecipeBook(int objectId, int id, int type) {
        this.charId = objectId;
        this.id = id;
        this.type = type;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public int getType() {
        return type;
    }
}

