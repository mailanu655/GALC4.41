package com.honda.galc.client.device.plc.omron.messages;

public class FinsRequestBase extends FinsMessageBase
        implements MessageRequest {
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
     */

    public FinsRequestBase(int destinationNode, int sourceNode, int serviceId, FinsCommand finsCommand) {
        super(destinationNode, sourceNode, serviceId, finsCommand);
        getFinsFirstHalfBody();
    }

    public StringBuilder getMessageRequest() {
        StringBuilder builder = new StringBuilder();
        builder.append(finsPrefix());
        builder.append(getFinsBody());
        return builder;
    }

    private String getFinsBody() {
        StringBuilder builder = new StringBuilder();
        builder.append(getFinsFirstHalfBody());
        builder.append(getFinsSecondHalfBody());
        return builder.toString();
    }

    private String getFinsFirstHalfBody() {
        StringBuilder builder = new StringBuilder();
        builder.append(asChars(ICF_REQUEST, 1));
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
        return builder.toString();
    }

    protected String getFinsSecondHalfBody() {
        return "";
    }

    private int calculateFinsMessageLength() {
        return TCP_HEADER_COMMAND_LEGTH + ERROR_CODE_LENGTH + getFinsBody().length();
    }

    private String finsPrefix() {
        StringBuilder builder = new StringBuilder();
        builder.append(FINS);
        builder.append(asChars(calculateFinsMessageLength(), FINS_MESSAGE_LENGTH));
        builder.append(asChars(TCP_HEADER_COMMAND, TCP_HEADER_COMMAND_LEGTH));
        builder.append(asChars(ERROR_CODE, ERROR_CODE_LENGTH));
        return builder.toString();
    }
}