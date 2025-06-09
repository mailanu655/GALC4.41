package com.honda.mfg.device.mesmodule;

import com.honda.mfg.device.MessageRequestProcessor;
import com.honda.mfg.device.exceptions.CommunicationsException;
import com.honda.mfg.device.exceptions.NetworkCommunicationException;
import com.honda.mfg.device.mesmodule.messages.MesDeviceInitialized;
import com.honda.mfg.device.mesmodule.messages.MesDeviceUninitialized;
import com.honda.mfg.device.mesmodule.messages.MesRequest;
import com.honda.mfg.device.mesmodule.messages.PingMessage;
import com.honda.mfg.device.messages.MessageRequest;
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
public class MesInitializer implements Runnable, MesInitializeState {
    private static final Logger LOG = LoggerFactory.getLogger(MesInitializer.class);

    private MesProcessorPair MesProcessorPair;

    private volatile long previousInitializeRequiredTime;
    private volatile long nextInitializeRequiredTime;
    private volatile boolean initialized;

    private Scheduler scheduler;

    public MesInitializer(MesProcessorPair MesProcessorPair, int initializeIntervalSecs) {
        this(MesProcessorPair);
        LOG.debug("#########  MesInitializer() called...");
        startInitializeThread(initializeIntervalSecs);
    }

    MesInitializer(MesProcessorPair MesProcessorPair) {
        AnnotationProcessor.process(this);
        this.MesProcessorPair = MesProcessorPair;
        this.previousInitializeRequiredTime = 0L;
        this.initialized = false;
    }

    private void startInitializeThread(int initializeIntervalSecs) {
        scheduler = new Scheduler(this, initializeIntervalSecs, this.getClass().getSimpleName());
    }

    void shutdown() {
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
        long currentInitializeRequiredTime = MesProcessorPair.getInitializeRequiredTime();
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
        MessageRequest initializeRequest = new MesRequest(new PingMessage());
        try {
            MessageRequestProcessor requestProcessor = MesProcessorPair.getRequestProcessor();
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
            EventBus.publish(new MesDeviceInitialized());
        }
    }

    private void setUninitialized() {
        boolean prevState = initialized;
        initialized = false;

        if (prevState == true && initialized == false) {
              LOG.info("Sending unInitialized  message!");
            EventBus.publish(new MesDeviceUninitialized());
        }else if(prevState == false && initialized == false){
             LOG.info("Sending unInitialized  message!");
            EventBus.publish(new MesDeviceUninitialized());
        }
    }
}
