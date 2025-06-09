package com.honda.mfg.device;

import com.honda.mfg.device.messages.MessageRequest;

/**
 * User: Jeffrey M Lutz
 * Date: 4/12/11
 */
public interface MessageRequestProcessor {
    void sendRequest(MessageRequest messageRequest);
}
