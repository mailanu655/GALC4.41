package com.honda.mfg.device.rfid.etherip.messages;

import org.junit.Test;


public class RfidReadTagRequestTest extends RfidRequestTestBase {
    RfidCommand cmd = RfidCommand.READ_TAG;

    @Test
    public void buildsValidReadTagRequestShort() {
        RfidReadTagRequest request = new RfidReadTagRequest(START_ADDRESS_GOOD, WORD_SIZE_GOOD);
        assertRequestConditions(cmd, request);
    }

    @Test
    public void buildsValidReadTagRequest() {
        RfidReadTagRequest request = new RfidReadTagRequest(NODE_ID_GOOD, READ_TIMEOUT_GOOD, START_ADDRESS_GOOD, WORD_SIZE_GOOD);
        assertRequestConditions(cmd, request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildsInvalidReadTagRequestByNodeId() {
        RfidReadTagRequest request = new RfidReadTagRequest(NODE_ID_BAD, READ_TIMEOUT_GOOD, START_ADDRESS_GOOD, WORD_SIZE_GOOD);
        assertRequestConditions(cmd, request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildsInvalidReadTagRequestByReadTimeout() {
        RfidReadTagRequest request = new RfidReadTagRequest(NODE_ID_GOOD, READ_TIMEOUT_BAD, START_ADDRESS_GOOD, WORD_SIZE_GOOD);
        assertRequestConditions(cmd, request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildsInvalidReadTagRequestByStartAddress() {
        RfidReadTagRequest request = new RfidReadTagRequest(NODE_ID_GOOD, READ_TIMEOUT_GOOD, START_ADDRESS_BAD, WORD_SIZE_GOOD);
        assertRequestConditions(cmd, request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildsInvalidReadTagRequestByWordSize() {
        RfidReadTagRequest request = new RfidReadTagRequest(NODE_ID_GOOD, READ_TIMEOUT_GOOD, START_ADDRESS_GOOD, WORD_SIZE_BAD);
        assertRequestConditions(cmd, request);
    }

}
