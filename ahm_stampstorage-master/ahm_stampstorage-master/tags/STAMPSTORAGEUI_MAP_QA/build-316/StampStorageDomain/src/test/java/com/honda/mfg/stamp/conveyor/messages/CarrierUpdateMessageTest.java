package com.honda.mfg.stamp.conveyor.messages;

import com.honda.mfg.stamp.conveyor.domain.*;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.PartProductionVolume;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;


/**
 * User: Jeffrey M Lutz
 * Date: 6/20/11
 */
public class CarrierUpdateMessageTest {

    @Test
    public void successfullyCreateValidMessage() {
        CarrierUpdateMessage msg = new CarrierUpdateMessage(getCarrier());

        assertEquals(StorageMessageType.CARRIER_UPDATE, msg.getMessageType());
        assertEquals("123", msg.carrierNumber);
        assertEquals("123", msg.getCarrierNumber());
        assertEquals("1", msg.dieNumber);
        assertEquals("12", msg.quantity);
        assertEquals("333", msg.currentLocation);
        assertEquals("456", msg.destination);
        assertEquals("456",msg.getDestination());
        assertEquals("0", msg.status);
        assertEquals("2011-06-20 08:58:26.708", msg.productionRunTimestamp);
        assertEquals("789", msg.productionRunNumber);
        assertEquals("", msg.originationLocation);
        assertEquals("", msg.tagID);
        assertEquals("", msg.buffer);
    }

    @Test
    public void successfullyCreateValidMessageWithNullProductionRunNumber() {
        Carrier c = getCarrier();
        c.setProductionRunNo(null);
        c.setReprocess(true);
        CarrierUpdateMessage msg = new CarrierUpdateMessage(c);

        assertEquals(StorageMessageType.CARRIER_UPDATE, msg.getMessageType());
        assertEquals("123", msg.carrierNumber);
        assertEquals("1", msg.dieNumber);
        assertEquals("12", msg.quantity);
        assertEquals("333", msg.currentLocation);
        assertEquals("456", msg.destination);
        assertEquals("0", msg.status);
        assertEquals("2011-06-20 08:58:26.708", msg.productionRunTimestamp);
        assertEquals("", msg.productionRunNumber);
        assertEquals("", msg.originationLocation);
        assertEquals("", msg.tagID);
        assertEquals("", msg.buffer);
        assertEquals("1", msg.reprocess);
    }

    @Test
    public void successfullyCreateValidMessageWithEmptyCurrentLocation() {
        Carrier c = getCarrier();
        c.setCurrentLocation(null);
        CarrierUpdateMessage msg = new CarrierUpdateMessage(c);

        assertEquals(StorageMessageType.CARRIER_UPDATE, msg.getMessageType());
        assertEquals("123", msg.carrierNumber);
        assertEquals("1", msg.dieNumber);
        assertEquals("12", msg.quantity);
        assertEquals("", msg.currentLocation);

        assertEquals("456", msg.destination);
        assertEquals("0", msg.status);
        assertEquals("2011-06-20 08:58:26.708", msg.productionRunTimestamp);
        assertEquals("789", msg.productionRunNumber);
        assertEquals("", msg.originationLocation);
        assertEquals("", msg.tagID);
        assertEquals("", msg.buffer);

        assertNotSame(StorageMessageType.STATUS_UPDATE, msg.getMessageType());
    }

    private Carrier getCarrier() {
        Carrier c = new Carrier();
        c.setCarrierNumber(123);
        c.setDie(new Die(1L, PartProductionVolume.HIGH_VOLUME));
        c.setQuantity(12);
        c.setCurrentLocation(getStop(333));
        c.setDestination(getStop(456));
        c.setCarrierStatus(CarrierStatus.SHIPPABLE);
        long fixedTime = 1308574706708L;  // NOTE:  Equals: 2011-06-20 08:58:26
        Date myDate = new Date(fixedTime);
        c.setStampingProductionRunTimestamp(myDate);
        c.setProductionRunNo(789);
        return c;
    }

    @Test
    public void successfullyCreateValidCarrierUpdateMessage() {
        Carrier c = getCarrier();
        c.setCarrierStatus(null);
        CarrierUpdateMessage msg = new CarrierUpdateMessage(c);

        assertEquals(StorageMessageType.CARRIER_UPDATE, msg.getMessageType());
        assertEquals("123", msg.carrierNumber);
        assertEquals("1", msg.dieNumber);
        assertEquals("12", msg.quantity);
        assertEquals("333", msg.currentLocation);
        assertEquals("456", msg.destination);
        assertEquals("", msg.status);
        assertEquals("2011-06-20 08:58:26.708", msg.productionRunTimestamp);
        assertEquals("789", msg.productionRunNumber);
        assertEquals("", msg.originationLocation);
        assertEquals("", msg.tagID);
        assertEquals("", msg.buffer);
    }


    private Stop getStop(int id) {
        Stop s = new Stop();
        s.setName("STOP_" + id);
        s.setId(new Long(id));
        return s;
    }
}
