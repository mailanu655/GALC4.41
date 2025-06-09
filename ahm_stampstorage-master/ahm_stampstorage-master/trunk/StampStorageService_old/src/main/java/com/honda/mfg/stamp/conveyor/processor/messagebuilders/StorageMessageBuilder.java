package com.honda.mfg.stamp.conveyor.processor.messagebuilders;

import com.honda.mfg.stamp.conveyor.messages.Message;
import com.honda.mfg.stamp.conveyor.processor.messagebuilders.exceptions.UnknownStorageMessageException;
import com.honda.mfg.stamp.conveyor.processor.messagebuilders.json.JSonResponseParser;


/**
 * User: vcc30690
 * Date: 3/21/11
 */
public class StorageMessageBuilder {

    public Message buildStorageMessage(String jsonResponse) {
        Message deviceMsg = null;
        MessageConfig messageConfig = MessageConfig.getInstance();
        try {
            JSonResponseParser parser = new JSonResponseParser();
            for (String msgType : messageConfig.getDeviceMessageTypes()) {
                if (jsonResponse.contains(msgType)) {
                    String className = messageConfig.getDeviceClass(msgType);
                    Class clazz = Class.forName(className);
                    deviceMsg = (Message) parser.parse(jsonResponse, clazz);
                    break;
                }
            }
        } catch (Exception e) {
            throw new UnknownStorageMessageException(e.getMessage(), e.getCause());
        }
        return deviceMsg;
    }
}
