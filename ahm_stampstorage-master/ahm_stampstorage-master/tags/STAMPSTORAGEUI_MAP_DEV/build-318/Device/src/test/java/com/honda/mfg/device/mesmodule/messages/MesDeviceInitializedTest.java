package com.honda.mfg.device.mesmodule.messages;

import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

/**
 * User: vcc30690
 * Date: 4/25/11
 */
public class MesDeviceInitializedTest {

    MesDeviceInitialized deviceInitialized;
    int count;

    @Test
    public void successfullyCreateMesDeviceInitializedMessage() {

        MesDeviceInitialized deviceInitialized = new MesDeviceInitialized();
        assertNotNull(deviceInitialized.getInitializedTime());
    }
}
