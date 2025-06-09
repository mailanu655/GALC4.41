package com.honda.mfg.device.mesmodule.messages;

import java.util.Date;

/**
 * User: vcc30690
 * Date: 4/21/11
 */
public class MesDeviceInitialized {
    private Date initializedTimestamp;

    public MesDeviceInitialized() {
        super();
        this.initializedTimestamp = new Date();
    }

    public Date getInitializedTime() {
        return initializedTimestamp;
    }
}
