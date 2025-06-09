package com.honda.mfg.device.mesmodule;

import com.honda.mfg.device.exceptions.CommunicationsException;
import com.honda.mfg.device.exceptions.NetworkCommunicationException;
import com.honda.mfg.device.exceptions.ResponseTimeoutException;
import com.honda.mfg.device.mesmodule.messages.GeneralMessage;
import com.honda.mfg.device.mesmodule.messages.MesMessage;
import com.honda.mfg.device.mesmodule.messages.MesRequest;
import com.honda.mfg.device.mesmodule.messages.PingMessage;
import com.honda.mfg.device.messages.MessageRequest;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.bushe.swing.event.annotation.ReferenceStrength;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: vcc30690
 * Date: 4/11/11
 */
public class BasicMesDevice implements MesDevice {
    private static final Logger LOG = LoggerFactory.getLogger(BasicMesDevice.class);

    private MesProcessorPair processorPair;
    private int responseTimeoutSec;
    private MesInitializeState initializeState;
    private GeneralMessage responseMessage;
    private volatile boolean pingResponseReceived;

    public BasicMesDevice(MesProcessorPair mesProcessorPair, MesInitializeState initializeState, int responseTimeoutSec) {
        this.processorPair = mesProcessorPair;
        this.initializeState = initializeState;
        this.responseTimeoutSec = responseTimeoutSec;
        AnnotationProcessor.process(this);
    }

    @EventSubscriber(eventClass = GeneralMessage.class, referenceStrength = ReferenceStrength.STRONG)
    public void receiveResponse(GeneralMessage response) {
        LOG.debug("Received MES MESSAGE response!");
        this.responseMessage = response;
    }

    @EventSubscriber(eventClass = PingMessage.class, referenceStrength = ReferenceStrength.STRONG)
    public void receiveResponse(PingMessage ping) {
        LOG.debug("Received MES PING response.  HEALTHY?  " + ping.isHealthy());
        this.pingResponseReceived = ping.isHealthy();
    }

    @Override
    public void sendMessage(MesMessage message) throws CommunicationsException, NetworkCommunicationException {
        if (!isInitialized()) {
            LOG.debug("Initialization required.  Not sending request.");
            throw new CommunicationsException("Unable to send mes request since the device is not initialized right now.");
        }
       // LOG.debug("Sending:  " + message.getMessage());
        MessageRequest request = new MesRequest(message, processorPair.getMessageSeparatorCharacter());
        processorPair.getRequestProcessor().sendRequest(request);
    }

    @Override
    public GeneralMessage sendMessage(MesMessage request, int timeoutSec) {
        LOG.trace("Sending MES MESSAGE request()");
        long start = System.currentTimeMillis();
        sendMessage(request);
        while (responseMessage == null) {
            throwResponseTimeoutIfExpired(start, timeoutSec);
        }
        return responseMessage;
    }

    @Override
    public boolean isHealthy() {
        LOG.trace("Sending MES PING request()");
        long start = System.currentTimeMillis();
        MesMessage pingMessage = new PingMessage();
        this.pingResponseReceived = false;
        sendMessage(pingMessage);
        LOG.trace("Checking for response.");
        while (!pingResponseReceived) {
            try {
                throwResponseTimeoutIfExpired(start);
            } catch (ResponseTimeoutException e) {
                return false;
            }
        }
        return pingResponseReceived;
    }

    private void throwResponseTimeoutIfExpired(long start) {
        throwResponseTimeoutIfExpired(start, responseTimeoutSec);
    }

    private void throwResponseTimeoutIfExpired(long start, int timeoutSec) {
        long timeoutMilli = timeoutSec * 1000L;
        if (System.currentTimeMillis() - start > (timeoutMilli)) {
            throw new ResponseTimeoutException("Response timeout exceeded limit: " + responseTimeoutSec + " (sec)");
        }
    }

    private boolean isInitialized() {
        return initializeState.isInitialized();
    }

    GeneralMessage getGeneralMessage() {
        return responseMessage;
    }
}
