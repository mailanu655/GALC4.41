package com.honda.galc.client.device.plc.omron.messages;

/**
 * User: Jeffrey M Lutz
 * Date: Sep 23, 2010
 * Time: 10:33:31 AM
 */
public class FinsResponseBase extends FinsMessageBase
        implements FinsResponse {

    /**
     * Message structure:
     * Header:
     * FINS   - 4 chars
     * *** length of data - 4 chars from rfidCommand to end of FINS frame
     * fins TCP header rfidCommand - 4 chars (2)
     * error code - 4 chars (0)
     * Message Body:
     * Information Control Field (ICF) - 1 char (128 or 0x80 hex)
     * Reserved (RSV) - 1 char (0)
     * Gateway count (GCT)- 1 char (2)
     * Destination Network Address (DNA) - 1 char (0 for local address, 1-127 otherwise)
     * *** Destination Node Address (DA1)- 1 char - dynamically assigned - PLC node from client prospective
     * Destination Unit Address (DA2) - 1 char - (0)
     * Source Network Address (SNA)- 1 char - same ranges as for Destination Network Address
     * *** Source Node Address (SA1)- 1 char - dynamically assigned  - client node id
     * Source Unit Address (SA2) - 1 char (0)
     * *** Service ID (SID) - 1 char  - process id data is sent from
     * main Request Code (MRC )- 1 char (1)
     * Sub-Request Code (SRC) - 1 char (1 for Read 2 for Write)
     * *** error code - 2 chars ?  (0-->no error)  AKA END CODE
     * *** Data - variable length (in our case words) - needed for write, empty for read
     */

    private String finsBody;
    private StringBuilder data;


    public FinsResponseBase(int destinationNode, int sourceNode, int serviceId, FinsCommand finsCommand, StringBuilder data) {
        super(destinationNode, sourceNode, serviceId, finsCommand);
        this.data = data;
        this.finsBody = finsBody();
        setFinsMessage(finsResponse());
    }

    public StringBuilder getFinsResponse() {
        return getFinsMessage();
    }

    public StringBuilder getData() {
        return data;
    }

    public String toString() {
        String separator = ", ";
        StringBuilder toStringBuilder = new StringBuilder();
        toStringBuilder.append(getClass());
        toStringBuilder.append("[");
        toStringBuilder.append("destinationNode=");
        toStringBuilder.append(getDestinationNode());
        toStringBuilder.append(separator);
        toStringBuilder.append("sourceNodeNode=");
        toStringBuilder.append(getSourceNode());
        toStringBuilder.append(separator);
        toStringBuilder.append("serviceId=");
        toStringBuilder.append(getServiceId());
        toStringBuilder.append(separator);
        toStringBuilder.append("requestCode=");
        toStringBuilder.append(getFinsCommand().getRequestCode());
        toStringBuilder.append(separator);
        toStringBuilder.append("subrequestCode=");
        toStringBuilder.append(getFinsCommand().getSubrequestCode());
        if (hasData()) {
            toStringBuilder.append(separator);
            toStringBuilder.append("data=|");
            toStringBuilder.append(data);
            toStringBuilder.append("|");
        }
        toStringBuilder.append("]");
        return toStringBuilder.toString();
    }

    private StringBuilder finsResponse() {
        StringBuilder builder = new StringBuilder();
        builder.append(finsPrefix());
        builder.append(finsBody);
        return builder;
    }

    private String finsBody() {
        StringBuilder builder = new StringBuilder();
        builder.append(asChars(ICF_RESPONSE, 1));
        builder.append(asChars(RSV, 1));
        builder.append(asChars(GCT, 1));
        builder.append(asChars(DNA, 1));
        builder.append(asChars(getDestinationNode(), 1));
        builder.append(asChars(DA2, 1));
        builder.append(asChars(SNA, 1));
        builder.append(asChars(getSourceNode(), 1));
        builder.append(asChars(SA2, 1));
        builder.append(asChars(getServiceId(), 1));
        builder.append(asChars(getFinsCommand().getRequestCode(), 1));
        builder.append(asChars(getFinsCommand().getSubrequestCode(), 1));
        builder.append(asChars(END_CODE, 2));
        builder.append(data);
        return builder.toString();
    }

    private int calculateFinsMessageLength() {
        return TCP_HEADER_COMMAND_LEGTH + ERROR_CODE_LENGTH + finsBody.length();
    }

    private String finsPrefix() {
        StringBuilder builder = new StringBuilder();
        builder.append(FINS);
        builder.append(asChars(calculateFinsMessageLength(), FINS_MESSAGE_LENGTH));
        builder.append(asChars(TCP_HEADER_COMMAND, TCP_HEADER_COMMAND_LEGTH));
        builder.append(asChars(ERROR_CODE, ERROR_CODE_LENGTH));
        return builder.toString();
    }

    private boolean hasData() {
        return !NO_DATA.equals(data);
    }
}
