package com.honda.mfg.device.rfid.etherip.messages;

import com.honda.mfg.device.messages.MessageBase;

/**
 * User: Jeffrey M Lutz
 * Date: Dec 6, 2010
 */
public class RfidMessageBase extends MessageBase {
    static final int DEFAULT_NODE_ID = 1;
    static final int DEFAULT_READ_TIMEOUT = 1000;
    static final int NODE_ID_MIN = 1;
    static final int NODE_ID_MAX = 255;
    static final int READ_TIMEOUT_MIN = 0;
    static final int READ_TIMEOUT_MAX = 65535;
    static final int START_ADDRESS_MIN = 0;
    static final int START_ADDRESS_MAX = 247;
    static final int START_ADDRESS_NOT_APPLICABLE = -1;
    static final int WORD_SIZE_MIN = 0;
    static final int WORD_SIZE_MAX = START_ADDRESS_MAX;
    static final int WORD_SIZE_NOT_APPLICABLE = START_ADDRESS_NOT_APPLICABLE;
    static final int WORD_LENGTH_INDEX = 1;
    static final int INSTANCE_COUNTER_MIN = 0;
    static final int INSTANCE_COUNTER_MAX = 255;

    private RfidCommand rfidCommand;

    String getCommandString() {
        StringBuilder b = new StringBuilder();
        b.append(asChars(170, 1));
        b.append(asChars(rfidCommand.getCode(), 1));
        return b.toString();
    }

    public RfidCommand getRfidCommand() {
        return rfidCommand;
    }

    protected void setRfidCommand(RfidCommand rfidCommand) {
        this.rfidCommand = rfidCommand;
    }
}
