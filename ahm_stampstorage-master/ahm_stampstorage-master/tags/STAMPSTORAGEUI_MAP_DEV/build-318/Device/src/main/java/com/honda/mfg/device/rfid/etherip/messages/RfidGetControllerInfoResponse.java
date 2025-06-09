package com.honda.mfg.device.rfid.etherip.messages;

import java.util.Date;

/**
 * User: Jeffrey M Lutz
 * Date: Nov 16, 2010
 */
public class RfidGetControllerInfoResponse extends RfidResponseBase {

    public RfidGetControllerInfoResponse(int instanceCounter, int nodeId, Date responseDate) {
        super(RfidCommand.GET_CONTROLLER_INFO, instanceCounter, nodeId, responseDate);
        /**     +-> Length (in words)
         *      |  +-> Literal (0xAA)
         *      |  |  +-> Command (0x38)
         *      |  |  |  +-> Instance Counter (hex)
         *      |  |  |  |  +-> Node ID (hex)
         *      |  |  |  |  |  +-> Month (hex)
         *      |  |  |  |  |  |  +-> Day (hex)
         *      |  |  |  |  |  |  |  +-> Hours (hex)
         *      |  |  |  |  |  |  |  |  +-> Minutes (hex)
         *      |  |  |  |  |  |  |  |  |  +-> Seconds (hex)
         *      |  |  |  |  |  |  |  |  |  |  +-> addition length in bytes
         * [00 13 AA 38 02 01 0B 09 0C 2A 22 1A 02 02 01 46 00 03 90 02 00 36 00 00 00 0F 04 00 00 00 F8 FF D1 59 A6 57 5E CE ]
         * Get Controller Info Complete
         */
        initializeResponse();
    }

    String getSuffix() {
        StringBuffer b = new StringBuffer();
        b.append(asChars(26, 1));
//[.02 02 01 46 00 03 90 02 00 36 00 00 00 0F 04 00 00 00 F8 FF D1 59 A6 57 5E CE ]
        b.append(asChars(2, 1));
        b.append(asChars(2, 1));
        b.append(asChars(1, 1));
        b.append(asChars(70, 1));
        b.append(asChars(0, 1));
        b.append(asChars(3, 1));
        b.append(asChars(144, 1));
        b.append(asChars(2, 1));
        b.append(asChars(0, 1));
        b.append(asChars(54, 1));
        b.append(asChars(0, 1));
        b.append(asChars(0, 1));
        b.append(asChars(0, 1));
        b.append(asChars(15, 1));
        b.append(asChars(4, 1));
        b.append(asChars(0, 1));
        b.append(asChars(0, 1));
        b.append(asChars(0, 1));
        b.append(asChars(248, 1));
        b.append(asChars(255, 1));
        b.append(asChars(209, 1));
        b.append(asChars(89, 1));
        b.append(asChars(166, 1));
        b.append(asChars(87, 1));
        b.append(asChars(94, 1));
        b.append(asChars(206, 1));
        return b.toString();
    }
}
