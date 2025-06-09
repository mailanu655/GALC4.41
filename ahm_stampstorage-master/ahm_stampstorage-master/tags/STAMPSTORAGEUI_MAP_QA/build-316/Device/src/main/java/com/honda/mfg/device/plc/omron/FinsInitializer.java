package com.honda.mfg.device.plc.omron;

import com.honda.mfg.device.exceptions.CommunicationsException;
import com.honda.mfg.device.exceptions.NetworkCommunicationException;
import com.honda.mfg.device.plc.omron.messages.FinsInitializeRequest;
import com.honda.mfg.device.plc.omron.messages.FinsInitializeResponse;
import com.honda.mfg.schedule.Scheduler;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.bushe.swing.event.annotation.ReferenceStrength;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Jeffrey M Lutz
 * Date: 4/8/11
 */
public class FinsInitializer implements Runnable, FinsInitializeState {
    private static final Logger LOG = LoggerFactory.getLogger(FinsInitializer.class);
    static final int INVALID_NODE_NUMBER = -1;

    private FinsProcessorPair finsProcessorPair;

    private volatile int sourceNode = INVALID_NODE_NUMBER;
    private volatile int destinationNode = INVALID_NODE_NUMBER;

    private volatile long previousInitializeRequiredTime;
    private volatile long nextInitializeRequiredTime;
    private volatile boolean initialized;
    private volatile boolean attemptingToInitialize;

    public FinsInitializer(FinsProcessorPair finsProcessorPair, int initializeIntervalSecs) {
        this(finsProcessorPair);
        startInitializeThread(initializeIntervalSecs);
    }

    FinsInitializer(FinsProcessorPair finsProcessorPair) {
        AnnotationProcessor.process(this);
        this.finsProcessorPair = finsProcessorPair;
        this.previousInitializeRequiredTime = 0L;
        this.initialized = false;
    }

    private void startInitializeThread(int initializeIntervalSecs) {
        new Scheduler(this, initializeIntervalSecs, this.getClass().getSimpleName());
    }

    @Override
    public int getSourceNode() {
        return sourceNode;
    }

    @Override
    public int getDestinationNode() {
        return destinationNode;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @EventSubscriber(eventClass = FinsInitializeResponse.class, referenceStrength = ReferenceStrength.STRONG)
    public void receiveInitializeResponse(FinsInitializeResponse response) {
        LOG.info("Received initialize response!");
        setInitialized(response);
    }

    public void run() {
        long currentInitializeRequiredTime = finsProcessorPair.getInitializeRequiredTime();
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
            LOG.debug("Initialization is not required at this time.");
            attemptingToInitialize = false;
            return;
        }
        LOG.info("#########   Sending initialize request.");
        sendInitializeRequest();
        attemptingToInitialize = false;
        LOG.info("#########  Done initializing.  isInitialized()? " + isInitialized());
    }

    private void sendInitializeRequest() {
        FinsInitializeRequest initializeRequest = new FinsInitializeRequest();
        try {
            finsProcessorPair.getRequestProcessor().sendRequest(initializeRequest);
            LOG.info("Able to get request processor and send initialize request.");
        } catch (CommunicationsException e) {
            LOG.error("Unable to connect.  MSG: CommunicationsException:       " + e.getMessage());
        } catch (NetworkCommunicationException e) {
            LOG.error("Unable to connect.  MSG: NetworkCommunicationException: " + e.getMessage());
        }
    }

    private void setInitialized(FinsInitializeResponse response) {
        sourceNode = response.getSourceNode();
        destinationNode = response.getDestinationNode();
        initialized = true;
        previousInitializeRequiredTime = nextInitializeRequiredTime;
    }

    private void setUninitialized() {
        sourceNode = INVALID_NODE_NUMBER;
        destinationNode = INVALID_NODE_NUMBER;
        initialized = false;
    }
}
