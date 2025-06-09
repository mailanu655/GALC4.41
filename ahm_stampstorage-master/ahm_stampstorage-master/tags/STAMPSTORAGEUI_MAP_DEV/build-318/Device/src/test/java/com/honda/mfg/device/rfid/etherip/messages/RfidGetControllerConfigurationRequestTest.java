package com.honda.mfg.device.rfid.etherip.messages;

import org.junit.Test;


public class RfidGetControllerConfigurationRequestTest extends RfidRequestTestBase {
    RfidCommand cmd = RfidCommand.GET_CONTROLLER_CONFIG;

    @Test
    public void buildsValidReadTagRequest() {
        RfidGetControllerConfigurationRequest request = new RfidGetControllerConfigurationRequest(NODE_ID_GOOD);
        assertRequestConditions(cmd, request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildsInvalidReadTagRequestByNodeId() {
        RfidGetControllerConfigurationRequest request = new RfidGetControllerConfigurationRequest(NODE_ID_BAD);
        assertRequestConditions(cmd, request);
    }

    @Test
    public void buildsValidReadTagRequestDefaultConstructor() {
        RfidGetControllerConfigurationRequest request = new RfidGetControllerConfigurationRequest();
        assertRequestConditions(cmd, request);
    }
}
