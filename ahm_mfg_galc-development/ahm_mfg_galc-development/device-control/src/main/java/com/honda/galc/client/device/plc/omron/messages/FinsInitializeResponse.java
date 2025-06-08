package com.honda.galc.client.device.plc.omron.messages;

/**
 * User: Jeffrey M Lutz
 * Date: Sep 10, 2010
 * Time: 7:26:59 AM
 */
public class FinsInitializeResponse extends FinsMessageBase implements FinsResponse {

    /**
     * Message structure:
     * Header:
     * FINS   - 4 chars
     * length of data - 4 chars from rfidCommand to end of FINS frame (12 or 0c hex)
     * misc fixed length:  12 chars (0 for all 12 chars)
     */

    public FinsInitializeResponse(int destinationNode, int sourceNode) {
        super(destinationNode, sourceNode, 1, FinsCommand.INITIALIZE);

        StringBuilder b = new StringBuilder();
        b.append("FINS");
        b.append(asChars(16, 4));
        b.append(asChars(1, 4));
        b.append(asChars(0, 4));
        b.append(asChars(sourceNode, 4));
        b.append(asChars(destinationNode, 4));
        setFinsMessage(b);
    }

    public StringBuilder getFinsResponse() {
        return getFinsMessage();
    }

    public StringBuilder getData() {
        return NO_DATA;
    }
}
