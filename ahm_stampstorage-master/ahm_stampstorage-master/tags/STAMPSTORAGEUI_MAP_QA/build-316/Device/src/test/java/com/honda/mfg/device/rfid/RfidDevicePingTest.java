package com.honda.mfg.device.rfid;

import com.honda.eventbus.EventBusConfig;
import com.honda.mfg.device.exceptions.CommunicationsException;
import com.honda.mfg.device.exceptions.NetworkCommunicationException;
import com.honda.mfg.device.watchdog.DevicePing;
import org.bushe.swing.event.EventServiceExistsException;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * User: Jeffrey M Lutz
 * Date: 4/12/11
 */
public class RfidDevicePingTest {
    public void before() throws EventServiceExistsException {
        EventBusConfig.setSingleThreadMode();
    }

    @Test
    public void successfullyPerformsValidPing() {
        // Pre-condition setup
        RfidDevice plc = mock(RfidDevice.class);
        when(plc.readControllerInfo()).thenReturn("Some controller info");

        // Perform test
        DevicePing ping = new RfidDevicePing(plc);
        boolean actual = ping.ping();

        // Assert post condition
        assertEquals("isHealthy device unsuccessfully", true, actual);
    }

    @Test
    public void failsPerformingPingDueToNullControllerInfo() {
        // Pre-condition setup
        RfidDevice plc = mock(RfidDevice.class);
        when(plc.readControllerInfo()).thenReturn(null);

        // Perform test
        DevicePing ping = new RfidDevicePing(plc);
        boolean actual = ping.ping();

        // Assert post condition
        assertEquals("Unable to isHealthy device successfully", false, actual);
    }

    @Test
    public void failsPerformingPingDueToEmptyControllerInfo() {
        // Pre-condition setup
        RfidDevice plc = mock(RfidDevice.class);
        when(plc.readControllerInfo()).thenReturn("");

        // Perform test
        DevicePing ping = new RfidDevicePing(plc);
        boolean actual = ping.ping();

        // Assert post condition
        assertEquals("isHealthy device successfully", false, actual);
    }

    @Test
    public void failsPerformingPingDueToDeviceError() {
        // Pre-condition setup
        RfidDevice plc = mock(RfidDevice.class);
        doThrow(new CommunicationsException("Comm error")).when(plc).readControllerInfo();
        RfidDevice plc2 = mock(RfidDevice.class);
        doThrow(new NetworkCommunicationException("Comm error")).when(plc2).readControllerInfo();

        // Perform test
        DevicePing ping = new RfidDevicePing(plc);
        boolean actual = ping.ping();

        DevicePing ping2 = new RfidDevicePing(plc2);
        boolean actual2 = ping2.ping();

        // Assert post condition
        assertEquals("isHealthy device successfully", false, actual);
        assertEquals("ping2 device successfully", false, actual2);
    }
}
