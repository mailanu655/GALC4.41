package com.honda.mfg.stamp.conveyor.processor;

import com.honda.mfg.connection.processor.ConnectionDevice;
import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.messages.CarrierUpdateMessage;
import com.honda.mfg.stamp.conveyor.messages.StatusUpdateMessage;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

/**
 * User: vcc30690
 * Date: 4/25/11
 */
public class DeviceSendMessageProcessorTest extends AbstractTestBase {


    @Test
    public void successfullySendCarrierMoveRequest() {
        AnnotationProcessor.process(this);
        ConnectionDevice device = mock(ConnectionDevice.class);
        CarrierUpdateMessage carrierMoveDeviceMessage = new CarrierUpdateMessage(getCarrier());
        DeviceSendMessageProcessor messageProcessor = new DeviceSendMessageProcessor(device);
        DeviceSendMessageProcessor messageProcessor1 = new DeviceSendMessageProcessor(null);
        EventBus.publish(carrierMoveDeviceMessage);
        assertNotNull(messageProcessor.getMessage());

        AnnotationProcessor.unprocess(this);
    }

    @Test
    public void successfullySendStatusUpdateMessage() {
        AnnotationProcessor.process(this);
        ConnectionDevice device = mock(ConnectionDevice.class);
        List<Integer> numbers = new ArrayList<Integer>();
        numbers.add(5);
        numbers.add(6);
        StatusUpdateMessage statusUpdateMessage = new StatusUpdateMessage(CarrierStatus.ON_HOLD, numbers, "user");
        DeviceSendMessageProcessor messageProcessor = new DeviceSendMessageProcessor(device);
        DeviceSendMessageProcessor messageProcessor1 = new DeviceSendMessageProcessor(null);
        EventBus.publish(statusUpdateMessage);
        assertNotNull(messageProcessor.getMessage());

        AnnotationProcessor.unprocess(this);
    }

    Carrier getCarrier() {

        int id = 0;
        Carrier carrier;
        Timestamp productionRunTimestamp = new Timestamp(System.currentTimeMillis());
        Integer prodRunNo = 108;
        Stop currentLocation = new Stop("05-13");
        Stop destination = new Stop("05-13");
        CarrierStatus carrierStatus = CarrierStatus.ON_HOLD;
        Integer carrierNumber = 1234;


        carrier = new Carrier(id++, productionRunTimestamp, prodRunNo, currentLocation, destination, carrierStatus, carrierNumber, null);

        return carrier;
    }
}

