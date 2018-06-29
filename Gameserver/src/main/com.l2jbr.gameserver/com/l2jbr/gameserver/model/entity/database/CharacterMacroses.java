package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.database.model.Entity;
import com.l2jbr.gameserver.model.L2Macro;
import org.springframework.data.annotation.Id;

@Table("character_macroses")
public class CharacterMacroses extends Entity<Integer> {

    @Id
    @Column("char_obj_id")
    private int charObjId;
    private int id;
    private int icon;
    private String name;
    private String descr;
    private String acronym;
    private String commands;

    public CharacterMacroses() {}

    public CharacterMacroses(int objectId, L2Macro macro) {
        this.charObjId = objectId;
        this.id = macro.id;
        this.icon = macro.icon;
        this.name = macro.name;
        this.acronym = macro.acronym;
        buildCommands(macro);

    }

    private void buildCommands(L2Macro macro) {
        StringBuilder sb = new StringBuilder();
        for (L2Macro.L2MacroCmd cmd : macro.commands) {
            sb.append(cmd.type).append(',');
            sb.append(cmd.d1).append(',');
            sb.append(cmd.d2);
            if ((cmd.cmd != null) && (cmd.cmd.length() > 0)) {
                sb.append(',').append(cmd.cmd);
            }
            sb.append(';');
        }
        this.commands = sb.toString();
    }

    @Override
    public Integer getId() {
        return charObjId;
    }

    public int getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public String getDescr() {
        return descr;
    }

    public String getAcronym() {
        return acronym;
    }

    public String getCommands() {
        return commands;
    }
}
