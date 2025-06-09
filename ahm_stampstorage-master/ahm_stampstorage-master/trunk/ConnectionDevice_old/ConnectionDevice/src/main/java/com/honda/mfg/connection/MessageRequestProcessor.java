package com.honda.mfg.connection;

import com.honda.mfg.connection.messages.MessageRequest;

/**
 * User: Jeffrey M Lutz
 * Date: 4/12/11
 */
public interface MessageRequestProcessor {
    void sendRequest(MessageRequest messageRequest);
}
