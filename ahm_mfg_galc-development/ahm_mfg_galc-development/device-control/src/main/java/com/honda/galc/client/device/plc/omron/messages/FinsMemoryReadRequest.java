package com.honda.galc.client.device.plc.omron.messages;

import com.honda.galc.client.device.plc.IPlcMemory;

/**
 * User: Jeffrey M Lutz
 * Date: Sep 10, 2010
 * Time: 7:26:59 AM
 */
public class FinsMemoryReadRequest extends FinsMemoryRequestBase {

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
     * *** Memory Area code - 1 char
     * *** Start Address - 2 chars
     * Don't know - 1 char (set to 0)
     * *** Number of items  - 2 chars (in our case number of words or 2 char pairs)
     * *** Data - variable length (in our case words) - needed for write, empty for read
     */


    public FinsMemoryReadRequest(IPlcMemory memory, int destinationNode, int sourceNode, int serviceId) {
        super(memory, destinationNode, sourceNode, serviceId, FinsCommand.MEMORY_READ, NO_DATA);
    }

    protected StringBuilder paddedData(IPlcMemory plcMemory) {
        return NO_DATA;
    }

}
