package com.l2jbr.gameserver.model.database;

import com.l2jbr.commons.database.annotation.Table;

@Table("etcitem")
public class EtcItem extends ItemTemplate {

    private String item_type;
    private String consume_type;
    private String oldname;
    private String oldtype;

    public String getItemType() {
        return item_type;
    }

    public String getConsumeType() {
        return consume_type;
    }

    public String getOldname() {
        return oldname;
    }

    public String getOldtype() {
        return oldtype;
    }
}
