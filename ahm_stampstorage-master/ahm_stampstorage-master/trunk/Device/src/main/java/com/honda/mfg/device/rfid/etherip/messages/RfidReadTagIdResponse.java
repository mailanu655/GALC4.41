package com.honda.mfg.device.rfid.etherip.messages;

import java.util.Date;

/**
 * User: Jeffrey M Lutz
 * Date: Nov 16, 2010
 */
public class RfidReadTagIdResponse extends RfidResponseBase {
    private static final int MAX_TAG_ID_LENGTH = 16;

    private String tagId;

    public RfidReadTagIdResponse(int instanceCounter, int nodeId, Date responseDate, String tagId) {
        super(RfidCommand.READ_TAG_ID, instanceCounter, nodeId, responseDate);
        this.tagId = tagId;
        /**     +-> Length (in words)
         *      |  +-> Literal (0xAA)
         *      |  |  +-> Command (0x7)
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
         * [00 06 AA 07 03 01 01 03 16 04 14 02 E0 04 AC 93 ]
         *         +-+--------------------------+-> Read Tag Id error response "Tag Not Found"
         * [00 06 FF FF 03 01 01 03 16 04 14 01 07 00 ]
         * 	 Node 1 	Tag Not Found
         */
        initializeResponse();
    }

    String getCommand() {
        StringBuilder b = new StringBuilder();
        if (isTagIdValid()) {
            b.append(asChars(170, 1));   // 0xAA
            b.append(asChars(7, 1));   // 0x07
        } else {
            this.setRfidCommand(RfidCommand.ERROR);
            b.append(asChars(255, 1));   // 0xFF
            b.append(asChars(255, 1));   // 0xFF
        }
        return b.toString();
    }

    private boolean isTagIdValid() {
        return tagId != null && tagId.length() > 0 && tagId.length() <= MAX_TAG_ID_LENGTH;
    }

    String getSuffix() {
        return getSecondaryLengthAndTags(tagId);
    }

    public String getTagId() {
        return tagId;
    }

    private String getSecondaryLengthAndTags(String tagId) {
        StringBuilder sb = new StringBuilder();
        // TODO  Consider adding more logic to deal with more than just "tag not found".
        if (tagId == null || tagId.length() == 0) {
            sb.append(asChars(1, 1));
            sb.append(asChars(7, 1));
            sb.append(asChars(0, 1));
        } else {
            sb.append(asChars(tagId.length(), 1));
            sb.append(tagId);
        }
        return sb.toString();
    }
}
