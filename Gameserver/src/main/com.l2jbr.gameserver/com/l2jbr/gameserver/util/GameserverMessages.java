package com.l2jbr.gameserver.util;

import com.l2jbr.commons.util.Messages;


public class GameserverMessages {

    public static String getMessage(String key, Object... arguments) {
        return Messages.getMessage("gameserver-messages", key, arguments);

    }
}
