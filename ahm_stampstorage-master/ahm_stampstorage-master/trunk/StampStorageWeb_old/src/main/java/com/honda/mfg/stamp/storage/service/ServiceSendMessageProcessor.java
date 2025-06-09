package com.honda.mfg.stamp.storage.service;

import com.honda.mfg.connection.processor.ConnectionDevice;
import com.honda.mfg.connection.processor.messages.GeneralMessage;
import com.honda.mfg.stamp.conveyor.messages.CarrierUpdateRequestMessage;
import com.honda.mfg.stamp.conveyor.messages.JsonServiceWrapperMessage;
import com.honda.mfg.stamp.conveyor.messages.Message;
import com.honda.mfg.stamp.conveyor.messages.ServiceRequestMessage;
import com.honda.mfg.stamp.storage.service.messagebuilders.MesMessageBuilder;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.bushe.swing.event.annotation.ReferenceStrength;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sends message to the service over the client socket
 * {@see com.honda.mfg.connection.processor.ConnectionDevice}
 * {@see com.honda.mfg.connection.processor.BasicConnection}
 * User: vcc44349
 * Date: 01/09/2012
 */
public class ServiceSendMessageProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(ServiceSendMessageProcessor.class);

    private ConnectionDevice device;
    private Message message = null;


    public ServiceSendMessageProcessor(ConnectionDevice device) {
        AnnotationProcessor.process(this);
        String name = Thread.currentThread().getName();
        LOG.trace("STORAGE THREAD NAME: " + name);
        this.device = device;
    }

    @EventSubscriber(eventClass = CarrierUpdateRequestMessage.class, referenceStrength = ReferenceStrength.STRONG)
    public void carrierUpdateRequestMessageListener(ServiceRequestMessage infoMessage) {
        message = infoMessage;
        MesMessageBuilder mesMessageBuilder = new MesMessageBuilder();
        GeneralMessage mesMsg = mesMessageBuilder.buildMesMessage(infoMessage, CarrierUpdateRequestMessage.class);
        if (device != null) {
            device.sendMessage(mesMsg);
        }

        LOG.info("Sent carrier update request message   -->   " + mesMsg.getMessage());
    }

    @EventSubscriber(eventClass = JsonServiceWrapperMessage.class, referenceStrength = ReferenceStrength.STRONG)
    public void carrierUpdateRequestMessageListener(JsonServiceWrapperMessage infoMessage) {
        message = infoMessage;
        MesMessageBuilder mesMessageBuilder = new MesMessageBuilder();
        GeneralMessage mesMsg = mesMessageBuilder.buildMesMessage(infoMessage, JsonServiceWrapperMessage.class);
        if (device != null) {
            device.sendMessage(mesMsg);
        }

        LOG.info("Sent carrier update request message   -->   " + mesMsg.getMessage());
    }

    public Message getMessage() {
        return message;
    }
}
