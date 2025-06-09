package com.honda.mfg.device.rfid.etherip.messages;

import org.junit.Test;

public class RfidClearPendingResponsesRequestTest extends RfidRequestTestBase {
    RfidCommand cmd = RfidCommand.CLEAR_PENDING_RESPONSES;

    @Test
    public void buildsValidReadTagRequest() {
        RfidClearPendingResponsesRequest request = new RfidClearPendingResponsesRequest(NODE_ID_GOOD);
        assertRequestConditions(cmd, request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildsInvalidReadTagRequestByNodeId() {
        RfidClearPendingResponsesRequest request = new RfidClearPendingResponsesRequest(NODE_ID_BAD);
        assertRequestConditions(cmd, request);
    }

    @Test
    public void buildsValidReadTagRequestDefaultConstructor() {
        RfidClearPendingResponsesRequest request = new RfidClearPendingResponsesRequest();
        assertRequestConditions(cmd, request);
    }
}
