package com.honda.mfg.mesparser;

import com.ils_tech.dw.ei.DWCustomDataHolder;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Properties;

/**
 * User: Adam S. Kendell
 * Date: 5/23/11
 */
public class MesJsonOutputTest extends TestCase {

    private static final String GENERAL_MESSAGE = "GeneralMessage";

    public void testProcessOutput() {

        HashMap map = new HashMap();
        map.put("tagID", "1234567891234567");
        map.put("id", "103");
        map.put("productionRunNo", "12");
        map.put("stampingProductionRunTimestamp", "03/21/2011 13:50:00");
        map.put("currentLocation", "0513");
        map.put("nextLocation", "LN01");
        map.put("messageType", "CARRIER_STATUS");

        DWCustomDataHolder ch = new DWCustomDataHolder(null, null, map);
        MesJsonOutput mesJsonOutput = new MesJsonOutput();
        String jsonMessage = mesJsonOutput.processOutput(ch);
        JsonToPropertiesConverter converter = new JsonToPropertiesConverter(jsonMessage);
        Properties actualJsonProperties = converter.getProperties();

        assertEquals("1234567891234567", actualJsonProperties.getProperty("tagID"));
        assertEquals("103", actualJsonProperties.getProperty("id"));
        assertEquals("12", actualJsonProperties.getProperty("productionRunNo"));
        assertEquals("03/21/2011 13:50:00", actualJsonProperties.getProperty("stampingProductionRunTimestamp"));
        assertEquals("0513", actualJsonProperties.getProperty("currentLocation"));
        assertEquals("LN01", actualJsonProperties.getProperty("nextLocation"));
        assertEquals("CARRIER_STATUS", actualJsonProperties.getProperty("messageType"));

    }

    public void testGetMap() {
        String jsonMessage = "{\"GeneralMessage\":{\"" +
                "currentLocation\":\"0513\",\"" +
                "id\":\"103\",\"" +
                "nextLocation\":\"LN01\",\"" +
                "productionRunNo\":\"12\",\"" +
                "stampingProductionRunTimestamp\":\"03/21/2011 13:50:00\",\"" +
                "tagID\":\"1234567891234567\",\"" +
                "messageType\":\"CARRIER_STATUS\"}}";

        MesJsonOutput mesJsonOutput = new MesJsonOutput();
        String expectedListenerMapId = mesJsonOutput.getMap(jsonMessage);
// TODO Adam, Please fix this.
        assertEquals("CARRIER_STATUS", expectedListenerMapId);
    }

    public void testFormatReply() {

    }

    public void testFormatErrorMessage() {

    }

}
