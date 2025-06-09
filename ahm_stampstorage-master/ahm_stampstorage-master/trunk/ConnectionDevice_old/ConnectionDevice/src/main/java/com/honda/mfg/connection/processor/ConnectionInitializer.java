package com.honda.mfg.connection.processor;

import com.honda.mfg.connection.MessageRequestProcessor;
import com.honda.mfg.connection.exceptions.CommunicationsException;
import com.honda.mfg.connection.exceptions.NetworkCommunicationException;
import com.honda.mfg.connection.messages.MessageRequest;
import com.honda.mfg.connection.processor.messages.ConnectionInitialized;
import com.honda.mfg.connection.processor.messages.ConnectionUninitialized;
import com.honda.mfg.connection.processor.messages.ConnectionRequest;
import com.honda.mfg.connection.processor.messages.PingMessage;
import com.honda.mfg.schedule.Scheduler;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.bushe.swing.event.annotation.ReferenceStrength;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: vcc30690
 * Date: 4/11/11
 */
public class ConnectionInitializer implements Runnable, ConnectionInitializeState {
    private static final Logger LOG = LoggerFactory.getLogger(ConnectionInitializer.class);

    private ConnectionProcessorPair connectionProcessorPair;

    private volatile long previousInitializeRequiredTime;
    private volatile long nextInitializeRequiredTime;
    private volatile boolean initialized;

    private Scheduler scheduler;

    public ConnectionInitializer(ConnectionProcessorPair connectionProcessorPair, int initializeIntervalSecs) {
        this(connectionProcessorPair);
        LOG.debug("#########  ConnectionInitializer() called...");
        startInitializeThread(initializeIntervalSecs);
    }

    ConnectionInitializer(ConnectionProcessorPair connectionProcessorPair) {
        AnnotationProcessor.process(this);
        this.connectionProcessorPair = connectionProcessorPair;
        this.previousInitializeRequiredTime = 0L;
        this.initialized = false;
    }

    private void startInitializeThread(int initializeIntervalSecs) {
        scheduler = new Scheduler(this, initializeIntervalSecs, this.getClass().getSimpleName());
    }

	public void shutdown() {
        if (scheduler != null) {
            scheduler.shutdown();
        }
        AnnotationProcessor.unprocess(this);
    }

    public boolean isInitialized() {
        return initialized;
    }

    @EventSubscriber(eventClass = PingMessage.class, referenceStrength = ReferenceStrength.STRONG)
    public void receiveInitializeResponse(PingMessage responseMessage) {
        LOG.trace("Received initialize response initialize message!");
        setInitialized();
    }

    public void run() {
        long currentInitializeRequiredTime = connectionProcessorPair.getInitializeRequiredTime();
        LOG.debug("currentInitializeRequiredTime:  " + currentInitializeRequiredTime);
        if (currentInitializeRequiredTime < 1) {
            // Wait for response processor to start.
            // Don't try to send anything out the request processor.
            LOG.debug("Initialization is required.");
            setUninitialized();
            return;
        }
        if (currentInitializeRequiredTime > previousInitializeRequiredTime) {
            // Need to set initialized = false since initializeRequiredTime changed
            LOG.debug("Initialization is required.");
            nextInitializeRequiredTime = currentInitializeRequiredTime;
            setUninitialized();
        }
        if (isInitialized()) {
            LOG.debug("Initialization is not required at this time.");
            return;
        }
        // Need to re-initialize because initialized == true
        LOG.debug("Sending initialize request now.");
        sendInitializeRequest();
    }

    private void sendInitializeRequest() {
        MessageRequest initializeRequest = new ConnectionRequest(new PingMessage());
        try {
            MessageRequestProcessor requestProcessor = connectionProcessorPair.getRequestProcessor();
            requestProcessor.sendRequest(initializeRequest);
            LOG.debug("Able to get request processor and send initialize request.");
        } catch (CommunicationsException e) {
            LOG.error("Unable to connect.  MSG: CommunicationsException:       " + e.getMessage());
        } catch (NetworkCommunicationException e) {
            LOG.error("Unable to connect.  MSG: NetworkCommunicationException: " + e.getMessage());
        }
    }

    private void setInitialized() {
        boolean prevState = initialized;
        initialized = true;
        if (prevState == false && initialized == true) {
            previousInitializeRequiredTime = nextInitializeRequiredTime;
            EventBus.publish(new ConnectionInitialized());
        }
    }

    private void setUninitialized() {
        boolean prevState = initialized;
        initialized = false;

        if (prevState == true && initialized == false) {
              LOG.info("Sending unInitialized  message!");
            EventBus.publish(new ConnectionUninitialized());
        }else if(prevState == false && initialized == false){
             LOG.info("Sending unInitialized  message!");
            EventBus.publish(new ConnectionUninitialized());
        }
    }
}
