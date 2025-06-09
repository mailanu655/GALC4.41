package com.honda.mfg.mesparser;

import junit.framework.TestCase;

import java.util.Properties;

/**
 * User: Jeffrey M Lutz
 * Date: 5/20/11
 */
public class JsonFromPropertiesConverterTest extends TestCase {

    private static final String GENERAL_MESSAGE = "GeneralMessage";

    public void testSingleKeyValueToJson() {
        String expectedKey = "dog";
        String expectedValue = "cow";
        Properties p = new Properties();
        p.setProperty(expectedKey, expectedValue);
        JsonFromPropertiesConverter c = new JsonFromPropertiesConverter(p);
        String json = c.getJson();

        assertEquals("{\"GeneralMessage\":{\"dog\":\"cow\"}}", json);
    }

    public void testArrayedKeyValueToJson() {
        String expectedKey = "dog";
        String expectedValue = "cow";
        Properties p = new Properties();
        p.setProperty(expectedKey + "[1]", expectedValue + "_1");
        p.setProperty(expectedKey + "[2]", expectedValue + "_2");
        JsonFromPropertiesConverter c = new JsonFromPropertiesConverter(p);
        String json = c.getJson();

        assertEquals("{\"GeneralMessage\":{\"dog[1]\":\"cow_1\",\"dog[2]\":\"cow_2\"}}", json);
    }

    public void testMultipleKeyValuesToJson() {
        String expectedKey = "dog";
        String expectedValue = "cow";
        Properties p = new Properties();
        p.setProperty(expectedKey, expectedValue);
        p.setProperty("bird", "cat");
        JsonFromPropertiesConverter c = new JsonFromPropertiesConverter(p);
        String json = c.getJson();

        assertEquals("{\"GeneralMessage\":{\"bird\":\"cat\",\"dog\":\"cow\"}}", json);
    }
    //carrier_carrierNumber, carrier_tagID, carrier_dieNumber, messageType

    public void testMultipleKeyValuesWithDepthToJson() {
        String expectedKey = "carrier[1]_carrierNumber";
        String expectedValue = "123";
        Properties p = new Properties();
        p.setProperty(expectedKey, expectedValue);
        p.setProperty("carrier[1]_carrierNumber", "123");
        p.setProperty("carrier[1]_dieNumber", "456");
        p.setProperty("carrier[2]_carrierNumber", "123");
        p.setProperty("carrier[2]_dieNumber", "1");
        JsonFromPropertiesConverter c = new JsonFromPropertiesConverter(p);
        String json = c.getJson();

        assertEquals("{\"GeneralMessage\":{\"carrier[1]\":{\"carrierNumber\":\"123\",\"dieNumber\":\"456\"},\"carrier[2]\":{\"carrierNumber\":\"123\",\"dieNumber\":\"1\"}}}", json);
    }
}
