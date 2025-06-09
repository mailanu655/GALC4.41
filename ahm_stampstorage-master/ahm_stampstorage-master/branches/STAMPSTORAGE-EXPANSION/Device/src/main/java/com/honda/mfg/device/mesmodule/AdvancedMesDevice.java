package com.honda.mfg.device.mesmodule;

import com.honda.mfg.device.mesmodule.messages.GeneralMessage;
import com.honda.mfg.device.mesmodule.messages.MesMessage;
import com.honda.mfg.device.watchdog.WatchdogAdapter;

/**
 * User: vcc30690
 * Date: 4/11/11
 */
public class AdvancedMesDevice implements MesDevice {
    private BasicMesDevice basicMesDevice;


    private WatchdogAdapter watchdogAdapter;

    public AdvancedMesDevice(BasicMesDevice basicMesDevice,
                             WatchdogAdapter watchdogAdapter) {
        if (basicMesDevice == null) {
            throw new IllegalArgumentException("basicMesDevice cannot be null and it was null");
        }
        if (watchdogAdapter == null) {
            throw new IllegalArgumentException("watchdogAdapter cannot be null and it was null");
        }
        this.basicMesDevice = basicMesDevice;
        this.watchdogAdapter = watchdogAdapter;
    }


    @Override
    public void sendMessage(MesMessage request) {
        basicMesDevice.sendMessage(request);
    }

    @Override
    public MesMessage sendMessage(MesMessage request, int timeoutSec) {
        return basicMesDevice.sendMessage(request, timeoutSec);
    }

    @Override
    public boolean isHealthy() {
        return basicMesDevice.isHealthy();
    }

    GeneralMessage getGeneralMessage() {
        return basicMesDevice.getGeneralMessage();
    }
}
