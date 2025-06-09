package com.honda.mfg.device.plc.omron;

import com.honda.mfg.device.MessageRequestProcessor;

/**
 * User: Mihail Chirita
 * Date: Sep 27, 2010
 * Time: 1:15:27 PM
 */
public interface FinsProcessorPair {

    public MessageRequestProcessor getRequestProcessor();

    public long getInitializeRequiredTime();
}
