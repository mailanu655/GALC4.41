package com.honda.mfg.device.mesmodule;

import com.honda.mfg.device.watchdog.DevicePing;

/**
 * User: vcc30690
 * Date: 4/11/11
 */
public class MesDevicePing implements DevicePing {
    private MesDevice mesDevice;

    public MesDevicePing(MesDevice mesDevice) {
        this.mesDevice = mesDevice;
    }

    @Override
    public boolean ping() {
        boolean pingReading;
        try {
            pingReading = mesDevice.isHealthy();
        } catch (RuntimeException e) {
            return false;
        }
        return pingReading;
    }

}
