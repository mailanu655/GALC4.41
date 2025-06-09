package com.honda.mfg.device.mesmodule;

import com.honda.io.StreamPair;
import com.honda.mfg.device.MessageRequestProcessor;
import com.honda.mfg.device.MessageRequestProcessorImpl;
import com.honda.mfg.device.exceptions.NetworkCommunicationException;
import com.honda.mfg.schedule.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * User: vcc30690
 * Date: 4/11/11
 */
public class StreamMesProcessorPair implements MesProcessorPair, Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(StreamMesProcessorPair.class);
    private static final long INVALID_INITIALIZE_TIME = -1;

    private StreamPair streamPair;
    private volatile boolean runOnce;
    private String messageSeparatorCharacter;

    private volatile long initializeRequiredTime;

    public StreamMesProcessorPair(StreamPair streamPair) {
        this(streamPair, MesMessageSeparators.DEFAULT.getSeparator());
    }

    public StreamMesProcessorPair(StreamPair streamPair, final String messageSeparatorCharacter) {
        this(streamPair, messageSeparatorCharacter, false);
        // Start a one-time / one-pass scheduler to run the run() method once
        new Scheduler(this, this.getClass().getSimpleName());
    }

    StreamMesProcessorPair(StreamPair streamPair, boolean runOnce) {
        this(streamPair, MesMessageSeparators.DEFAULT.getSeparator(), runOnce);
    }

    StreamMesProcessorPair(StreamPair streamPair, final String messageSeparatorCharacter, boolean runOnce) {
        this.streamPair = streamPair;
        this.messageSeparatorCharacter = messageSeparatorCharacter;
        this.runOnce = runOnce;
    }

    public MessageRequestProcessor getRequestProcessor() {
        //need to recreate the processors every time to make sure it fixes
        //itself if the streamPair goes stale
        return new MessageRequestProcessorImpl(streamPair.out());
    }

    public long getInitializeRequiredTime() {
        return initializeRequiredTime;
    }

    public String getMessageSeparatorCharacter() {
        return messageSeparatorCharacter;
    }

    public void run() {
        LOG.debug("Starting run() loop");
        MesResponseProcessor responseProcessor;
        while (true) {
            try {
                LOG.debug("About to get response processor.");
                responseProcessor = getResponseProcessor();
                LOG.debug("Retrieved the response processor.");
                this.initializeRequiredTime = System.currentTimeMillis();
                responseProcessor.run();
            } catch (NetworkCommunicationException e) {
                // failed to get response processor...
                LOG.debug("Unable to create/run response processor.  MESSAGE: " + e.getMessage());
                this.initializeRequiredTime = INVALID_INITIALIZE_TIME;
            } catch (Exception e) {
                // failed to get response processor...
                LOG.debug("Unexpected/unknown failure.  ERROR: " + e.getMessage(), e);
                this.initializeRequiredTime = INVALID_INITIALIZE_TIME;
            }
            if (runOnce) {
                break;
            }
        }

        LOG.debug("exited run() loop");
    }

    private MesResponseProcessor getResponseProcessor() {
        //need to recreate the processors every time to make sure it fixes
        //itself if the streamPair goes stale
        MesResponseReader responseReader = new MesResponseReader(streamPair.in());

        return new MesResponseProcessor(responseReader, runOnce);
    }
}
