package com.honda.mfg.device.rfid.etherip.messages;

/**
 * User: Jeffrey M Lutz
 * Date: Nov 9, 2010
 */
public class RfidReadTagRequest extends RfidRequestBase {

    /**
     * Raw Command 	 RFID Controller@10.72.85.15 	 Node 1 	[FF 01 00 06 AA 07 00 01 03 E8 00 00 00 00 ]
     * Command 	          Cobalt HF@10.72.85.15 	 Node 1 	Tag Read ID (Timeout=1000)
     * Raw Response 	  Cobalt HF@10.72.85.15 	 Node 1 	[00 07 FF FF 03 01 01 03 16 04 14 01 07 00 ]
     * Error      	      Cobalt HF@10.72.85.15 	 Node 1 	Tag Not Found
     */

    public RfidReadTagRequest(int startAddress, int wordSize) {
        this(DEFAULT_NODE_ID, DEFAULT_READ_TIMEOUT, startAddress, wordSize);
    }

    RfidReadTagRequest(int nodeId, int readTimeout, int startAddress, int wordSize) {
        super(RfidCommand.READ_TAG, nodeId, readTimeout, startAddress, wordSize);
    }
}
