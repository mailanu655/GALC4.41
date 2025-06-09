package com.honda.mfg.device.rfid.etherip.messages;

/**
 * User: Jeffrey M Lutz
 * Date: Nov 9, 2010
 */
public class RfidClearPendingResponsesRequest extends RfidRequestBase {

    public RfidClearPendingResponsesRequest() {
        this(DEFAULT_NODE_ID);
    }

    public RfidClearPendingResponsesRequest(int nodeId) {
        super(RfidCommand.CLEAR_PENDING_RESPONSES, nodeId, READ_TIMEOUT_MIN, START_ADDRESS_MIN, WORD_SIZE_MIN);
    }
}
