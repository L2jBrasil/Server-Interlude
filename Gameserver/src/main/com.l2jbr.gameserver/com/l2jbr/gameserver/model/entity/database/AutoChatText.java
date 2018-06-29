package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Table;

@Table("auto_chat_text")
public class AutoChatText  {

    private int groupId;
    private String chatText;

    public String getChatText() {
        return chatText;
    }
}
