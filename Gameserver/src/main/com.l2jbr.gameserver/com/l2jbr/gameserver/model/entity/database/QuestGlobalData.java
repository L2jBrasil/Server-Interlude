package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import org.springframework.data.annotation.Id;

@Table("quest_global_data")
public class QuestGlobalData  {

    @Id
    @Column("quest_name")
    private String questName;
    private String var;
    private String value;
}
