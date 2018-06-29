package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.database.model.Entity;
import org.springframework.data.annotation.Id;

@Table("character_shortcuts")
public class CharacterShortcuts extends Entity<Integer> {

    @Id
    @Column("char_obj_id")
    private int charObjId;
    private int slot;
    private int page;
    private int type;
    @Column("shortcut_id")
    private int shortcutId;
    private int level;
    @Column("class_index")
    private int classIndex;

    @Override
    public Integer getId() {
        return charObjId;
    }

    public int getSlot() {
        return slot;
    }

    public int getPage() {
        return page;
    }

    public int getType() {
        return type;
    }

    public int getShortcutId() {
        return shortcutId;
    }

    public int getLevel() {
        return level;
    }

    public int getClassIndex() {
        return classIndex;
    }
}
