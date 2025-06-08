package com.honda.galc.client.device.plc.omron.messages;

import com.honda.galc.client.device.plc.IPlcMemory;
import com.honda.galc.client.device.plc.omron.exceptions.FinsRequestException;

/**
 * User: Jeffrey M Lutz
 * Date: Sep 10, 2010
 * Time: 7:26:59 AM
 */
public class FinsMemoryWriteRequest extends FinsMemoryRequestBase {

    public FinsMemoryWriteRequest(IPlcMemory memory, int destinationNode, int sourceNode, int serviceId, StringBuilder data) {
        super(memory, destinationNode, sourceNode, serviceId, FinsCommand.MEMORY_WRITE, data);
    }

    protected StringBuilder paddedData(IPlcMemory plcMemory) {
        StringBuilder originalData = getOriginalData();
        if (originalData == null) throw new FinsRequestException("Cannot write a null messages");
      	StringBuilder builder = new StringBuilder();
      	
        if (plcMemory.getBitAddress() >= 0) {
        	// bit can either be set to 1 or 0 only
        	if (new Integer(originalData.toString()) > 0)
        		builder.append((char) 1);
        	else 
        		builder.append((char) 0);
        } else {
        	//memory size is in words - 2 chars per word
        	builder.append(originalData);

            if (originalData.length() % 2 > 0) 
            	builder.append(' ');
        }


        return builder;
    }
}
