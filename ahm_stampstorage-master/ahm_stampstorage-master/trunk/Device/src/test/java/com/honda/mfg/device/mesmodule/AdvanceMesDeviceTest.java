package com.honda.mfg.device.mesmodule;

import com.honda.mfg.device.mesmodule.messages.GeneralMessage;
import com.honda.mfg.device.watchdog.WatchdogAdapter;
import org.junit.Test;

import static org.mockito.Mockito.mock;

/**
 * User: vcc30690
 * Date: 4/12/11
 */
public class AdvanceMesDeviceTest {

    @Test
    public void successfullyCreateAdvancedMesDeviceAndExerciseIt() {
        BasicMesDevice basicMesDevice = mock(BasicMesDevice.class);
        WatchdogAdapter watchdogAdapter = mock(WatchdogAdapter.class);
        AdvancedMesDevice mes = new AdvancedMesDevice(basicMesDevice, watchdogAdapter);
        mes.isHealthy();
        mes.sendMessage(new GeneralMessage("hello"));
        mes.sendMessage(new GeneralMessage("hello"), 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionCreatingAdvancedMesDeviceWithNullDevice() {
        BasicMesDevice basicMesDevice = null;
        WatchdogAdapter watchdogAdapter = mock(WatchdogAdapter.class);
        AdvancedMesDevice mes = new AdvancedMesDevice(basicMesDevice, watchdogAdapter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionCreatingAdvancedMesDeviceWithNullWatchdogAdapter() {
        BasicMesDevice basicMesDevice = mock(BasicMesDevice.class);
        WatchdogAdapter watchdogAdapter = null;
        AdvancedMesDevice mes = new AdvancedMesDevice(basicMesDevice, watchdogAdapter);
    }
}
