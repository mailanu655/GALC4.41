package com.honda.mfg.device.rfid.etherip;

import com.honda.mfg.device.exceptions.CommunicationsException;
import com.honda.mfg.device.exceptions.NetworkCommunicationException;
import com.honda.mfg.device.exceptions.ResponseTimeoutException;
import com.honda.mfg.device.exceptions.UnknownResponseException;
import com.honda.mfg.device.rfid.etherip.messages.RfidResponse;
import org.bushe.swing.event.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RfidResponseProcessor implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(RfidResponseProcessor.class);
    private RfidResponseBuilder responseBuilder;
    private RfidResponseReader responseReader;
    private volatile boolean connected;

    private volatile boolean runOnce;

    public RfidResponseProcessor(RfidResponseReader responseReader, RfidResponseBuilder responseBuilder) {
        this(responseReader, responseBuilder, false);
    }

    public RfidResponseProcessor(RfidResponseReader responseReader, RfidResponseBuilder responseBuilder, boolean runOnce) {
        this.responseReader = responseReader;
        this.responseBuilder = responseBuilder;
        this.runOnce = runOnce;
        this.connected = true;
    }

    private String getResponse() {
        return responseReader.getResponse();
    }

    public void run() {
        LOG.info("Starting response processors. Thread id: " + Thread.currentThread().getId());
        boolean successfulFirstPass = false;
        while (connected) {
            try {
                processResponse();
                if (!successfulFirstPass) {
                    LOG.info("Completed first response processor cycle.");
                    successfulFirstPass = true;
                }
            } catch (ResponseTimeoutException ex) {
                LOG.trace("Response timeout occurred. MSG: " + ex.getMessage());
            } catch (CommunicationsException e) {
                LOG.error("Communication Exception. MSG: " + e.getMessage());
                connected = false;
            } catch (NetworkCommunicationException e) {
                LOG.error("Network Communication Exception MSG: " + e.getMessage());
                connected = false;
            }
            if (runOnce) {
                break;
            }
        }
    	LOG.error("RFID CONNECTION ISSUE - App Monitor Restart Enabled.");
        LOG.error("Stopped Response Processor stopProcessing event received. Thread ID: " + Thread.currentThread().getId());
    }

    boolean isRunning() {
        return connected;
    }

    private void processResponse() {
        LOG.trace("Start processResponse()");
        String response = getResponse();
        RfidResponse rfidResponse;
        try {
            rfidResponse = responseBuilder.buildRfidResponse(response);
            EventBus.publish(rfidResponse);
        } catch (UnknownResponseException e) {
            LOG.warn("Ignoring received unknown response.");
        }
    }
}