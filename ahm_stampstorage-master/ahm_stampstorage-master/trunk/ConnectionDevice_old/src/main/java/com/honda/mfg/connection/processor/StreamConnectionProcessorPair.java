package com.honda.mfg.connection.processor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.honda.io.StreamPair;
import com.honda.mfg.connection.MessageRequestProcessor;
import com.honda.mfg.connection.MessageRequestProcessorImpl;
import com.honda.mfg.connection.exceptions.NetworkCommunicationException;
import com.honda.mfg.schedule.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * User: vcc30690
 * Date: 4/11/11
 */
public class StreamConnectionProcessorPair implements ConnectionProcessorPair, Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(StreamConnectionProcessorPair.class);
    private static final long INVALID_INITIALIZE_TIME = -1;

    private StreamPair streamPair;
    private volatile boolean runOnce;
    private String messageSeparatorCharacter;

    private volatile long initializeRequiredTime;
    private int maxExceptionCount = 10;

    public int getMaxExceptionCount() {
		return maxExceptionCount;
	}

	public void setMaxExceptionCount(int maxExceptionCount) {
		this.maxExceptionCount = maxExceptionCount;
	}

	public StreamConnectionProcessorPair(StreamPair streamPair) {
        this(streamPair, ConnectionMessageSeparators.DEFAULT.getSeparator());
    }

    public StreamConnectionProcessorPair(StreamPair streamPair, final String messageSeparatorCharacter) {
        this(streamPair, messageSeparatorCharacter, false);
        // Start a one-time / one-pass scheduler to run the run() method once
        new Scheduler(this, this.getClass().getSimpleName());
    }

    StreamConnectionProcessorPair(StreamPair streamPair, boolean runOnce) {
        this(streamPair, ConnectionMessageSeparators.DEFAULT.getSeparator(), runOnce);
    }

    StreamConnectionProcessorPair(StreamPair streamPair, final String messageSeparatorCharacter, boolean runOnce) {
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
        ConnectionResponseProcessor responseProcessor;
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
            finally  {
            	pause(2, TimeUnit.SECONDS);
            }
            if (runOnce) {
                break;
            }
        }

        LOG.debug("exited run() loop");
    }

    private void pause(int t, TimeUnit unit)  {
		ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
		
		try {
			exec.schedule(
					new Runnable() {					
						@Override
						public void run() {
							//do nothing						
						}
					}, t, unit).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		finally  {
			if(exec != null)  exec.shutdown();
		}

    }
    private ConnectionResponseProcessor getResponseProcessor() {
        //need to recreate the processors every time to make sure it fixes
        //itself if the streamPair goes stale
        ConnectionResponseReader responseReader = new ConnectionResponseReader(streamPair.in());
        ConnectionResponseProcessor conResProc = new ConnectionResponseProcessor(responseReader, runOnce);
        conResProc.setMaxExceptionCount(maxExceptionCount);
        return conResProc;
    }
}
