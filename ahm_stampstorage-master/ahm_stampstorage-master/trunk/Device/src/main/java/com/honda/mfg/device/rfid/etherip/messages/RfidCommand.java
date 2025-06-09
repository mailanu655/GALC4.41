package com.honda.mfg.device.rfid.etherip.messages;

/**
 * User: Jeffrey M Lutz
 * Date: Nov 16, 2010
 */
public enum RfidCommand {
    CLEAR_PENDING_RESPONSES(121),
    GET_CONTROLLER_CONFIG(51),
    GET_CONTROLLER_INFO(56),
    READ_TAG(5),
    WRITE_TAG(6),
    READ_TAG_ID(7),
    ERROR(255),
    UNKNOWN(100);
    private int code;

    private RfidCommand(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
