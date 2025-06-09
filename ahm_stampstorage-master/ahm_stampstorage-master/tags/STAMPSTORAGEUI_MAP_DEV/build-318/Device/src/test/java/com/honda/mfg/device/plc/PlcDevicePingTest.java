package com.honda.mfg.device.plc;

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
 * Date: 4/3/11
 */
public class PlcDevicePingTest {

    public void before() throws EventServiceExistsException {
        EventBusConfig.setSingleThreadMode();
    }

    @Test
    public void successfullyPerformsValidPing() {
        // Pre-condition setup
        PlcDevice plc = mock(PlcDevice.class);
        when(plc.readClock()).thenReturn("Some Clock reading");

        // Perform test
        DevicePing ping = new PlcDevicePing(plc);
        boolean actual = ping.ping();

        // Assert post condition
        assertEquals("isHealthy device unsuccessfully", true, actual);
    }

    @Test
    public void failsPerformingPingDueToNullClockReading() {
        // Pre-condition setup
        PlcDevice plc = mock(PlcDevice.class);
        when(plc.readClock()).thenReturn(null);

        // Perform test
        DevicePing ping = new PlcDevicePing(plc);
        boolean actual = ping.ping();

        // Assert post condition
        assertEquals("Unable to isHealthy device successfully", false, actual);
    }

    @Test
    public void failsPerformingPingDueToEmptyClockReading() {
        // Pre-condition setup
        PlcDevice plc = mock(PlcDevice.class);
        when(plc.readClock()).thenReturn("");

        // Perform test
        DevicePing ping = new PlcDevicePing(plc);
        boolean actual = ping.ping();

        // Assert post condition
        assertEquals("isHealthy device successfully", false, actual);
    }

    @Test
    public void failsPerformingPingDueToDeviceError() {
        // Pre-condition setup
        PlcDevice plc = mock(PlcDevice.class);
        doThrow(new CommunicationsException("Comm error")).when(plc).readClock();
        PlcDevice plc2 = mock(PlcDevice.class);
        doThrow(new NetworkCommunicationException("Comm error")).when(plc2).readClock();

        // Perform test
        DevicePing ping = new PlcDevicePing(plc);
        boolean actual = ping.ping();

        DevicePing ping2 = new PlcDevicePing(plc2);
        boolean actual2 = ping2.ping();

        // Assert post condition
        assertEquals("isHealthy device successfully", false, actual);
        assertEquals("ping2 device successfully", false, actual2);
    }
}
