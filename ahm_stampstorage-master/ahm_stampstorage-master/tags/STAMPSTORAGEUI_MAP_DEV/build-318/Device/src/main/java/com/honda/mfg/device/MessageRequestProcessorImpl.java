package com.honda.mfg.device;

import com.honda.mfg.device.exceptions.CommunicationsException;
import com.honda.mfg.device.messages.MessageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * User: Mihail Chirita
 * Date: Sep 27, 2010
 * Time: 11:45:52 AM
 */
public class MessageRequestProcessorImpl implements MessageRequestProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(MessageRequestProcessorImpl.class);

    private BufferedWriter writer;

    public MessageRequestProcessorImpl(BufferedWriter writer) {
        this.writer = writer;
    }

    @Override
    public void sendRequest(MessageRequest messageRequest) {
        try {
            String request = messageRequest.getMessageRequest();
            LOG.trace("Sending Message request: " + messageRequest.getClass().getSimpleName() + ":  " + messageRequest.getMessageRequest());
            writer.write(request);
            writer.flush();
        } catch (IOException ex) {
            throw new CommunicationsException("Unable to write message ", ex);
        }
    }
}
