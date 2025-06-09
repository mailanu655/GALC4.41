package com.honda.mfg.connection.processor.messages;

import com.honda.mfg.connection.messages.MessageRequest;
import com.honda.mfg.connection.processor.ConnectionMessageSeparators;

/**
 * User: vcc30690
 * Date: 4/11/11
 */
public class ConnectionRequest implements MessageRequest {

    private String message;
    private String messageSeparatorCharacter;

    public ConnectionRequest(String msg) {
        this(msg, ConnectionMessageSeparators.DEFAULT.getSeparator());
    }

    private ConnectionRequest(String msg, String messageSeparatorCharacter) {
        this.message = msg;
        this.messageSeparatorCharacter = messageSeparatorCharacter;
    }

    public ConnectionRequest(ConnectionMessage message) {
        this(message, ConnectionMessageSeparators.DEFAULT.getSeparator());
    }

    public ConnectionRequest(ConnectionMessage message, String messageSeparatorCharacter) {
        this(message.getMessage(), messageSeparatorCharacter);
    }

    @Override
    public String getMessageRequest() {
        return message + messageSeparatorCharacter;
    }
}
