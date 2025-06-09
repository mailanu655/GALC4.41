package com.honda.mfg.stamp.conveyor.processor;

import com.honda.mfg.connection.processor.ConnectionDevice;
import com.honda.mfg.connection.processor.messages.GeneralMessage;
import com.honda.mfg.stamp.conveyor.messages.CarrierUpdateMessage;
import com.honda.mfg.stamp.conveyor.messages.Message;
import com.honda.mfg.stamp.conveyor.messages.PlcAlarmMessage;
import com.honda.mfg.stamp.conveyor.messages.StatusUpdateMessage;
import com.honda.mfg.stamp.conveyor.processor.messagebuilders.MesMessageBuilder;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.bushe.swing.event.annotation.ReferenceStrength;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: vcc30690
 * Date: 3/23/11
 */
public class DeviceSendMessageProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(DeviceSendMessageProcessor.class);

    private ConnectionDevice device;
    private Message message;


    public DeviceSendMessageProcessor(ConnectionDevice device) {
        AnnotationProcessor.process(this);
        String name = Thread.currentThread().getName();
        LOG.trace("STORAGE THREAD NAME: " + name);
        this.device = device;
    }

    @EventSubscriber(eventClass = CarrierUpdateMessage.class, referenceStrength = ReferenceStrength.STRONG)
    public void carrierUpdateStorageMessageListener(CarrierUpdateMessage infoMessage) {
        message = infoMessage;
        MesMessageBuilder mesMessageBuilder = new MesMessageBuilder();
        GeneralMessage mesMsg = mesMessageBuilder.buildMesMessage(infoMessage, CarrierUpdateMessage.class);
        if (device != null) {
            device.sendMessage(mesMsg);
        }

        LOG.info("Sent carrier update message   -->   " + mesMsg.getMessage());
    }

    @EventSubscriber(eventClass = PlcAlarmMessage.class, referenceStrength = ReferenceStrength.STRONG)
       public void plcAlarmMessageListener(PlcAlarmMessage infoMessage) {
           message = infoMessage;
           MesMessageBuilder mesMessageBuilder = new MesMessageBuilder();
           GeneralMessage mesMsg = mesMessageBuilder.buildMesMessage(infoMessage, PlcAlarmMessage.class);
           if (device != null) {
               device.sendMessage(mesMsg);
           }

           LOG.info("Sent Plc Alarm Message   -->   " + mesMsg.getMessage());
       }


    @EventSubscriber(eventClass = StatusUpdateMessage.class, referenceStrength = ReferenceStrength.STRONG)
    public void statusUpdateStorageMessageListener(StatusUpdateMessage infoMessage) {
        message = infoMessage;
        MesMessageBuilder mesMessageBuilder = new MesMessageBuilder();
        GeneralMessage mesMsg = mesMessageBuilder.buildMesMessage(infoMessage, StatusUpdateMessage.class);
        if (device != null) {
            device.sendMessage(mesMsg);
        }
        LOG.info("Sent status update message   -->   " + mesMsg.getMessage());
    }

    public Message getMessage() {
        return message;
    }
}
