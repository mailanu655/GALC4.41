package com.honda.mfg.device.rfid.etherip.messages;

import java.util.Date;

/**
 * User: Jeffrey M Lutz
 * Date: Nov 16, 2010
 */
public class RfidWriteTagResponse extends RfidResponseBase {

    public RfidWriteTagResponse(int instanceCounter, int nodeId, Date responseDate) {
        super(RfidCommand.WRITE_TAG, instanceCounter, nodeId, responseDate);
        /**     +-> Length (in words)
         *      |  +-> Literal (0xAA)
         *      |  |  +-> Command (0x5)
         *      |  |  |  +-> Instance Counter (hex)
         *      |  |  |  |  +-> Node ID (hex)
         *      |  |  |  |  |  +-> Month (hex)
         *      |  |  |  |  |  |  +-> Day (hex)
         *      |  |  |  |  |  |  |  +-> Hours (hex)
         *      |  |  |  |  |  |  |  |  +-> Minutes (hex)
         *      |  |  |  |  |  |  |  |  |  +-> Seconds (hex)
         *      |  |  |  |  |  |  |  |  |  |  +-> Additional Length (in words)
         *      |  |  |  |  |  |  |  |  |  |  |  +--+-> Tag ID #1
         *      |  |  |  |  |  |  |  |  |  |  |  |  |  +--+-> Tag ID #2
         * [00 06 AA 05 03 01 01 03 16 04 14 02 E0 04 AC 93 ]
         *         +-+--------------------------+-> Write Tag error response "Tag Not Found"
         * [00 06 FF FF 03 01 01 03 16 04 14 01 07 00 ]
         * 	 Node 1 	Tag Not Found
         */
        initializeResponse();
    }

    String getSuffix() {
        return new String(asChars(0, 1));
    }
}
