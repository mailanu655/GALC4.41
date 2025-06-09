package com.honda.mfg.device.mesmodule;

import com.honda.mfg.device.mesmodule.messages.MesMessage;

/**
 * User: vcc30690
 * Date: 4/11/11
 */
public interface MesDevice {

    public void sendMessage(MesMessage request);

    public MesMessage sendMessage(MesMessage request, int timeoutSec);

    public boolean isHealthy();
}
