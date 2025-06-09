package com.honda.mfg.device.rfid.etherip;

import com.honda.mfg.device.exceptions.CommunicationsException;
import com.honda.mfg.device.exceptions.NetworkCommunicationException;
import com.honda.mfg.device.exceptions.ResponseTimeoutException;
import com.honda.mfg.device.messages.MessageRequest;
import com.honda.mfg.device.rfid.RfidDevice;
import com.honda.mfg.device.rfid.RfidMemory;
import com.honda.mfg.device.rfid.etherip.exceptions.TagNotFoundException;
import com.honda.mfg.device.rfid.etherip.messages.*;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Jeffrey M Lutz
 * Date: Nov 10, 2010
 */
public class BasicEtherIpRfidDevice implements RfidDevice {
    private static final Logger LOG = LoggerFactory.getLogger(BasicEtherIpRfidDevice.class);

    private RfidProcessorPair processorPair;
    private EtherIpInitializeState initializeState;
    private int responseTimeoutSec;
    private int rfidRetryCountLimit;

    private RfidGetControllerInfoResponse getControllerInfoResponse;
    private RfidReadTagIdResponse readTagIdResponse;
    private RfidReadTagResponse readTagResponse;
    private RfidWriteTagResponse writeTagResponse;

    public BasicEtherIpRfidDevice(RfidProcessorPair processorPair, EtherIpInitializeState initializeState, int responseTimeoutSec, int rfidRetryCount) {
        this.processorPair = processorPair;
        this.initializeState = initializeState;
        this.responseTimeoutSec = responseTimeoutSec;
        this.rfidRetryCountLimit = rfidRetryCount;

        AnnotationProcessor.process(this);
    }

    @EventSubscriber(eventClass = RfidGetControllerInfoResponse.class)
    public void receivedRfidGetControllerInfoResponse(RfidGetControllerInfoResponse response) {
        LOG.trace("Received get controller info response!");
        this.getControllerInfoResponse = response;
    }

    @EventSubscriber(eventClass = RfidReadTagIdResponse.class)
    public void receivedRfidReadTagIdResponse(RfidReadTagIdResponse response) {
        LOG.trace("Received read tag id response!");
        this.readTagIdResponse = response;
    }

    @EventSubscriber(eventClass = RfidReadTagResponse.class)
    public void receivedRfidReadTagResponse(RfidReadTagResponse response) {
        LOG.trace("Received read tag response!");
        this.readTagResponse = response;
    }

    @EventSubscriber(eventClass = RfidWriteTagResponse.class)
    public void receivedClearPendingResponsesResponse(RfidWriteTagResponse response) {
        LOG.trace("Received write tag response!");
        this.writeTagResponse = response;
    }

    @Override
    public String readControllerInfo()
            throws CommunicationsException, NetworkCommunicationException, ResponseTimeoutException {
        RfidGetControllerInfoRequest request = new RfidGetControllerInfoRequest();
        long start = System.currentTimeMillis();
        getControllerInfoResponse = null;
        sendRequest(request);
        while (getControllerInfoResponse == null) {
            throwResponseTimeoutIfExpired(start);
        }
        return getControllerInfoResponse.getRfidResponse();
    }

    @Override
    public String readTagId()
            throws CommunicationsException, NetworkCommunicationException, ResponseTimeoutException {
        RfidReadTagIdRequest request = new RfidReadTagIdRequest();
        long start = System.currentTimeMillis();
        readTagIdResponse = null;
        sendRequest(request);
        while (readTagIdResponse == null || readTagIdResponse.getTagId() == null) {
            throwTagNotFoundIfExpired(start);
        }
        return readTagIdResponse.getTagId();
    }

    @Override
    public String readTag(RfidMemory memory)
            throws CommunicationsException, NetworkCommunicationException, ResponseTimeoutException {
        RfidReadTagRequest request = new RfidReadTagRequest(memory.getStartAddress(), memory.getWordLength());
        long start = System.currentTimeMillis();
        readTagResponse = null;
        sendRequest(request);
        while (readTagResponse == null || readTagResponse.getTagValue() == null) {
            throwTagNotFoundIfExpired(start);
        }
        return readTagResponse.getTagValue();
    }

    @Override
    public void writeTag(RfidMemory memory, String tagValue)
            throws CommunicationsException, NetworkCommunicationException, ResponseTimeoutException {
        RfidWriteTagRequest request = new RfidWriteTagRequest(memory.getStartAddress(), tagValue);
    	boolean retryNeeded = false;
    	int writeRetryCount = 1;

        long start = System.currentTimeMillis();
        long loopTime = System.currentTimeMillis();

        LOG.trace("Attempting to Write RFID Data <" + request.getMessageRequest() + ">.");
        do {
        	try {
				writeTagResponse = null;
				LOG.info("Writing to RFID Tag - Try # <" + writeRetryCount + " / " + rfidRetryCountLimit + ">.");
				sendRequest(request);

				loopTime = System.currentTimeMillis();
				LOG.trace("Begin RFID Write Wait Loop.");
				start = System.currentTimeMillis();
				while (writeTagResponse == null) {
					throwTagNotFoundIfExpired(start);
				}
				break;	//IF HERE - TAG WAS WRITTEN SO BREAK OUT OF do/while LOOP
        	} catch (TagNotFoundException e) {
        		LOG.warn("RFID Write Try #< " + writeRetryCount + " / " + rfidRetryCountLimit + "> Failed - Incrementing writeRetryCount.");
	  			writeRetryCount++;
				retryNeeded = true;
			}
        	loopTime = System.currentTimeMillis() - loopTime;
			LOG.trace("While RFID Write Wait Loop Completed in " + loopTime + " (ms).");
        	if (writeRetryCount > rfidRetryCountLimit) {
				LOG.error("Exceeded Max RFID Write Retry Count <" + writeRetryCount + " / " + rfidRetryCountLimit + ">. Throwing TagNotFoundException.");
				throw new TagNotFoundException("RFID Write Retry Count Exceeded. RFID tag not found.");
			}
    	} while (retryNeeded);
    }

    private boolean isInitialized() {
        return initializeState.isInitialized();
    }

    private void sendRequest(MessageRequest request) throws CommunicationsException, NetworkCommunicationException {
        if (!isInitialized()) {
            LOG.warn("Initialization required.  Not sending request.");
            throw new CommunicationsException("Cannot send message now due to the device not being initialized.  If this message repeats after 10+ secs, please check the network and device for health.");
        }
        LOG.trace("Sending message.");
        // Note:  getRequestProcessor() throws NetworkCommunicationException
        // Note:  sendRequest() method throws CommunicationsException
        processorPair.getRequestProcessor().sendRequest(request);
    }

    private void throwTagNotFoundIfExpired(long start) {
        if (System.currentTimeMillis() - start > (responseTimeoutSec * 1000L)) {
        	LOG.trace("Throwing TagNotFoundException. RFID tag not found.");
        	throw new TagNotFoundException("RFID tag not found.");
        }
    }

    private void throwResponseTimeoutIfExpired(long start) {
        long timeout;
        timeout = responseTimeoutSec * 1000L;
        if (System.currentTimeMillis() - start > (timeout)) {
            throw new ResponseTimeoutException("Response timeout exceeded limit: " + responseTimeoutSec + " (sec)");
        }
    }
}