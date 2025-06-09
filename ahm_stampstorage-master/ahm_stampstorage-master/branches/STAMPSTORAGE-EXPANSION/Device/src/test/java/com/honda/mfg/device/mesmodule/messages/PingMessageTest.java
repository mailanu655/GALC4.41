package com.honda.mfg.device.mesmodule.messages;

import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * User: Jeffrey M Lutz
 * Date: 5/23/11
 */
public class PingMessageTest {
    @Test
    public void successfullyConstructPingTextMessage() {
        String expectedMsg = "{\"GeneralMessage\":{\"messageType\":\"PING\",\"lastStoreAndForwardEventTime\":\"\",\"lastStoreAndForwardState\":\"100\"}}";
        PingMessage ping = new PingMessage();
        String actualMsg = ping.getMessage();
        String actualTime = ping.getLastStoreAndForwardEventTime();
        assertEquals("", actualTime);
        assertEquals(expectedMsg, actualMsg);
        assertFalse(ping.isHealthy());
    }

    @Test
    public void successfullyConstructPingTextWithUnhealthyResponseMessage() {
        String expectedMsg = "{\"GeneralMessage\":{\"messageType\":\"PING\",\"lastStoreAndForwardEventTime\":\"2011-06-18 11:52:37.\",\"lastStoreAndForwardState\":\"100\"}}";
        PingMessage ping = new PingMessage(expectedMsg);
        String actualMsg = ping.getMessage();
        String actualTime = ping.getLastStoreAndForwardEventTime();
        assertEquals("2011-06-18 11:52:37.", actualTime);
        assertEquals(expectedMsg, actualMsg);
        assertFalse(ping.isHealthy());
    }

    @Test
    public void successfullyConstructPingTextWithHealthyResponseMessage() {
        String expectedMsg = "{\"GeneralMessage\":{\"messageType\":\"PING\",\"lastStoreAndForwardEventTime\":\"\",\"lastStoreAndForwardState\":\"101\"}}";
        PingMessage ping = new PingMessage(expectedMsg);
        String actualMsg = ping.getMessage();
        assertEquals(expectedMsg, actualMsg);
        assertTrue(ping.isHealthy());
    }

    @Test
    public void testMe() {
        String expectedMsg = "{\"GeneralMessage\":{\"lastStoreAndForwardEventTime\":\"2011-07-07 09:59:47.883\",\"lastStoreAndForwardState\":\"102\",\"messageType\":\"PING\"}}";
        PingMessage ping = new PingMessage(expectedMsg);
        assertFalse(ping.isHealthy());
    }

    @Test
    public void successfullyConstructPingTextWithHealthyStatusMessage() {
        String expectedMsg = "{\"GeneralMessage\":{\"messageType\":\"PING\",\"lastStoreAndForwardEventTime\":\"\",\"lastStoreAndForwardState\":\"101\"}}";
        PingMessage ping = new PingMessage("", true);
        String actualMsg = ping.getMessage();
        assertEquals(expectedMsg, actualMsg);
        assertTrue(ping.isHealthy());
    }

    @Test
    public void successfullyConstructPingTextWithUnhealthyStatusMessage() {
        String expectedMsg = "{\"GeneralMessage\":{\"messageType\":\"PING\",\"lastStoreAndForwardEventTime\":\"\",\"lastStoreAndForwardState\":\"100\"}}";
        PingMessage ping = new PingMessage("", false);
        String actualMsg = ping.getMessage();
        assertEquals(expectedMsg, actualMsg);
        assertFalse(ping.isHealthy());
    }
}
