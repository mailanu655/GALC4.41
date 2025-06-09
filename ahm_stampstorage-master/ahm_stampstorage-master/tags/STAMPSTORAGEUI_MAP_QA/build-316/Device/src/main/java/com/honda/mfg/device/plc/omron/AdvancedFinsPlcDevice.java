package com.honda.mfg.device.plc.omron;

import com.honda.mfg.device.plc.PlcDevice;
import com.honda.mfg.device.plc.PlcMemory;
import com.honda.mfg.device.watchdog.WatchdogAdapter;

/**
 * User: Jeffrey M Lutz
 * Date: 4/8/11
 */
public class AdvancedFinsPlcDevice implements PlcDevice {
    private BasicFinsPlcDevice basicFinsPlcDevice;
    private WatchdogAdapter watchdogAdapter;

    public AdvancedFinsPlcDevice(BasicFinsPlcDevice basicFinsPlcDevice,
                                 WatchdogAdapter watchdogAdapter) {
        if (basicFinsPlcDevice == null) {
            throw new IllegalArgumentException("basicFinsPlcDevice cannot be null and it was null");
        }
        if (watchdogAdapter == null) {
            throw new IllegalArgumentException("watchdogAdapter cannot be null and it was null");
        }
        this.basicFinsPlcDevice = basicFinsPlcDevice;
        this.watchdogAdapter = watchdogAdapter;
    }

    @Override
    public String readMemory(PlcMemory memory) {
        return basicFinsPlcDevice.readMemory(memory);
    }

    @Override
    public void writeMemory(PlcMemory memory, String data) {
        basicFinsPlcDevice.writeMemory(memory, data);
    }

    @Override
    public String readClock() {
        return basicFinsPlcDevice.readClock();
    }
}
