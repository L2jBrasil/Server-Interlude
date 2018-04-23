package com.l2jbr.gameserver.model.database;

import com.l2jbr.commons.database.annotation.Table;
import org.springframework.data.annotation.Id;

@Table("auto_chat")
public class AutoChat  {

    @Id
    private Integer groupId;
    private Integer npcId;
    private Long chatDelay;

    public Integer getGroupId() {
        return groupId;
    }

    public Integer getNpcId() {
        return npcId;
    }

    public Long getChatDelay() {
        return chatDelay;
    }
}
