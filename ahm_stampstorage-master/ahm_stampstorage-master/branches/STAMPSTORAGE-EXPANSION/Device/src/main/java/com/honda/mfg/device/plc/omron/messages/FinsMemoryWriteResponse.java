package com.honda.mfg.device.plc.omron.messages;

/**
 * User: Jeffrey M Lutz
 * Date: Sep 20, 2010
 * Time: 3:23:19 PM
 */
public class FinsMemoryWriteResponse extends FinsResponseBase {

    public FinsMemoryWriteResponse(int destinationNode, int sourceNode, int serviceId) {
        super(destinationNode, sourceNode, serviceId, FinsCommand.MEMORY_WRITE, NO_DATA);
    }

}
