package com.honda.mfg.device.plc.omron;

import com.honda.mfg.device.exceptions.UnknownResponseException;
import com.honda.mfg.device.plc.omron.messages.*;

/**
 * User: Jeffrey Lutz
 * Date: Sep 24, 2010
 * Time: 1:27:46 PM
 */
public class FinsResponseBuilder {

    private FinsResponseParser parser;

    public FinsResponseBuilder(FinsResponseParser parser) {
        this.parser = parser;
    }

    public FinsResponse buildFinsResponse(String finsResponse) {
        if (!parser.isValidLength(finsResponse)) {
            throw new UnknownResponseException(finsResponse);
        }
        FinsCommand finsCommand = parser.parseFinsCommand(finsResponse);
        //todo consider using a hash map for code - class lookup instead of ifs
        if (FinsCommand.INITIALIZE.equals(finsCommand)) {
            return new FinsInitializeResponse(parser.parseDestinationNodeForInitialize(finsResponse), parser.parseSourceNodeForInitialize(finsResponse));
        }
        if (FinsCommand.CLOCK_READ.equals(finsCommand)) {
            return new FinsClockReadResponse(parser.parseDestinationNode(finsResponse), parser.parseSourceNode(finsResponse), parser.parseServiceId(finsResponse), parser.parseData(finsResponse));
        }
        if (FinsCommand.MEMORY_READ.equals(finsCommand)) {
            return new FinsMemoryReadResponse(parser.parseDestinationNode(finsResponse), parser.parseSourceNode(finsResponse), parser.parseServiceId(finsResponse), parser.parseData(finsResponse));
        }
        if (FinsCommand.MEMORY_WRITE.equals(finsCommand)) {
            return new FinsMemoryWriteResponse(parser.parseDestinationNode(finsResponse), parser.parseSourceNode(finsResponse), parser.parseServiceId(finsResponse));
        }
        throw new UnknownResponseException(finsResponse);
    }
}
