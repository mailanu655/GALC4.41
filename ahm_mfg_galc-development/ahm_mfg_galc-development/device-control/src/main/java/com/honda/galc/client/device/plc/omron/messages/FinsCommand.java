package com.honda.galc.client.device.plc.omron.messages;

/**
 * User: Jeffrey M Lutz
 * Date: Oct 26, 2010
 */
public enum FinsCommand {
    INITIALIZE(00), MEMORY_READ(11), MEMORY_WRITE(12), CLOCK_READ(71), UNKNOWN(100);
    private int code;

    private FinsCommand(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public int getRequestCode() {
        return (getCode() / 10) % 10;
    }

    public int getSubrequestCode() {
        return getCode() % 10;
    }
}
