package com.honda.mfg.device.mesmodule.messages;

import com.honda.mfg.device.mesmodule.MessageValidator;

/**
 * User: vcc30690
 * Date: 4/15/11
 */
public class MesMessageBase implements MesMessage {
    private String message;

    public MesMessageBase(String message) {
        MessageValidator msgValidator = new MessageValidator();
        if (msgValidator.isAlreadyWrapped(message)) {
            this.message = message;
        } else {
            this.message = "{\"GeneralMessage\":" + message + "}";
        }
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String toString() {
        return message;
    }
}
