package com.honda.mfg.device.rfid.etherip.messages;

import java.util.Date;

/**
 * User: Jeffrey M Lutz
 * Date: Nov 16, 2010
 */
public class RfidReadTagResponse extends RfidResponseBase {

    private String tagValue;

    public RfidReadTagResponse(int instanceCounter, int nodeId, Date responseDate, String tagValue) {
        super(RfidCommand.READ_TAG, instanceCounter, nodeId, responseDate);
        this.tagValue = tagValue;
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
         *         +-+--------------------------+-> Read Tag Id error response "Tag Not Found"
         * [00 06 FF FF 03 01 01 03 16 04 14 01 07 00 ]
         * 	 Node 1 	Tag Not Found
         */
        initializeResponse();
    }

    String getSuffix() {
        return getSecondaryLengthAndTagValue();
    }

    public String getTagValue() {
        return tagValue;
    }

    private String getSecondaryLengthAndTagValue() {
        StringBuilder sb = new StringBuilder();
        // TODO  Consider adding more logic to deal with more than just "tag not found".
        if (tagValue == null || tagValue.length() == 0) {
            sb.append(asChars(0, 1));
            return sb.toString();
        }
        sb.append(asChars(tagValue.length(), 1));
        sb.append(tagValue);
        return sb.toString();
    }
}
