package com.honda.mfg.device.plc.omron;

import com.honda.mfg.device.exceptions.CommunicationsException;
import com.honda.mfg.device.exceptions.NetworkCommunicationException;
import com.honda.mfg.device.exceptions.ResponseTimeoutException;
import com.honda.mfg.device.exceptions.UnknownResponseException;
import com.honda.mfg.device.plc.omron.messages.FinsResponse;
import org.bushe.swing.event.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class FinsResponseProcessor implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(FinsResponseProcessor.class);

    private FinsResponseBuilder responseBuilder;
    private FinsResponseReader responseReader;
    private volatile boolean connected;

    private volatile boolean runOnce;
    private boolean recordSequence;
    private List<Object> runSequence = new ArrayList<Object>();

    public FinsResponseProcessor(FinsResponseReader responseReader, FinsResponseBuilder responseBuilder) {
        this(responseReader, responseBuilder, false);
    }

    FinsResponseProcessor(FinsResponseReader responseReader, FinsResponseBuilder responseBuilder, boolean runOnce) {
        this(responseReader, responseBuilder, runOnce, false);
    }

    FinsResponseProcessor(FinsResponseReader responseReader, FinsResponseBuilder responseBuilder, boolean runOnce, boolean recordSequence) {
        this.responseReader = responseReader;
        this.responseBuilder = responseBuilder;
        this.recordSequence = recordSequence;
        this.runOnce = runOnce;
        this.connected = true;
    }

    public void run() {
        LOG.info("Starting response processor. Thread id: " + Thread.currentThread().getId() + " --> runOnce? " + runOnce);
        boolean successfulFirstPass = false;
        while (connected) {
            try {
                LOG.trace("In loop.  connected? " + connected);
                processResponse();
                if (!successfulFirstPass) {
                    LOG.info("Complete first response processor cycle.");
                    successfulFirstPass = true;
                }
            } catch (ResponseTimeoutException ex) {
                recordItem(ex);
                LOG.trace("Response timeout occurred.  MSG: " + ex.getMessage());
            } catch (CommunicationsException e) {
                recordItem(e);
                LOG.error("Communications error. MSG: " + e.getMessage());
                connected = false;
            } catch (NetworkCommunicationException e) {
                recordItem(e);
                LOG.error("Network error. MSG: " + e.getMessage());
                connected = false;
            } catch (Throwable e) {
                LOG.error("Unknown error.", e);
                connected = false;
            }
            if (runOnce) {
                break;
            }
        }
        LOG.warn("Stopped Response Processor since communications failed. Thread ID: " + Thread.currentThread().getId());
    }

    boolean isRunning() {
        return connected;
    }

    List<Object> getSequence() {
        return runSequence;
    }

    private void recordItem(Object item) {
        if (recordSequence) {
            runSequence.add(item);
        }
    }

    void processResponse() {
        LOG.trace("Getting response().");
        String response = getResponse();
        LOG.trace("Response received: " + response);
        FinsResponse finsResponse;
        try {
            LOG.trace("Creating FINS response.");
            finsResponse = responseBuilder.buildFinsResponse(response);
            LOG.trace("Created FINS response:  " + finsResponse.getClass().getSimpleName());
            recordItem(finsResponse);
            LOG.trace("Publishing FINS response:  " + finsResponse.getClass().getSimpleName());
            // todo Need to determine issue with hang.
            LOG.trace("Thread count: " + Thread.activeCount());
            EventBus.publish(finsResponse);
            LOG.trace("Finished publish() call.");
        } catch (UnknownResponseException e) {
            LOG.warn("Ignoring received unknown response.", e);
        } catch (RuntimeException e) {
            LOG.error("Received fatal error.", e);
            throw e;
        }
        LOG.trace("Returning from processResponse() method.");
    }

    private String getResponse() {
        LOG.trace("Attempting to read response.");
        return responseReader.getResponse();
    }

    void setConnected(boolean connected) {
        this.connected = connected;
    }
}