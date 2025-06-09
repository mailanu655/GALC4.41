package com.honda.mfg.device.rfid.etherip.messages;

/**
 * User: Jeffrey M Lutz
 * Date: Nov 9, 2010
 * <p/>
 * Raw Command 	 RFID Controller@10.72.85.15 	 Node 1 	[FF 01 00 06 AA 07 00 01 03 E8 00 00 00 00 ]
 * Command 	          Cobalt HF@10.72.85.15 	 Node 1 	Tag Read ID (Timeout=1000)
 * Raw Response 	  Cobalt HF@10.72.85.15 	 Node 1 	[00 07 FF FF 03 01 01 03 16 04 14 01 07 00 ]
 * Error      	      Cobalt HF@10.72.85.15 	 Node 1 	Tag Not Found
 */
public class RfidWriteTagRequest extends RfidRequestBase {

    public RfidWriteTagRequest(int startAddress, String tagValue) {
        this(DEFAULT_NODE_ID, DEFAULT_READ_TIMEOUT, startAddress, tagValue);
    }

    RfidWriteTagRequest(int nodeId, int readTimeout, int startAddress, String tagValue) {
        super(RfidCommand.WRITE_TAG, nodeId, readTimeout, startAddress, tagValue);
    }
}
