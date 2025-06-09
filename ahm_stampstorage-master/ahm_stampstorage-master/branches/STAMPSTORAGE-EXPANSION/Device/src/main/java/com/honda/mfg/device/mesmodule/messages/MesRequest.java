package com.honda.mfg.device.mesmodule.messages;

import com.honda.mfg.device.mesmodule.MesMessageSeparators;
import com.honda.mfg.device.messages.MessageRequest;

/**
 * User: vcc30690
 * Date: 4/11/11
 */
public class MesRequest implements MessageRequest {

    private String message;
    private String messageSeparatorCharacter;

    public MesRequest(String msg) {
        this(msg, MesMessageSeparators.DEFAULT.getSeparator());
    }

    private MesRequest(String msg, String messageSeparatorCharacter) {
        this.message = msg;
        this.messageSeparatorCharacter = messageSeparatorCharacter;
    }

    public MesRequest(MesMessage message) {
        this(message, MesMessageSeparators.DEFAULT.getSeparator());
    }

    public MesRequest(MesMessage message, String messageSeparatorCharacter) {
        this(message.getMessage(), messageSeparatorCharacter);
    }

    @Override
    public String getMessageRequest() {
        return message + messageSeparatorCharacter;
    }
}
