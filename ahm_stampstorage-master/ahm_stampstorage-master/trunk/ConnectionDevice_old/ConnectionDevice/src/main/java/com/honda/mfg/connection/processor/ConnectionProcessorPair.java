package com.honda.mfg.connection.processor;

import com.honda.mfg.connection.MessageRequestProcessor;

/**
 * User: vcc30690
 * Date: 4/11/11
 */
public interface ConnectionProcessorPair {
   MessageRequestProcessor getRequestProcessor();

    long getInitializeRequiredTime();

    String getMessageSeparatorCharacter();
}
