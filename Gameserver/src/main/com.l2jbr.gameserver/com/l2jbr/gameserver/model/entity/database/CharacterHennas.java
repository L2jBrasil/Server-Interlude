package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.database.model.Entity;
import org.springframework.data.annotation.Id;

@Table("character_hennas")
public class CharacterHennas extends Entity<Integer> {

    @Id
    @Column("char_obj_id")
    private int charObjId;
    @Column("symbol_id")
    private int symbolId;
    private int slot;
    @Column("class_index")
    private int classIndex;

    public  CharacterHennas() {}

    public CharacterHennas(int objectId, int symbolId, int classIndex, int slot) {
        this.charObjId = objectId;
        this.symbolId = symbolId;
        this.classIndex = classIndex;
        this.slot = slot;
    }

    @Override
    public Integer getId() {
        return charObjId;
    }

    public int getSymbolId() {
        return symbolId;
    }

    public int getSlot() {
        return slot;
    }

    public int getClassIndex() {
        return classIndex;
    }
}
