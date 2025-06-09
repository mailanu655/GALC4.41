package com.honda.mfg.device.plc.omron.messages;

/**
 * User: Jeffrey M Lutz
 * Date: Oct 6, 2010
 * Time: 11:36:08 AM
 */

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
 * main Request Code (MRC )- 1 char (7 for clock read)
 * Sub-Request Code (SRC) - 1 char (1 for clock Read)
 */

public class FinsClockReadRequest extends FinsRequestBase {


    public FinsClockReadRequest(int destinationNode, int sourceNode, int serviceId) {
        super(destinationNode, sourceNode, serviceId, FinsCommand.CLOCK_READ);
        getMessageRequest();
    }
}
