package com.honda.mfg.device.rfid.etherip;

import com.honda.mfg.device.exceptions.CommunicationsException;
import com.honda.mfg.device.exceptions.NetworkCommunicationException;
import com.honda.mfg.device.messages.MessageRequest;
import com.honda.mfg.device.rfid.etherip.messages.*;
import com.honda.mfg.schedule.Scheduler;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.bushe.swing.event.annotation.ReferenceStrength;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Jeffrey M Lutz
 * Date: 4/10/11
 */
public class EtherIpInitializer implements Runnable, EtherIpInitializeState {
    private static final Logger LOG = LoggerFactory.getLogger(EtherIpInitializer.class);

    private RfidProcessorPair rfidProcessorPair;

    private volatile long previousInitializeRequiredTime;
    private volatile boolean initialized;
    private volatile int responses;
    private volatile long nextInitializeRequiredTime;
    private volatile boolean attemptingToInitialize;

    public EtherIpInitializer(RfidProcessorPair rfidProcessorPair, int initializeIntervalSec) {
        this(rfidProcessorPair);
        startInitializeThread(initializeIntervalSec);
    }

    EtherIpInitializer(RfidProcessorPair rfidProcessorPair) {
        AnnotationProcessor.process(this);
        this.rfidProcessorPair = rfidProcessorPair;
        this.previousInitializeRequiredTime = 0L;
        this.initialized = false;
    }

    private void startInitializeThread(int initializeIntervalSec) {
        LOG.info("Initialize interval: " + initializeIntervalSec + " (sec)");
        new Scheduler(this, initializeIntervalSec, this.getClass().getSimpleName());
    }

    @EventSubscriber(eventClass = RfidClearPendingResponsesResponse.class, referenceStrength = ReferenceStrength.STRONG)
    public void receiveRfidResponse(RfidClearPendingResponsesResponse response) {
        LOG.info("Initialize step 1 of 3!  RESPONSE: " + response.getClass().getSimpleName());
        responses++;
    }

    @EventSubscriber(eventClass = RfidGetControllerConfigurationResponse.class, referenceStrength = ReferenceStrength.STRONG)
    public void receiveRfidResponse(RfidGetControllerConfigurationResponse response) {
        LOG.info("Initialize step 2 of 3!  RESPONSE: " + response.getClass().getSimpleName());
        responses++;
    }

    @EventSubscriber(eventClass = RfidGetControllerInfoResponse.class, referenceStrength = ReferenceStrength.STRONG)
    public void receiveRfidResponse(RfidGetControllerInfoResponse response) {
        if (!isInitialized()) {
            LOG.info("Initialize step 3 of 3!  RESPONSE: " + response.getClass().getSimpleName());
            LOG.info("Received initialize response!");
            setInitialized();
            responses++;
        }
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void run() {
        long currentInitializeRequiredTime = rfidProcessorPair.getInitializeRequiredTime();
        LOG.trace("Running():  time: " + currentInitializeRequiredTime);
        if (attemptingToInitialize) {
            return;
        }
        if (currentInitializeRequiredTime < 1) {
            // Wait for response processor to start.
            // Don't try to send anything out the request processor.
            LOG.info("Initialization is required.");
            setUninitialized();
            return;
        }
        attemptingToInitialize = true;
        if (currentInitializeRequiredTime > previousInitializeRequiredTime) {
            // Need to set initialized = false since initializeRequiredTime changed
            LOG.info("Initialization is required.");
            nextInitializeRequiredTime = currentInitializeRequiredTime;
            setUninitialized();
        }
        if (isInitialized()) {
            LOG.trace("Initialization is not required at this time.");
            attemptingToInitialize = false;
            return;
        }
        LOG.info("#########   Sending initialize request.");
        sendInitializeRequest();
        attemptingToInitialize = false;
        LOG.info("#########  Done initializing.  isInitialized()? " + isInitialized());
    }

    private void clearResponses() {
        responses = 0;
    }

    private void sendInitializeRequest() {
        clearResponses();
        try {
            LOG.info("Initialize step 1 of 3!  REQUEST: RfidClearPendingResponsesRequest");
            sendRequest(new RfidClearPendingResponsesRequest());
            while (responses < 1) {
            }
            LOG.info("Initialize step 2 of 3!  REQUEST: RfidGetControllerConfigurationRequest");
            sendRequest(new RfidGetControllerConfigurationRequest());
            while (responses < 2) {
            }
            LOG.info("Initialize step 3 of 3!  REQUEST: RfidGetControllerInfoRequest");
            sendRequest(new RfidGetControllerInfoRequest());
            while (responses < 3) {
            }

        } catch (CommunicationsException e) {
            LOG.error("Unable to connect.  MSG: CommunicationsException:       " + e.getMessage());
        } catch (NetworkCommunicationException e) {
            LOG.error("Unable to connect.  MSG: NetworkCommunicationException: " + e.getMessage());
        }
    }

    private void sendRequest(MessageRequest request) {
        rfidProcessorPair.getRequestProcessor().sendRequest(request);
    }

    private void setInitialized() {
        initialized = true;
        previousInitializeRequiredTime = nextInitializeRequiredTime;
    }

    private void setUninitialized() {
        initialized = false;
    }
}
