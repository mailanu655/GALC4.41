package com.honda.mfg.device.rfid.etherip.messages;

import org.junit.Test;

public class RfidGetControllerInfoRequestTest extends RfidRequestTestBase {
    RfidCommand cmd = RfidCommand.GET_CONTROLLER_INFO;

    @Test
    public void buildsValidReadTagRequest() {
        RfidGetControllerInfoRequest request = new RfidGetControllerInfoRequest();
        assertRequestConditions(cmd, request);
    }
}
