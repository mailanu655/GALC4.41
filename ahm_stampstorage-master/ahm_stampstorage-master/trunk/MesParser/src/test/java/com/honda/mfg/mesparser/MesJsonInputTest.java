package com.honda.mfg.mesparser;

import com.ils_tech.dw.ei.DWCustomDataHolder;
import junit.framework.TestCase;

import java.util.HashMap;

/**
 * User: Adam S. Kendell
 * Date: 5/17/11
 */
public class MesJsonInputTest extends TestCase {

    public void testCarrierUpdateMessage() {

        String carrierUpdateMessage = "{\"GeneralMessage\": {\n" +
                "\"messageType\":\"CARRIER_UPDATE\",\n" +
                "\"stampingProductionRunTimestamp\":\"03/21/2011 13:50:00\",\n" +
                "\"productionRunNo\":\"12\", \n" +
                "\"quantity\":\"2\", \n" +
                "\"currentLocation\":\"05-13\", \n" +
                "\"destination\":\"12-12\", \n" +
                "\"carrierStatus\":\"SH\", \n" +
                "\"carrierNumber\":\"1234\", \n" +
                "\"dieNumber\":\"TAO\", \n" +
                "\"partProductionVolume\":\"MEDIUM_VOLUME\"}} \n" +
                "{END_OF_MESSAGE}";

        MesJsonInput testJsonInput = new MesJsonInput();

        //make sure the listener map name is correct
        assertEquals("CARRIER_UPDATE", testJsonInput.getMap(carrierUpdateMessage));

        DWCustomDataHolder dwCustomDataHolder = testJsonInput.processInput(carrierUpdateMessage);
        HashMap resultsHashMap = dwCustomDataHolder.getItems();

        assertEquals("CARRIER_UPDATE", resultsHashMap.get("messageType"));
        assertEquals("03/21/2011 13:50:00", resultsHashMap.get("stampingProductionRunTimestamp"));
        assertEquals("12", resultsHashMap.get("productionRunNo"));
        assertEquals("2", resultsHashMap.get("quantity"));
        assertEquals("05-13", resultsHashMap.get("currentLocation"));
        assertEquals("12-12", resultsHashMap.get("destination"));
        assertEquals("SH", resultsHashMap.get("carrierStatus"));
        assertEquals("1234", resultsHashMap.get("carrierNumber"));
        assertEquals("TAO", resultsHashMap.get("dieNumber"));
        assertEquals("MEDIUM_VOLUME", resultsHashMap.get("partProductionVolume"));

    }

    public void testSomething() {
        String carrierUpdateMessage = "{\"GeneralMessage\":{\"messageType\":\"CARRIER_UPDATE\",\"carrierNumber\":105," +
                "\"currentLocation\":\"\",\"destination\":513,\"productionRunTimestamp\":\"                    \"," +
                "\"dieNumber\":\"\",\"quantity\":0,\"carrierStatus\":\"\"}}";
        MesJsonInput testJsonInput = new MesJsonInput();

        //make sure the listener map name is correct
        assertEquals("CARRIER_UPDATE", testJsonInput.getMap(carrierUpdateMessage));
        DWCustomDataHolder dwCustomDataHolder = testJsonInput.processInput(carrierUpdateMessage);
        HashMap resultsHashMap = dwCustomDataHolder.getItems();

        assertEquals("CARRIER_UPDATE", resultsHashMap.get("messageType"));
        assertEquals("105", resultsHashMap.get("carrierNumber"));
        assertEquals("", resultsHashMap.get("currentLocation"));
        assertEquals("513", resultsHashMap.get("destination"));
        assertEquals("", resultsHashMap.get("dieNumber"));
        assertEquals("0", resultsHashMap.get("quantity"));
        assertEquals("", resultsHashMap.get("carrierStatus"));
    }

    public void testPingMessage() {
        String pingMessage = "{\"GeneralMessage\": {\n" +
                "\"messageType\":\"PING\"}} \n" +
                "{END_OF_MESSAGE}";

        MesJsonInput testJsonInput = new MesJsonInput();

        //make sure the listener map name is correct
        assertEquals("PING", testJsonInput.getMap(pingMessage));

        DWCustomDataHolder dwCustomDataHolder = testJsonInput.processInput(pingMessage);
        HashMap resultsHashMap = dwCustomDataHolder.getItems();

        assertEquals("PING", resultsHashMap.get("messageType"));
    }

    public void testStorageStateRequestMessage() {

        String malformedPingMessage = "{\"GeneralMessage\": {\n" +
                "\"messageType\":\"STORAGE_STATE_REQUEST\"}}} \n" +
                "{END_OF_MESSAGE}";

        MesJsonInput testJsonInput = new MesJsonInput();

        //make sure the listener map name is correct
        assertEquals("STORAGE_STATE_REQUEST", testJsonInput.getMap(malformedPingMessage));

        DWCustomDataHolder dwCustomDataHolder = testJsonInput.processInput(malformedPingMessage);
        HashMap resultsHashMap = dwCustomDataHolder.getItems();

        assertEquals("STORAGE_STATE_REQUEST", resultsHashMap.get("messageType"));
    }

    public void atestMalformedStorageStateRequestMessage() {

        String malformedPingMessage = "{\"GeneralMessage\": {\n" +
                "\"messageType\":\"STORAGE_STATE_REQUEST\"}}} \n" +
                "{END_OF_MESSAGE}";

        MesJsonInput testJsonInput = new MesJsonInput();

        //make sure the listener map name is correct
        assertEquals("STORAGE_STATE_REQUEST", testJsonInput.getMap(malformedPingMessage));

        DWCustomDataHolder dwCustomDataHolder = testJsonInput.processInput(malformedPingMessage);
        HashMap resultsHashMap = dwCustomDataHolder.getItems();

        assertEquals("STORAGE_STATE_REQUEST", resultsHashMap.get("messageType"));

        try {
            //should throw a NullPointerException
            testJsonInput.getMap(malformedPingMessage);
            fail("Should throw a NullPointerException");
        } catch (NullPointerException e) {
        }
    }

    public void atestFormatReplyEmpty() {
        String carrierUpdateMessage = "{\"GeneralMessage\": {\n" +
                "\"messageType\":\"CARRIER_UPDATE\",\n" +
                "\"carrier\":{\n" +
                "\"stampingProductionRunTimestamp\":\"03/21/2011 13:50:00\",\n" +
                "\"productionRunNo\":\"12\", \n" +
                "\"quantity\":\"2\", \n" +
                "\"currentLocation\":\"05-13\", \n" +
                "\"destination\":\"12-12\" \n" +
                "\"carrierStatus\":\"SH\", \n" +
                "\"carrierNumber\":\"1234\", \n" +
                "\"dieNumber\":\"TAO\", \n" +
                "\"partProductionVolume\":\"MEDIUM_VOLUME\"}}} \n" +
                "{END_OF_MESSAGE}";

        MesJsonInput testJsonInput = new MesJsonInput();

        testJsonInput.getMap(carrierUpdateMessage);
        DWCustomDataHolder dwCustomDataHolder = testJsonInput.processInput(carrierUpdateMessage);

        StringBuffer sb = new StringBuffer("");
        byte[] formattedReply = null;

        assertEquals(formattedReply, testJsonInput.formatReplyEmpty(dwCustomDataHolder));

    }
}
