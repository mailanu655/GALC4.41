package com.honda.mfg.mesparser;

import junit.framework.TestCase;

import java.util.Properties;

/**
 * User: Jeffrey M Lutz
 * Date: 5/20/11
 */
public class JsonToPropertiesConverterTest extends TestCase {

    private static final String GENERAL_MESSAGE = "GeneralMessage";

    public void testSimplestFromJson() {
        JsonToPropertiesConverter jsonToPropertiesConverter = new JsonToPropertiesConverter("{\" " + GENERAL_MESSAGE + " \" : \" RootKey \"}");
        Properties p = jsonToPropertiesConverter.getProperties();
        assertEquals(0, p.size());
    }

    public void testSimplestNumberFromJson() {
        String expectedNumber = "123";
        JsonToPropertiesConverter jsonToPropertiesConverter = new JsonToPropertiesConverter("{\" " + GENERAL_MESSAGE + " \" :  " + expectedNumber + " }");
        Properties p = jsonToPropertiesConverter.getProperties();
        assertEquals(0, p.size());
    }

    public void testManyKeyValuesWithNestingFromJson() {
        String stringKey = "productionRunTimestamp";
        String stringVal = "2011-06-13 12:34:56";
        String intKey = "carrierNumber";
        String intVal = "23";
        String jsonMsg = "{\"GeneralMessage\":{\"" + intKey + "\":" + intVal + ",\"currentLocation\":\"\",\"destination\":13,\"" + stringKey + "\":\"" + stringVal + "\",\"dieNumber\":\"\",\"quantity\":0,\"carrierStatus\":\"\"}}";
        JsonToPropertiesConverter jsonToPropertiesConverter = new JsonToPropertiesConverter(jsonMsg);
        Properties p = jsonToPropertiesConverter.getProperties();
        assertEquals(stringVal, p.getProperty(stringKey));
        assertEquals(intVal, p.getProperty(intKey));
    }

    public void testKeyWithNestedValueFromJson() {
        String expectedKey = "dog";
        String expectedValue = "cow";
        JsonToPropertiesConverter jsonToPropertiesConverter = new JsonToPropertiesConverter(
                "{\" GeneralMessage \" : {\" " + expectedKey + " \" : \" " + expectedValue + " \"} }");
        Properties p = jsonToPropertiesConverter.getProperties();
        assertTrue(p.containsKey(expectedKey));
        assertEquals("cow", p.getProperty(expectedKey));
    }

    public void testFullJsonForStorage() {
        JsonToPropertiesConverter jsonToPropertiesConverter = new JsonToPropertiesConverter("{\"GeneralMessage\" : {\n" +
                "\"messageType\":\"CARRIER_MOVE\",\n" +
                "\"tagID\":\"1234567891234567\",\n" +
                "\"id\":103,\n" +
                "\"productionRunNo\":12,\n" +
                "\"stampingProductionRunTimestamp\":\"03/21/2011 13:50:00\",\n" +
                "\"currentLocation\":\"0513\",\n" +
                "\"nextLocation\":\"LN01\"\n" +
                "}}");
        Properties p = jsonToPropertiesConverter.getProperties();

        String[][] expectedPairs =
                {
                        {"messageType", "CARRIER_MOVE"},
                        {"nextLocation", "LN01"},
                        {"id", "103"},
                        {"productionRunNo", "12"},
                        {"stampingProductionRunTimestamp", "03/21/2011 13:50:00"},
                        {"currentLocation", "0513"},
                        {"tagID", "1234567891234567"},
                };
        assertConditions(p, expectedPairs);
    }

    private void assertConditions(Properties p, String[][] expectedPairs) {
        for (int i = 0; i < expectedPairs.length; i++) {
            String key = expectedPairs[i][0];
            String expectedValue = expectedPairs[i][1];
            assertEquals(expectedValue, p.getProperty(key));
        }
    }
}
