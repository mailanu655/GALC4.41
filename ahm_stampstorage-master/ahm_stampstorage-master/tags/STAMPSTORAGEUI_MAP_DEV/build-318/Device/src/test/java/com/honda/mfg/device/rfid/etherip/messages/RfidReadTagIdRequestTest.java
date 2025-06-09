package com.honda.mfg.device.rfid.etherip.messages;

import org.junit.Test;

public class RfidReadTagIdRequestTest extends RfidRequestTestBase {
    RfidCommand cmd = RfidCommand.READ_TAG_ID;

    @Test
    public void buildsValidReadTagRequest() {
        RfidReadTagIdRequest request = new RfidReadTagIdRequest();
        assertRequestConditions(cmd, request);
    }

    @Test
    public void buildsValidReadTagRequestWithNodeId() {
        RfidReadTagIdRequest request = new RfidReadTagIdRequest(NODE_ID_GOOD);
        assertRequestConditions(cmd, request);
    }

    @Test
    public void buildsValidReadTagRequestWithNodeIdAndTimeout() {
        RfidReadTagIdRequest request = new RfidReadTagIdRequest(NODE_ID_GOOD, READ_TIMEOUT_GOOD);
        assertRequestConditions(cmd, request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildsInvalidReadTagRequestByNodeId() {
        RfidReadTagIdRequest request = new RfidReadTagIdRequest(NODE_ID_GOOD, READ_TIMEOUT_BAD);
        assertRequestConditions(cmd, request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildsInvalidReadTagRequestByReadTimeout() {
        RfidReadTagIdRequest request = new RfidReadTagIdRequest(NODE_ID_BAD, READ_TIMEOUT_GOOD);
        assertRequestConditions(cmd, request);
    }
}
