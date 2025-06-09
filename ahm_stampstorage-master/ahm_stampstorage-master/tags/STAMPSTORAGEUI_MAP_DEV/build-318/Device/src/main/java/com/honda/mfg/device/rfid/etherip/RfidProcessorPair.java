package com.honda.mfg.device.rfid.etherip;

import com.honda.mfg.device.MessageRequestProcessor;

/**
 * User: Jeffrey M Lutz
 * Date: Nov 10, 2010
 */
public interface RfidProcessorPair {

    public MessageRequestProcessor getRequestProcessor();

    public long getInitializeRequiredTime();
}
