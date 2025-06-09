package com.honda.mfg.device.plc.omron.messages;

import com.honda.mfg.device.plc.PlcMemory;
import com.honda.mfg.device.plc.omron.exceptions.FinsRequestException;

/**
 * User: Jeffrey M Lutz
 * Date: Sep 10, 2010
 * Time: 7:26:59 AM
 */
public class FinsMemoryWriteRequest extends FinsMemoryRequestBase {

    public FinsMemoryWriteRequest(PlcMemory memory, int destinationNode, int sourceNode, int serviceId, String data) {
        super(memory, destinationNode, sourceNode, serviceId, FinsCommand.MEMORY_WRITE, data);
    }

    protected String paddedData() {
        String originalData = getOriginalData();
        if (originalData == null) throw new FinsRequestException("Cannot write a null messages");
        //memory size is in words - 2 chars per word
        int expectedLength = getMemory().getMemorySize() * 2;
        StringBuilder builder = new StringBuilder();
        builder.append(originalData);
        for (int i = 0; i < expectedLength - originalData.length(); i++) {
            builder.append('\u0000');
        }
        return builder.toString();
    }

}
