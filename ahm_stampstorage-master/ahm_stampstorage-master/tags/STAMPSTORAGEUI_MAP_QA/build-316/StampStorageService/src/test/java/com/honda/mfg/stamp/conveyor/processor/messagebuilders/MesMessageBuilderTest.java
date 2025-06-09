package com.honda.mfg.stamp.conveyor.processor.messagebuilders;

import com.honda.mfg.connection.processor.messages.GeneralMessage;
import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.messages.CarrierUpdateMessage;
import org.junit.Test;

import java.sql.Timestamp;

import static junit.framework.Assert.assertNotNull;

/**
 * User: vcc30690
 * Date: 4/25/11
 */
public class MesMessageBuilderTest {


    @Test
    public void successfullyBuildCarrierMoveRequest() {
        CarrierUpdateMessage carrierMessage = new CarrierUpdateMessage(getCarrier());

        MesMessageBuilder mesMessageBuilder = new MesMessageBuilder();
        GeneralMessage message = mesMessageBuilder.buildMesMessage(carrierMessage, CarrierUpdateMessage.class);

        assertNotNull(message);
    }

    Carrier getCarrier() {

        long id = 0;
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
