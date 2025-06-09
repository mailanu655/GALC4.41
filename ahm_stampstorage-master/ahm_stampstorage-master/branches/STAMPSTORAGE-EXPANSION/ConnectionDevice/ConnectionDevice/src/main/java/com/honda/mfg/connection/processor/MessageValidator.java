package com.honda.mfg.connection.processor;

import com.honda.mfg.connection.exceptions.IncompleteMesMessageException;

/**
 * User: Jeffrey M Lutz
 * Date: 5/25/11
 */
public class MessageValidator {
    private static final String MESSAGE_START_STRING = "\"GENERALMESSAGE\"";

    public MessageValidator() {
        super();
    }

    public boolean isAlreadyWrapped(String msg) {
        boolean retVal;
        try {
            retVal = cleanMessage(msg) != null;
        } catch (IncompleteMesMessageException e) {
            retVal = false;
        }
        return retVal;
    }

    public String cleanMessage(String message) {
        int startIndex = getMessageStartIndex(message);
        int endIndex = getMessageEndIndex(message);
        if ((startIndex < 0 || endIndex < 0) || (endIndex < startIndex)) {
            throw new IncompleteMesMessageException("Incomplete message: " + message);
        }
        return message.substring(startIndex, endIndex);
    }

    private int getMessageStartIndex(String msg) {
        int idx = msg == null ? -1 : msg.toUpperCase().indexOf(MESSAGE_START_STRING);
        idx--;
        while (idx > -1) {
            char ch = msg.charAt(idx);
            if (ch == '{') {
                return idx;
            }
            if (ch != ' ') {
                return -1;
            }
            idx--;
        }
        return idx;
    }

    private int getMessageEndIndex(String msg) {
        return msg == null ? -1 : msg.length();
    }
}
