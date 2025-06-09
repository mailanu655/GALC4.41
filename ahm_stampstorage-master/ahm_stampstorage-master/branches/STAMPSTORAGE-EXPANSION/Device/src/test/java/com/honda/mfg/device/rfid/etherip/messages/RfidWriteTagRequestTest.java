package com.honda.mfg.device.rfid.etherip.messages;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;


public class RfidWriteTagRequestTest extends RfidRequestTestBase {
    private final String MAX_TAG_VALUE = "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012";
    RfidCommand WRITE_TAG_COMMAND = RfidCommand.WRITE_TAG;

    @Test
    public void buildsValidWriteTagRequestEmpty() {
        RfidWriteTagRequest request =
                new RfidWriteTagRequest(START_ADDRESS_GOOD, TAG_VALUE_GOOD);
        assertRequestConditions(WRITE_TAG_COMMAND, request);
    }

    @Test
    public void buildsValidWriteTagRequest() {
        RfidWriteTagRequest request =
                new RfidWriteTagRequest(NODE_ID_GOOD, READ_TIMEOUT_GOOD, START_ADDRESS_GOOD, TAG_VALUE_GOOD);
        assertRequestConditions(WRITE_TAG_COMMAND, request);
    }

    @Test
    public void buildsValidWriteTagRequestWithMaxValue() {
        RfidWriteTagRequest request =
                new RfidWriteTagRequest(NODE_ID_GOOD, READ_TIMEOUT_GOOD, START_ADDRESS_GOOD, MAX_TAG_VALUE);
        assertRequestConditions(WRITE_TAG_COMMAND, request);
    }

    @Test
    public void buildsInvalidWriteTagRequestWithTooBigValue() {
        String TOO_BIG_TAG_VALUE = "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123";
        RfidWriteTagRequest expectedRequest =
                new RfidWriteTagRequest(NODE_ID_GOOD, READ_TIMEOUT_GOOD, START_ADDRESS_GOOD, MAX_TAG_VALUE);
        RfidWriteTagRequest request =
                new RfidWriteTagRequest(NODE_ID_GOOD, READ_TIMEOUT_GOOD, START_ADDRESS_GOOD, TOO_BIG_TAG_VALUE);
        assertRequestConditions(WRITE_TAG_COMMAND, request);
        assertEquals(expectedRequest.getMessageRequest(), request.getMessageRequest());
    }

    @Test
    public void buildsInvalidWriteTagRequestWithTooBigValueAndNonZeroStartAddress() {
        String TOO_BIG_TAG_VALUE = "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123";
        RfidWriteTagRequest expectedRequest =
                new RfidWriteTagRequest(NODE_ID_GOOD, READ_TIMEOUT_GOOD, START_ADDRESS_GOOD + 4, MAX_TAG_VALUE);
        RfidWriteTagRequest request =
                new RfidWriteTagRequest(NODE_ID_GOOD, READ_TIMEOUT_GOOD, START_ADDRESS_GOOD + 4, TOO_BIG_TAG_VALUE);
        assertRequestConditions(WRITE_TAG_COMMAND, request);
        assertEquals(expectedRequest.getMessageRequest(), request.getMessageRequest());
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildsInvalidWriteTagRequestByNodeId() {
        RfidWriteTagRequest request = new RfidWriteTagRequest(NODE_ID_BAD, READ_TIMEOUT_GOOD, START_ADDRESS_GOOD, TAG_VALUE_GOOD);
        assertRequestConditions(WRITE_TAG_COMMAND, request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildsInvalidWriteTagRequestByWriteTimeout() {
        RfidWriteTagRequest request = new RfidWriteTagRequest(NODE_ID_GOOD, READ_TIMEOUT_BAD, START_ADDRESS_GOOD, TAG_VALUE_GOOD);
        assertRequestConditions(WRITE_TAG_COMMAND, request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildsInvalidWriteTagRequestByStartAddress() {
        RfidWriteTagRequest request = new RfidWriteTagRequest(NODE_ID_GOOD, READ_TIMEOUT_GOOD, START_ADDRESS_BAD, TAG_VALUE_GOOD);
        assertRequestConditions(WRITE_TAG_COMMAND, request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildsInvalidWriteTagRequestByWordSize() {
        RfidWriteTagRequest request = new RfidWriteTagRequest(NODE_ID_GOOD, READ_TIMEOUT_GOOD, START_ADDRESS_GOOD, TAG_VALUE_BAD);
        assertRequestConditions(WRITE_TAG_COMMAND, request);
    }

}
