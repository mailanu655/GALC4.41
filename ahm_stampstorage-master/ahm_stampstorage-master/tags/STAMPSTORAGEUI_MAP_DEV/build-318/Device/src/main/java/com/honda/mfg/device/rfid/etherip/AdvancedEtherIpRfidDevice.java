package com.honda.mfg.device.rfid.etherip;

import com.honda.mfg.device.rfid.RfidDevice;
import com.honda.mfg.device.rfid.RfidMemory;
import com.honda.mfg.device.watchdog.WatchdogAdapter;

/**
 * User: Jeffrey M Lutz
 * Date: 4/8/11
 */
public class AdvancedEtherIpRfidDevice implements RfidDevice {
    private BasicEtherIpRfidDevice basicRfidDevice;
    private WatchdogAdapter watchdogAdapter;

    public AdvancedEtherIpRfidDevice(BasicEtherIpRfidDevice basicRfidDevice,
                                     WatchdogAdapter watchdogAdapter) {
        if (basicRfidDevice == null) {
            throw new IllegalArgumentException("basicFinsPlcDevice cannot be null and it was null");
        }
        if (watchdogAdapter == null) {
            throw new IllegalArgumentException("watchdogAdapter cannot be null and it was null");
        }
        this.basicRfidDevice = basicRfidDevice;
        this.watchdogAdapter = watchdogAdapter;
    }

    @Override
    public String readTagId() {
        return basicRfidDevice.readTagId();
    }

    @Override
    public String readTag(RfidMemory memory) {
        return basicRfidDevice.readTag(memory);
    }

    @Override
    public void writeTag(RfidMemory memory, String tagValue) {
        basicRfidDevice.writeTag(memory, tagValue);
    }

    @Override
    public String readControllerInfo() {
        return basicRfidDevice.readControllerInfo();
    }
}
