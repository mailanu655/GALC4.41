package com.honda.mfg.stamp.conveyor.processor.messagebuilders.json;

import com.honda.mfg.stamp.conveyor.domain.*;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.PartProductionVolume;
import com.honda.mfg.stamp.conveyor.messages.CarrierStatusMessage;
import com.honda.mfg.stamp.conveyor.messages.CarrierUpdateMessage;
import com.honda.mfg.stamp.conveyor.messages.DeviceMessageType;
import com.honda.mfg.stamp.conveyor.messages.StatusUpdateMessage;
import com.thoughtworks.xstream.converters.ConversionException;
import junit.framework.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: vcc30690
 * Date: 3/21/11
 */
public class JSonResponseParserTest {
    private static final Logger LOG = LoggerFactory.getLogger(JSonResponseParserTest.class);

    @Test
    public void successfullyParseCarrierUpdateToString() {
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

        JSonResponseParser jSonResponseParser = new JSonResponseParser();
        CarrierUpdateMessage msg = new CarrierUpdateMessage(c);

        String actualJsonMsg;
        actualJsonMsg = jSonResponseParser.toJson(msg, CarrierUpdateMessage.class);
        System.out.println(actualJsonMsg);
    }

    @Test
    public void successfullyParseStatusUpdateToString() {
        List<Integer> numbers = new ArrayList<Integer>();
        numbers.add(5);
        numbers.add(6);

        JSonResponseParser jSonResponseParser = new JSonResponseParser();
        StatusUpdateMessage msg = new StatusUpdateMessage(CarrierStatus.INSPECTION_REQUIRED, numbers, null);

        String actualJsonMsg;
        actualJsonMsg = jSonResponseParser.toJson(msg, StatusUpdateMessage.class);
        System.out.println(actualJsonMsg);
    }

    private Stop getStop(int id) {
        Stop s = new Stop();
        s.setName("STOP_" + id);
        s.setId(new Long(id));
        return s;
    }

    @Test
    public void successfullyCreateDeviceStatusMessageObject() {
        String actualJsonMsg = "{\"GeneralMessage\" : {\n" +
                "\"messageType\":\"" + DeviceMessageType.CARRIER_STATUS.toString() + "\",\n" +
                "\"carrier\":" +
                "{" +
                "\"carrierNumber\":\"1234\",\n" +
                "\"id\":103,\n" +
                "\"productionRunNo\":12,\n" +
                "\"stampingProductionRunTimestamp\":\"03/21/2011 12:00:00\",\n" +
                "\"currentLocation\":{\"name\":\"ST5-13\"},\n" +
                "\"destination\":\"\"\n" +
                "}}}";
        JSonResponseParser jSonResponseParser = new JSonResponseParser();

        CarrierStatusMessage message = (CarrierStatusMessage) jSonResponseParser.parse(actualJsonMsg, CarrierStatusMessage.class);
        Assert.assertEquals(DeviceMessageType.CARRIER_STATUS, message.getMessageType());

        String expectedJsonMessage = jSonResponseParser.toJson(message, CarrierUpdateMessage.class);

        LOG.info(expectedJsonMessage);
        Assert.assertNotNull(expectedJsonMessage);

        message.setUpdateDate(null);
        String expectedJsonMessage1 = jSonResponseParser.toJson(message, CarrierUpdateMessage.class);

         LOG.info(expectedJsonMessage1);
        Assert.assertNotNull(expectedJsonMessage1);
    }

    @Test(expected = ConversionException.class)
    public void throwsConversionException() {
        String actualJsonMsg = "{\"GeneralMessage\" : {\n" +
                "\"messageType\":\"" + DeviceMessageType.CARRIER_STATUS.toString() + "\",\n" +
                "\"carrier\":" +
                "{" +
                "\"carrierNumber\":\"1234\",\n" +
                "\"id\":103,\n" +
                "\"productionRunNo\":12,\n" +
                "\"stampingProductionRunTimestamp\":\"03/21/2011 \",\n" +
                "\"currentLocation\":{\"name\":\"ST5-13\"},\n" +
                "\"destination\":\"\"\n" +
                "}}}";
        JSonResponseParser jSonResponseParser = new JSonResponseParser();

        CarrierStatusMessage message = (CarrierStatusMessage) jSonResponseParser.parse(actualJsonMsg, CarrierStatusMessage.class);
    }

    @Test
    public void successfullyCreateStatusUpdateMessageObject() {

    }
}
