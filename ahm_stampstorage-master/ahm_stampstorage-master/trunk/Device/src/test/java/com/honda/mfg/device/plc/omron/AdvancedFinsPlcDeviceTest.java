package com.honda.mfg.device.plc.omron;

import com.honda.mfg.device.watchdog.WatchdogAdapter;
import org.junit.Test;

import static org.mockito.Mockito.mock;

/**
 * User: Jeffrey M Lutz
 * Date: 4/10/11
 */
public class AdvancedFinsPlcDeviceTest {

    private FinsMemory getMemory() {
        return new FinsMemory(FinsMemory.BANK.DM, 10, 20);
    }

    @Test
    public void successfullyCreateAdvancedFinsPlcDeviceAndExerciseIt() {
        BasicFinsPlcDevice basicFinsPlcDevice = mock(BasicFinsPlcDevice.class);
        WatchdogAdapter watchdogAdapter = mock(WatchdogAdapter.class);
        AdvancedFinsPlcDevice plc = new AdvancedFinsPlcDevice(basicFinsPlcDevice, watchdogAdapter);
        plc.readMemory(getMemory());
        plc.writeMemory(getMemory(), "bogus data");
        plc.readClock();
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionCreatingAdvancedFinsPlcDeviceWithNullDevice() {
        BasicFinsPlcDevice basicFinsPlcDevice = null;
        WatchdogAdapter watchdogAdapter = mock(WatchdogAdapter.class);
        AdvancedFinsPlcDevice plc = new AdvancedFinsPlcDevice(basicFinsPlcDevice, watchdogAdapter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionCreatingAdvancedFinsPlcDeviceWithNullWatchdogAdapter() {
        BasicFinsPlcDevice basicFinsPlcDevice = mock(BasicFinsPlcDevice.class);
        WatchdogAdapter watchdogAdapter = null;
        AdvancedFinsPlcDevice plc = new AdvancedFinsPlcDevice(basicFinsPlcDevice, watchdogAdapter);
    }
}
