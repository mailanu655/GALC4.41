package com.honda.mfg.device.rfid.etherip.messages;

/**
 * User: Jeffrey M Lutz
 * Date: Nov 9, 2010
 */
public class RfidGetControllerInfoRequest extends RfidRequestBase {

    public RfidGetControllerInfoRequest() {
        super(RfidCommand.GET_CONTROLLER_INFO, DEFAULT_NODE_ID, READ_TIMEOUT_MIN, START_ADDRESS_MIN, WORD_SIZE_MIN);
    }
}
