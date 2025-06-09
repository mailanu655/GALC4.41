package com.honda.mfg.device.rfid.etherip.messages;

/**
 * User: Jeffrey M Lutz
 * Date: Nov 9, 2010
 */
public class RfidGetControllerConfigurationRequest extends RfidRequestBase {

    public RfidGetControllerConfigurationRequest() {
        this(DEFAULT_NODE_ID);
    }

    public RfidGetControllerConfigurationRequest(int nodeId) {
        super(RfidCommand.GET_CONTROLLER_CONFIG, nodeId, READ_TIMEOUT_MIN, START_ADDRESS_MIN, WORD_SIZE_MIN);
    }
}
