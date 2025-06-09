package com.honda.mfg.connection.processor;

import com.honda.mfg.connection.processor.AdvancedConnection;
import com.honda.mfg.connection.processor.BasicConnection;
import com.honda.mfg.connection.processor.messages.GeneralMessage;
import com.honda.mfg.connection.watchdog.WatchdogAdapter;
import com.honda.mfg.connection.watchdog.WatchdogAdapterInterface;

import org.junit.Test;

import static org.mockito.Mockito.mock;

/**
 * User: vcc30690
 * Date: 4/12/11
 */
public class AdvancedConnectionTest {

    @Test
    public void successfullyCreateAdvancedMesDeviceAndExerciseIt() {
        BasicConnection basicConnection = mock(BasicConnection.class);
        WatchdogAdapterInterface watchdogAdapter = mock(WatchdogAdapter.class);
        AdvancedConnection mes = new AdvancedConnection(basicConnection, watchdogAdapter);
        mes.isHealthy();
        mes.sendMessage(new GeneralMessage("hello"));
        mes.sendMessage(new GeneralMessage("hello"), 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionCreatingAdvancedMesDeviceWithNullDevice() {
        BasicConnection basicConnection = null;
        WatchdogAdapterInterface watchdogAdapter = mock(WatchdogAdapter.class);
        AdvancedConnection mes = new AdvancedConnection(basicConnection, watchdogAdapter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionCreatingAdvancedMesDeviceWithNullWatchdogAdapter() {
        BasicConnection basicConnection = mock(BasicConnection.class);
        WatchdogAdapterInterface watchdogAdapter = null;
        AdvancedConnection mes = new AdvancedConnection(basicConnection, watchdogAdapter);
    }
}
