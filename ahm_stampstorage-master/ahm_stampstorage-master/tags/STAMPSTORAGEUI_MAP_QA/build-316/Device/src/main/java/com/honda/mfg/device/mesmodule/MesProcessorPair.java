package com.honda.mfg.device.mesmodule;

import com.honda.mfg.device.MessageRequestProcessor;

/**
 * User: vcc30690
 * Date: 4/11/11
 */
public interface MesProcessorPair {
    public MessageRequestProcessor getRequestProcessor();

    public long getInitializeRequiredTime();

    public String getMessageSeparatorCharacter();
}
