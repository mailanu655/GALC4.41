package com.honda.mfg.device.plc.omron;

import com.honda.mfg.device.exceptions.CommunicationsException;
import com.honda.mfg.device.exceptions.NetworkCommunicationException;
import com.honda.mfg.device.exceptions.ResponseTimeoutException;
import com.honda.mfg.device.messages.MessageRequest;
import com.honda.mfg.device.plc.PlcDevice;
import com.honda.mfg.device.plc.PlcMemory;
import com.honda.mfg.device.plc.omron.messages.*;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.bushe.swing.event.annotation.ReferenceStrength;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Jeffrey M Lutz
 * Date: Sep 9, 2010
 * Time: 7:59:01 AM
 */
public class BasicFinsPlcDevice implements PlcDevice {
    private static final Logger LOG = LoggerFactory.getLogger(BasicFinsPlcDevice.class);

    private static final int READ_MEMORY_SERVICE_ID = 1;
    private static final int WRITE_MEMORY_SERVICE_ID = 2;
    private static final int READ_CLOCK_SERVICE_ID = 3;

    private FinsProcessorPair processorPair;
    private final long responseTimeoutSec;
    private volatile String memoryReadValue;
    private volatile String clockReadValue;
    private FinsInitializeState initializeState;

    private volatile boolean alreadyReading;

    public BasicFinsPlcDevice(FinsProcessorPair finsProcessorPair, FinsInitializeState initializeState, int responseTimeoutSec) {
        this.processorPair = finsProcessorPair;
        this.initializeState = initializeState;
        this.responseTimeoutSec = responseTimeoutSec;
        AnnotationProcessor.process(this);
    }

    @EventSubscriber(eventClass = FinsMemoryReadResponse.class, referenceStrength = ReferenceStrength.STRONG)
    public void receiveMemoryReadResponse(FinsMemoryReadResponse response) {
        LOG.debug("Received Memory READ response.");
        LOG.trace("Response:  " + response);
        LOG.trace("response.getData(): " + response.getData());
        this.memoryReadValue = response.getData();
        LOG.trace("Updated memoryReadValue:  " + this.memoryReadValue);
    }

    @EventSubscriber(eventClass = FinsClockReadResponse.class, referenceStrength = ReferenceStrength.STRONG)
    public void receiveClockReadResponse(FinsClockReadResponse response) {
        LOG.debug("Received Clock READ response.  clock reading: " + response);
        this.clockReadValue = response.clockReading();
    }

    @Override
    public String readMemory(PlcMemory memory) {
        if (alreadyReading) {
            return null;
        }
        alreadyReading = true;
        FinsMemoryReadRequest readRequest =
                new FinsMemoryReadRequest(memory, getDestinationNode(), getSourceNode(), READ_MEMORY_SERVICE_ID);
        long start = System.currentTimeMillis();
        memoryReadValue = null;
        LOG.debug("Sending  Memory READ request.");
        try {
            sendRequest(readRequest);
        } catch (RuntimeException e) {
            alreadyReading = false;
            throw e;
        }
        while (memoryReadValue == null || memoryReadValue.length() < 1) {
            throwResponseTimeoutIfExpired(start);
        }
        alreadyReading = false;
        LOG.debug("readMemory() method done.");
        return memoryReadValue;
    }

    @Override
    public void writeMemory(PlcMemory memory, String data) {
        FinsMemoryWriteRequest writeRequest =
                new FinsMemoryWriteRequest(memory, getDestinationNode(), getSourceNode(), WRITE_MEMORY_SERVICE_ID, data);
        LOG.debug("Sending  Memory WRITE request.");
        sendRequest(writeRequest);
    }

    @Override
    public String readClock() {
        FinsClockReadRequest clockReadRequest =
                new FinsClockReadRequest(getDestinationNode(), getSourceNode(), READ_CLOCK_SERVICE_ID);
        long start = System.currentTimeMillis();
        clockReadValue = null;
        LOG.debug("Sending  Clock READ request.");
        sendRequest(clockReadRequest);
        while (clockReadValue == null || clockReadValue.length() < 1) {
            throwResponseTimeoutIfExpired(start);
        }
        LOG.debug("readClock() method done.");
        return clockReadValue;
    }

    private int getSourceNode() {
        return initializeState.getSourceNode();
    }

    private int getDestinationNode() {
        return initializeState.getDestinationNode();
    }

    private void throwResponseTimeoutIfExpired(long start) {
        if (System.currentTimeMillis() - start > (responseTimeoutSec * 1000L)) {
            alreadyReading = false;
            throw new ResponseTimeoutException("Response timeout exceeded limit: " + responseTimeoutSec + " (sec)");
        }
    }

    private boolean isInitialized() {
        return initializeState.isInitialized();
    }

    private void sendRequest(final MessageRequest request)
            throws CommunicationsException, NetworkCommunicationException {
        if (!isInitialized()) {
            LOG.debug("Initialization required.  Not sending request.");
            throw new CommunicationsException("Unable to send fins request since the device is not initialized right now.");
        }
        LOG.debug("Sending message type:  " + request.getClass().getSimpleName());

        processorPair.getRequestProcessor().sendRequest(request);
    }
}
