package com.honda.mfg.connection.watchdog;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.honda.mfg.connection.watchdog.DevicePing;
import com.honda.mfg.connection.watchdog.Watchdog;
import com.honda.mfg.connection.watchdog.WatchdogAdapter;

import static org.mockito.Mockito.*;

/**
 * User: Jeffrey M Lutz
 * Date: 4/3/11
 */
public class WatchdogAdapterTest {
    private static final Logger LOG = LoggerFactory.getLogger(WatchdogAdapterTest.class);

    @Test
    public void successfullyRunWatchdogAdapterWithPolling() {
        // Pre-condition setup
        Watchdog watchdog = mock(Watchdog.class);

        DevicePing devicePing = mock(DevicePing.class);
        when(devicePing.ping())
                .thenReturn(false)
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);

        // Perform test
        WatchdogAdapterInterface watchdogAdapter;
        watchdogAdapter = new WatchdogAdapter(watchdog, devicePing, 1, false);

        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < 6000) {
        }
        watchdogAdapter.stopRunning();
        verify(watchdog, atLeast(2)).healthyEvent();
        verify(watchdog, atLeast(2)).unhealthyEvent();
    }

    @Test
    public void successfullyRunWatchdogAdapter() {
        // Pre-condition setup
        Watchdog watchdog = mock(Watchdog.class);

        DevicePing devicePing = mock(DevicePing.class);
        when(devicePing.ping())
                .thenReturn(false)
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);

        // Perform test
        WatchdogAdapter watchdogAdapter;
        watchdogAdapter = new WatchdogAdapter(watchdog, devicePing);
        watchdogAdapter.setPassivePing(false);
        watchdogAdapter.run();
        verify(watchdog, times(0)).healthyEvent();
        verify(watchdog, times(1)).unhealthyEvent();

        watchdogAdapter.run();
        verify(watchdog, times(1)).healthyEvent();
        verify(watchdog, times(1)).unhealthyEvent();

        watchdogAdapter.run();
        verify(watchdog, times(2)).healthyEvent();
        verify(watchdog, times(1)).unhealthyEvent();

        watchdogAdapter.run();
        verify(watchdog, times(2)).healthyEvent();
        verify(watchdog, times(2)).unhealthyEvent();
    }
}
