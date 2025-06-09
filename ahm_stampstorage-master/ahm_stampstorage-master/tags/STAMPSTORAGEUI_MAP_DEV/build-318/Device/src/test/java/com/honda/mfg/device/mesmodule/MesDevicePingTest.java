package com.honda.mfg.device.mesmodule;

import com.honda.eventbus.EventBusConfig;
import com.honda.mfg.device.exceptions.CommunicationsException;
import com.honda.mfg.device.exceptions.NetworkCommunicationException;
import com.honda.mfg.device.exceptions.ResponseTimeoutException;
import com.honda.mfg.device.watchdog.DevicePing;
import org.bushe.swing.event.EventServiceExistsException;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * User: vcc30690
 * Date: 4/11/11
 */
public class MesDevicePingTest {
    public void before() throws EventServiceExistsException {
        EventBusConfig.setSingleThreadMode();
    }

    @Test
    public void successfullyPerformsValidPing() {
        // Pre-condition setup
        MesDevice mes = mock(MesDevice.class);
        when(mes.isHealthy()).thenReturn(true);

        // Perform test
        DevicePing ping = new MesDevicePing(mes);
        boolean actual = ping.ping();

        // Assert post condition
        assertEquals("isHealthy device unsuccessfully", true, actual);
    }

    @Test
    public void failsPerformingPingDueToDeviceError() {
        // Pre-condition setup
        MesDevice mes = mock(MesDevice.class);
        doThrow(new CommunicationsException("Comm error")).when(mes).isHealthy();
        MesDevice mes2 = mock(MesDevice.class);
        doThrow(new NetworkCommunicationException("Comm error")).when(mes2).isHealthy();
        MesDevice mes3 = mock(MesDevice.class);
        when(mes3.isHealthy()).thenThrow(new ResponseTimeoutException());
        // Perform test
        DevicePing ping = new MesDevicePing(mes);
        boolean actual = ping.ping();

        DevicePing ping2 = new MesDevicePing(mes2);
        boolean actual2 = ping2.ping();

        DevicePing ping3 = new MesDevicePing(mes3);
        boolean actual3 = ping3.ping();

        // Assert post condition
        assertEquals("isHealthy device successfully", false, actual);
        assertEquals("ping2 device successfully", false, actual2);
        assertEquals("isHealthy device successfully", false, actual3);
    }


}
