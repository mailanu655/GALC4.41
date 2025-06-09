package com.honda.mfg.device.rfid.etherip.messages;

import java.util.Date;

/**
 * User: Jeffrey M Lutz
 * Date: Nov 16, 2010
 */
public class RfidGetControllerConfigurationResponse extends RfidResponseBase {

    public RfidGetControllerConfigurationResponse(int instanceCounter, int nodeId, Date responseDate) {
        super(RfidCommand.GET_CONTROLLER_CONFIG, instanceCounter, nodeId, responseDate);
        /**     +-> Length (in words)
         *      |  +-> Literal (0xAA)
         *      |  |  +-> Command (0x33)
         *      |  |  |  +-> Instance Counter (hex)
         *      |  |  |  |  +-> Node ID (hex)
         *      |  |  |  |  |  +-> Month (hex)
         *      |  |  |  |  |  |  +-> Day (hex)
         *      |  |  |  |  |  |  |  +-> Hours (hex)
         *      |  |  |  |  |  |  |  |  +-> Minutes (hex)
         *      |  |  |  |  |  |  |  |  |  +-> Seconds (hex)
         *      |  |  |  |  |  |  |  |  |  |  +-> addition length in bytes
         * [00 10 AA 33 01 01 0B 09 0C 2A 22 13 00 00 00 70 02 01 00 00 01 00 F8 00 00 00 02 02 01 46 00 00 ]
         * Clear Pending Responses Complete
         */
        initializeResponse();
    }

    String getSuffix() {
        StringBuffer b = new StringBuffer();
        b.append(asChars(19, 1));
        b.append(asChars(0, 1));
        b.append(asChars(0, 1));
        b.append(asChars(0, 1));
        b.append(asChars(112, 1));
        b.append(asChars(2, 1));
        b.append(asChars(1, 1));
        b.append(asChars(0, 1));
        b.append(asChars(0, 1));
        b.append(asChars(1, 1));
        b.append(asChars(0, 1));
        b.append(asChars(248, 1));
        b.append(asChars(0, 1));
        b.append(asChars(0, 1));
        b.append(asChars(0, 1));
        b.append(asChars(2, 1));
        b.append(asChars(2, 1));
        b.append(asChars(1, 1));
        b.append(asChars(70, 1));
        b.append(asChars(0, 1));
        b.append(asChars(0, 1));
        return b.toString();
    }
}
