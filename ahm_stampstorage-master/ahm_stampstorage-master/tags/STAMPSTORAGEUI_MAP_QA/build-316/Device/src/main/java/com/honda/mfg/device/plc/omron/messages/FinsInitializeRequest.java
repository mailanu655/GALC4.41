package com.honda.mfg.device.plc.omron.messages;

import com.honda.mfg.device.messages.MessageRequest;

/**
 * User: Jeffrey M Lutz
 * Date: Sep 10, 2010
 * Time: 7:26:59 AM
 */
public class FinsInitializeRequest extends FinsMessageBase implements MessageRequest {

    /**
     * Message structure:
     * Header:
     * FINS   - 4 chars
     * length of data - 4 chars from rfidCommand to end of FINS frame (12 or 0c hex)
     * misc fixed length:  12 chars (0 for all 12 chars)
     */

    public FinsInitializeRequest() {
        super(0, 0, 1, null);
        StringBuilder b = new StringBuilder();
        b.append("FINS");
        b.append(asChars(12, 4));
        b.append(asChars(0, 12));
        setFinsMessage(b.toString());
    }

    @Override
    public int hashCode() {
        return getFinsMessage().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof FinsInitializeRequest);
    }


    @Override
    public String getMessageRequest() {
        return getFinsMessage();

    }
}
