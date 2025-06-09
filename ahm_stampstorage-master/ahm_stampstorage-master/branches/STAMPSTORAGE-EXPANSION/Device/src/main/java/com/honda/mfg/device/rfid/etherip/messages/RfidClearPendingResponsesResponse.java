package com.honda.mfg.device.rfid.etherip.messages;

import java.util.Date;

/**
 * User: Jeffrey M Lutz
 * Date: Nov 16, 2010
 */
public class RfidClearPendingResponsesResponse extends RfidResponseBase {

    public RfidClearPendingResponsesResponse(int instanceCounter, int nodeId, Date responseDate) {
        super(RfidCommand.CLEAR_PENDING_RESPONSES, instanceCounter, nodeId, responseDate);
        /**     +-> Length (in words)
         *      |  +-> Literal (0xAA)
         *      |  |  +-> Command (0x79)
         *      |  |  |  +-> Instance Counter (hex)
         *      |  |  |  |  +-> Node ID (hex)
         *      |  |  |  |  |  +-> Month (hex)
         *      |  |  |  |  |  |  +-> Day (hex)
         *      |  |  |  |  |  |  |  +-> Hours (hex)
         *      |  |  |  |  |  |  |  |  +-> Minutes (hex)
         *      |  |  |  |  |  |  |  |  |  +-> Seconds (hex)
         *      |  |  |  |  |  |  |  |  |  |  +-> Always 0x00
         * [00 06 AA 79 00 01 0B 09 0C 2A 22 00 ]
         * Clear Pending Responses Complete
         */
        initializeResponse();
    }

    String getSuffix() {
        StringBuffer b = new StringBuffer();
        b.append(asChars(0, 1));
        return b.toString();
    }
}
