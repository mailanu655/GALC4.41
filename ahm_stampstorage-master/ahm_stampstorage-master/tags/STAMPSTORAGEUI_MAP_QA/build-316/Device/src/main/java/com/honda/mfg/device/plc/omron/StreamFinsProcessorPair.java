package com.honda.mfg.device.plc.omron;

import com.honda.io.StreamPair;
import com.honda.mfg.device.MessageRequestProcessor;
import com.honda.mfg.device.MessageRequestProcessorImpl;
import com.honda.mfg.device.exceptions.NetworkCommunicationException;
import com.honda.mfg.schedule.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Mihail Chirita
 * Date: Sep 27, 2010
 * Time: 1:08:25 PM
 */
public class StreamFinsProcessorPair implements FinsProcessorPair, Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(StreamFinsProcessorPair.class);
    private static final long INVALID_INITIALIZE_TIME = -1;

    private StreamPair streamPair;
    private volatile boolean runOnce;
    private FinsResponseBuilder responseBuilder;

    private volatile long initializeRequiredTime;

    public StreamFinsProcessorPair(StreamPair streamPair) {
        this(streamPair, false);
        // Start a one-time / one-pass scheduler to run the run() method once
        new Scheduler(this, this.getClass().getSimpleName());
    }

    StreamFinsProcessorPair(StreamPair streamPair, boolean runOnce) {
        this.streamPair = streamPair;
        this.runOnce = runOnce;
        this.responseBuilder = new FinsResponseBuilder(new FinsResponseParser());
    }

    @Override
    public MessageRequestProcessor getRequestProcessor() {
        //need to recreate the processors every time to make sure it fixes
        //itself if the streamPair goes stale
        return new MessageRequestProcessorImpl(streamPair.out());
    }

    @Override
    public long getInitializeRequiredTime() {
        return initializeRequiredTime;
    }

    @Override
    public void run() {
        LOG.debug("Starting run() loop");
        FinsResponseProcessor responseProcessor;
        while (true) {
            try {
                responseProcessor = getResponseProcessor();
                this.initializeRequiredTime = System.currentTimeMillis();
                responseProcessor.run();
            } catch (NetworkCommunicationException e) {
                // failed to get response processor...
                LOG.warn("Unable to create/run response processor.  MESSAGE: " + e.getMessage());
                this.initializeRequiredTime = INVALID_INITIALIZE_TIME;
            } catch (Throwable e) {
                // failed to get response processor...
                LOG.error("Unexpected/unknown failure.  ERROR: " + e.getMessage(), e);
                this.initializeRequiredTime = INVALID_INITIALIZE_TIME;
            }
            if (runOnce) {
                break;
            }
        }
    }

    private FinsResponseProcessor getResponseProcessor() {
        //need to recreate the processors every time to make sure it fixes
        //itself if the streamPair goes stale
        FinsResponseReader responseReader = new FinsResponseReader(streamPair.in());
        return new FinsResponseProcessor(responseReader, responseBuilder, runOnce);
    }
}
