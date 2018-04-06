package com.l2jbr.commons.util;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class Messages {

    private static Logger _log = Logger.getLogger(Messages.class.getName());

    private static Map<String, ResourceBundle> messagesBundle = new HashMap<>();
    private static Locale locale = Locale.getDefault();
    private static MessageFormat formatter = new MessageFormat("", locale);

    public static String getMessage(String resource, String messageKey, Object... arguments) {
        ResourceBundle messages = getMessageFromBundle(resource);
        String message =  messages.getString(messageKey);
        formatter.applyPattern(message);
        return formatter.format(arguments);
    }

    private static ResourceBundle getMessageFromBundle(String resource) {
        ResourceBundle  messages = messagesBundle.get(resource);
        if(messages == null) {
            messages = initializeResource(resource);
            messagesBundle.put(resource, messages);
        }
        return  messages;
    }

    private static ResourceBundle initializeResource(String resource) {
        return ResourceBundle.getBundle(resource, locale);
    }

}
