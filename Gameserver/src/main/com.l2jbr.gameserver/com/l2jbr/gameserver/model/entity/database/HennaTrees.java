package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import org.springframework.data.annotation.Id;

@Table("henna_trees")
public class HennaTrees  {

    @Id
    @Column("class_id")
    private int classId;
    @Column("symbol_id")
    private int symbolId;

    public int getClassId() {
        return classId;
    }

    public int getSymbolId() {
        return symbolId;
    }
}
