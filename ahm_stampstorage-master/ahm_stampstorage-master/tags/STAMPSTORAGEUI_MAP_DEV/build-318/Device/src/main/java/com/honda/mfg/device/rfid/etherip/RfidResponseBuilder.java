package com.honda.mfg.device.rfid.etherip;

import com.honda.mfg.device.exceptions.UnknownResponseException;
import com.honda.mfg.device.rfid.etherip.messages.*;

import java.util.Date;

/**
 * User: Jeffrey Lutz
 * Date: Sep 24, 2010
 * Time: 1:27:46 PM
 */
public class RfidResponseBuilder {

    private RfidResponseParser parser;

    public RfidResponseBuilder(RfidResponseParser parser) {
        this.parser = parser;
    }

    public RfidResponse buildRfidResponse(String rfidResponse) {
        if (!parser.validLength(rfidResponse)) {
            throw new UnknownResponseException(rfidResponse);
        }
        RfidCommand rfidCommand = parser.getRfidCommand(rfidResponse);
        int instanceCounter = parser.getInstanceCounter(rfidResponse);
        int nodeId = parser.getNodeId(rfidResponse);
        Date responseDate = parser.parseDate(rfidResponse);
        //todo consider using a hash map for code - class lookup instead of ifs
        if (RfidCommand.CLEAR_PENDING_RESPONSES.equals(rfidCommand)) {
            return new RfidClearPendingResponsesResponse(instanceCounter, nodeId, responseDate);
        } else if (RfidCommand.GET_CONTROLLER_CONFIG.equals(rfidCommand)) {
            return new RfidGetControllerConfigurationResponse(instanceCounter, nodeId, responseDate);
        } else if (RfidCommand.GET_CONTROLLER_INFO.equals(rfidCommand)) {
            return new RfidGetControllerInfoResponse(instanceCounter, nodeId, responseDate);
        } else if (RfidCommand.READ_TAG_ID.equals(rfidCommand)) {
            String tagId = parser.getTagIdString(rfidResponse);
            return new RfidReadTagIdResponse(instanceCounter, nodeId, responseDate, tagId);
        } else if (RfidCommand.ERROR.equals(rfidCommand)) {
            return new RfidReadTagIdResponse(instanceCounter, nodeId, responseDate, null);
        } else if (RfidCommand.READ_TAG.equals(rfidCommand)) {
            String tagValue = parser.getTagValue(rfidResponse);
            return new RfidReadTagResponse(instanceCounter, nodeId, responseDate, tagValue);
        } else if (RfidCommand.WRITE_TAG.equals(rfidCommand)) {
            return new RfidWriteTagResponse(instanceCounter, nodeId, responseDate);
        }
        throw new UnknownResponseException(rfidResponse);
    }
}
