package com.honda.mfg.device.plc.omron.messages;

/**
 * User: Jeffrey M Lutz
 * Date: Sep 23, 2010
 * Time: 10:33:12 AM
 */
public class FinsMemoryReadResponse extends FinsResponseBase {

    public FinsMemoryReadResponse(int destinationNode, int sourceNode, int serviceId, String data) {
        super(destinationNode, sourceNode, serviceId, FinsCommand.MEMORY_READ, data);
    }

}
