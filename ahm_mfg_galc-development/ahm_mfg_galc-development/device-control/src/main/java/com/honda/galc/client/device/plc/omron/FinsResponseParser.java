package com.honda.galc.client.device.plc.omron;

import com.honda.galc.client.device.plc.omron.messages.FinsCommand;

/**
 * User: Mihail Chirita
 * Date: Sep 24, 2010
 */
public class FinsResponseParser {

    private static final int REQUEST_CODE_INDEX = 26;
    private static final int SUBREQUEST_CODE_INDEX = 27;
    private static final int SRC_NODE_FOR_INITIALIZE_INDEX = 19;
    private static final int DEST_NODE_FOR_INITIALIZE_INDEX = 23;
    private static final int SRC_NODE_INDEX = 23;
    private static final int DEST_NODE_INDEX = 20;
    private static final int SERVICE_ID_INDEX = 25;
    private static final int DATA_INDEX = 30;
    private static final int INITIALIZE_RESPONSE_LENGTH = 24;

    public FinsCommand parseFinsCommand(StringBuilder finsResponse) {
        if (isInitializeResponse(finsResponse)) {
            return FinsCommand.INITIALIZE;
        }

        int tens = finsResponse.charAt(REQUEST_CODE_INDEX) * 10;
        int ones = finsResponse.charAt(SUBREQUEST_CODE_INDEX);
        int total = tens + ones;
        for (FinsCommand c : FinsCommand.values()) {
            if (total == c.getCode()) {
                return c;
            }
        }
        return FinsCommand.UNKNOWN;
    }

    public boolean isValidLength(StringBuilder finsResponse) {
        if (finsResponse == null || finsResponse.length() < INITIALIZE_RESPONSE_LENGTH) {
            return false;
        }
        return true;
    }

    private boolean isInitializeResponse(StringBuilder response) {
        String node = response != null && response.length() > 11 ? response.substring(8, 12) : null;
        return response != null && response.length() == INITIALIZE_RESPONSE_LENGTH
                && node.charAt(0) == '\0'
                && node.charAt(1) == '\0'
                && node.charAt(2) == '\0'
                && node.charAt(3) == '\1';
    }

    public int parseRequestCode(StringBuilder finsResponse) {
        return finsResponse.charAt(REQUEST_CODE_INDEX);
    }

    public int parseSubrequestCode(StringBuilder finsResponse) {
        return finsResponse.charAt(SUBREQUEST_CODE_INDEX);
    }

    public int parseSourceNodeForInitialize(StringBuilder finsResponse) {
        return finsResponse.charAt(SRC_NODE_FOR_INITIALIZE_INDEX);
    }

    public int parseDestinationNodeForInitialize(StringBuilder finsResponse) {
        return finsResponse.charAt(DEST_NODE_FOR_INITIALIZE_INDEX);
    }

    public int parseDestinationNode(StringBuilder finsResponse) {
        return finsResponse.charAt(DEST_NODE_INDEX);
    }

    public int parseSourceNode(StringBuilder finsResponse) {
        return finsResponse.charAt(SRC_NODE_INDEX);
    }

    public int parseServiceId(StringBuilder finsResponse) {
        return finsResponse.charAt(SERVICE_ID_INDEX);
    }

    public StringBuilder parseData(StringBuilder finsResponse) {
        return new StringBuilder(finsResponse.substring(DATA_INDEX));
    }
}
